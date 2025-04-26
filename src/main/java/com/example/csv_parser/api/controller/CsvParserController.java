package com.example.csv_parser.api.controller;

import com.example.csv_parser.api.model.Employee;
import com.example.csv_parser.api.service.EmployeeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

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

    @GetMapping("/{id}")
    public Optional<Employee> getEmployee(@PathVariable String id) {
        return employeeService.getEmployee(id);
    }

    @GetMapping("/department/{department}")
    public List<Employee> getEmployeesByDepartment(@PathVariable String department) {
        return employeeService.getEmployeesByDepartment(department);
    }

    @PostMapping("/import")
    public ResponseEntity<String> importEmployeesFromCsv() {
        employeeService.importFromCsv();
        return ResponseEntity.ok("CSV import triggered");
    }

}
