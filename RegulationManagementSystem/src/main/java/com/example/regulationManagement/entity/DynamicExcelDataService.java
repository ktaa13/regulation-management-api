package com.example.regulationManagement.entity;

import java.util.Map;

import com.example.regulationManagement.converter.HashMapConverter;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "excel_data")
public class DynamicExcelDataService {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Convert(converter = HashMapConverter.class)  // Convert map to JSON for the db
    @Column(name = "data", columnDefinition = "jsonb")  // Ensure correct type for jsonb column
    private Map<String, String> data;

    // Default constructor
    public DynamicExcelDataService() {}

    // Constructor accepting Map for data
    public DynamicExcelDataService(Map<String, String> data) {
        this.data = data;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Map<String, String> getData() {
        return data;
    }

    public void setData(Map<String, String> data) {
        this.data = data;
    }
}
