package com.example.regulationManagement.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.regulationManagement.repository.DynamicExcelDataRepository;

@Service
public class DynamicExcelDataService {

    @Autowired
    private DynamicExcelDataRepository dynamicExcelDataRepository;

    public DynamicExcelDataService(Map<String, Object> row) {
		// TODO Auto-generated constructor stub
	}

	/**
     * Saves the processed data (list of maps) to the database.
     *
     * @param data List of maps representing rows from the Excel file
     * @return List of saved DynamicExcelData entities
     */
    public List<DynamicExcelDataService> saveData(List<Map<String, Object>> data) {
        // Convert each map into a DynamicExcelData entity and save to the database
        List<DynamicExcelDataService> entities = data.stream()
                .map(row -> new DynamicExcelDataService(row))  // Create entity for each row
                .toList();

        return dynamicExcelDataRepository.saveAll(entities);  // Save all entities
    }
}
