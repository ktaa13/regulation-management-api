package com.example.regulationManagement.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.regulationManagement.entity.DynamicExcelDataService;
import com.example.regulationManagement.util.ExcelFileProcessor;

@RestController
@RequestMapping("/api")
public class FileUploadController {

    private final ExcelFileProcessor excelFileProcessor;
    private final DynamicExcelDataService dataService;

    @Autowired
    public FileUploadController(ExcelFileProcessor excelFileProcessor, DynamicExcelDataService dataService) {
        this.excelFileProcessor = excelFileProcessor;
        this.dataService = dataService;
    }

    // Handle file upload
    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        List<Map<String, String>> data = ExcelFileProcessor.processExcelFile(file);

        if (data.isEmpty()) {
            return ResponseEntity.status(400).body("No data found in the uploaded file.");
        }

        // Save the data to the database
        dataService.saveData(data);
        
        return ResponseEntity.ok("File uploaded and data saved successfully.");
    }
}
