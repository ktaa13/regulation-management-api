package com.example.demo;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/data")
@CrossOrigin(origins = "http://localhost:3000")
public class DynamicDataController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // Endpoint to fetch column names dynamically
    @GetMapping("/columns")
    public List<String> getColumns() {
        String sql = "SELECT column_name FROM information_schema.columns WHERE table_name = 'regulations'";
        return jdbcTemplate.queryForList(sql, String.class);
    }

    // Endpoint to fetch paginated data
    @GetMapping
    public List<Map<String, Object>> getData(@RequestParam int page, @RequestParam int size) {
        int offset = page * size;
        String sql = "SELECT * FROM \"RegulationDB\".regulations LIMIT ? OFFSET ?";
        return jdbcTemplate.queryForList(sql, size, offset);
    }

    // Endpoint to fetch data details by ID (linked from the main table)
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getDataById(@PathVariable String id) {
    	System.out.println("HEYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYY" +" " +id);
    	
        String sql = "SELECT *  FROM \"RegulationDB\".regulations\r\n"
        		+ "where \"RegulationDB\".regulations.الرقم  like ?";
        List<Map<String, Object>> result = jdbcTemplate.queryForList(sql, id);
        System.out.println(id);
        
        if (result.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "No data found for ID " + id));
        }
        
        return ResponseEntity.ok(result.get(0)); // Return the first result as JSON
        
    }
}
