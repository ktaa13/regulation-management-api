package com.example.regulationManagement.util;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.*;

@Service
public class ExcelFileProcessor {

    // Process the uploaded file and return the data as a list of maps
    public static List<Map<String, String>> processExcelFile(MultipartFile file) {
        try (InputStream inputStream = file.getInputStream()) {
            return processExcelFile(inputStream);
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList(); // Return empty list on failure
        }
    }

    // Process the Excel file from InputStream
    private static List<Map<String, String>> processExcelFile(InputStream inputStream) {
        List<Map<String, String>> dataList = new ArrayList<>();
        
        try (Workbook workbook = new XSSFWorkbook(inputStream)) {
            Sheet sheet = workbook.getSheetAt(0); // Read first sheet
            Iterator<Row> rowIterator = sheet.iterator();

            if (!rowIterator.hasNext()) {
                throw new IllegalStateException("Excel file is empty.");
            }

            // Extract headers dynamically
            Row headerRow = rowIterator.next();
            List<String> headers = extractHeaders(headerRow);

            // Process each data row
            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                Map<String, String> rowData = new LinkedHashMap<>();

                // Process each cell in the row
                for (int col = 0; col < headers.size(); col++) {
                    Cell cell = row.getCell(col, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                    String cellValue = getCellValue(cell);
                    rowData.put(headers.get(col), cellValue);
                }

                // Add the row data to the list
                dataList.add(rowData);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return dataList;
    }

    // Extract headers from the first row
    private static List<String> extractHeaders(Row headerRow) {
        List<String> headers = new ArrayList<>();
        if (headerRow == null) {
            throw new IllegalStateException("Header row is missing in the Excel file.");
        }

        for (Cell cell : headerRow) {
            Object cellValue = getCellValue(cell);
            if (cellValue == null || cellValue.toString().trim().isEmpty()) {
                throw new IllegalStateException("Header cells must contain valid strings.");
            }
            headers.add(cellValue.toString().trim());
        }
        return headers;
    }

    // Extract the value from the Excel cell based on its type
    private static String getCellValue(Cell cell) {
        if (cell == null) return null;

        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue().trim();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue().toString();  // Handle dates correctly
                } else {
                    return String.valueOf((long) cell.getNumericCellValue()); // Convert numbers to string
                }
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());  // Convert booleans to string
            case FORMULA:
                try {
                    return cell.getStringCellValue();  // Try to get the string value of the formula
                } catch (IllegalStateException e) {
                    return String.valueOf(cell.getNumericCellValue());  // Fallback to number value if formula isn't string
                }
            case BLANK:
                return "";  // Return empty string for blank cells
            default:
                return null;  // Handle unknown cell types
        }
    }
}
