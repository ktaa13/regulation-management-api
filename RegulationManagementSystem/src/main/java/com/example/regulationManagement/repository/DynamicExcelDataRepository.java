package com.example.regulationManagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.regulationManagement.entity.DynamicExcelDataService;

@Repository
public interface DynamicExcelDataRepository extends JpaRepository<DynamicExcelDataService, Long> {
    // You can add custom query methods here if necessary
}
