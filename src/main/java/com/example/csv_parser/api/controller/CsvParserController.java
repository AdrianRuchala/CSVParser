package com.example.csv_parser.api.controller;

import com.example.csv_parser.api.model.Employee;
import com.example.csv_parser.api.service.EmployeeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/api/v1/employees")
public class CsvParserController {

    public final EmployeeService employeeService;

    public CsvParserController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping
    public List<Employee> getAllEmployees() {
        return employeeService.getAllEmployees();
    }

    @PostMapping("/import")
    public ResponseEntity<String> importEmployeesFromCsv() {
        employeeService.importFromCsv();
        return ResponseEntity.ok("CSV import triggered");
    }

}
