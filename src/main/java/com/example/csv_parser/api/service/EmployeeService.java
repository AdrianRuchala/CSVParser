package com.example.csv_parser.api.service;

import com.example.csv_parser.api.model.Employee;
import com.example.csv_parser.api.repository.EmployeeRepository;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Service;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class EmployeeService {
    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public void importFromCsv() {
        InputStream inputFile = getClass().getClassLoader().getResourceAsStream("Employee_Data_Sample.csv");
        if (inputFile == null) {
            throw new IllegalArgumentException("File not found");
        }

        try (InputStreamReader reader = new InputStreamReader(inputFile);
             CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader())) {

            List<Employee> employees = new ArrayList<>();

            for (CSVRecord record : csvParser) {
                String employeeCode = record.get("Employee Code");
                String firstName = record.get("First Name");
                String lastName = record.get("Last Name");
                String position = record.get("Position");
                String department = record.get("Department");
                String email = record.get("Email");
                String phone = record.get("Phone");

                Employee employee = new Employee(
                        employeeCode,
                        firstName,
                        lastName,
                        position,
                        department,
                        email,
                        phone
                );

                employees.add(employee);
            }

            employeeRepository.saveAll(employees);

        } catch (Exception e) {
            throw new RuntimeException("Failed to parse CSV file", e);
        }
    }

    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    public Optional<Employee> getEmployee(String id) {
        return employeeRepository.findById(id);
    }

    public List<Employee> getEmployeesByDepartment(String department) {
        List<Employee> allEmployeesList;
        allEmployeesList = employeeRepository.findAll();
        List<Employee> employeesList = new ArrayList<>(List.of());
        for (Employee employee: allEmployeesList) {
            if(Objects.equals(employee.getDepartment(), department.toUpperCase())){
                employeesList.add(employee);
            }
        }
        return employeesList;
    }
}
