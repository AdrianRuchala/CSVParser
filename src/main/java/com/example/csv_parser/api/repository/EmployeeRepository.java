package com.example.csv_parser.api.repository;

import com.example.csv_parser.api.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, String> {
}
