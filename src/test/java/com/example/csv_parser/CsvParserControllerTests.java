package com.example.csv_parser;

import com.example.csv_parser.api.model.Employee;
import com.example.csv_parser.api.service.EmployeeService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;
import java.util.Optional;

@SpringBootTest
@AutoConfigureMockMvc
public class CsvParserControllerTests { //integration tests

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private EmployeeService employeeService;

    @Test
    void shouldGetAllEmployees() throws Exception {
        Employee employee = new Employee(
                "TEST1XXX",
                "Jan",
                "Kowalski",
                "Manager",
                "IT",
                "jan.kowalski@examplecompany.com",
                "123-456-789"
        );

        Mockito.when(employeeService.getAllEmployees()).thenReturn(List.of(employee));

        mockMvc.perform(get("/api/v1/employees"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].employeeCode").value("TEST1XXX"))
                .andExpect(jsonPath("$[0].firstName").value("Jan"));
    }

    @Test
    void shouldGetEmployeeById() throws Exception {
        Employee employee = new Employee(
                "TEST1XXX",
                "Jan",
                "Kowalski",
                "Manager",
                "IT",
                "jan.kowalski@examplecompany.com",
                "123-456-789"
        );

        Mockito.when(employeeService.getEmployee("TEST1XXX")).thenReturn(Optional.of(employee));

        mockMvc.perform(get("/api/v1/employees/TEST1XXX"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.employeeCode").value("TEST1XXX"))
                .andExpect(jsonPath("$.firstName").value("Jan"));
    }

    @Test
    void shouldGetEmployeesByDepartment() throws Exception {
        Employee employee = new Employee(
                "TEST1XXX",
                "Jan",
                "Kowalski",
                "Manager",
                "IT",
                "jan.kowalski@examplecompany.com",
                "123-456-789"
        );

        Mockito.when(employeeService.getEmployeesByDepartment("IT")).thenReturn(List.of(employee));

        mockMvc.perform(get("/api/v1/employees/department/IT"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].employeeCode").value("TEST1XXX"))
                .andExpect(jsonPath("$[0].department").value("IT"));
    }

    @Test
    void shouldGetDepartmentsManagers() throws Exception {
        Employee manager = new Employee(
                "TEST1XXX",
                "Jan",
                "Kowalski",
                "Manager",
                "IT",
                "jan.kowalski@examplecompany.com",
                "123-456-789"
        );

        Mockito.when(employeeService.getDepartmentsManagers()).thenReturn(List.of(manager));

        mockMvc.perform(get("/api/v1/employees/managers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].employeeCode").value("TEST1XXX"))
                .andExpect(jsonPath("$[0].position").value("Manager"));
    }

    @Test
    void shouldImportEmployeesFromCsv() throws Exception {
        Mockito.doNothing().when(employeeService).importFromCsv();

        mockMvc.perform(post("/api/v1/employees/import"))
                .andExpect(status().isOk())
                .andExpect(content().string("CSV import triggered"));
    }

    @Test
    void shouldAddNewEmployee() throws Exception {
        Mockito.doNothing().when(employeeService).addNewEmployee(any(Employee.class));

        String newEmployeeJson = """
                {
                    "employeeCode": "TEST1XXX",
                    "firstName": "Jan",
                    "lastName": "Kowalski",
                    "position": "Manager",
                    "department": "IT",
                    "email": "jan.kowalski@examplecompany.com",
                    "phone": "123-456-789"
                }
                """;

        mockMvc.perform(post("/api/v1/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(newEmployeeJson))
                .andExpect(status().isOk())
                .andExpect(content().string("New employee added successfully"));
    }

    @Test
    void shouldDeleteEmployee() throws Exception {
        Mockito.doNothing().when(employeeService).deleteEmployee("TEST1XXX");

        mockMvc.perform(delete("/api/v1/employees/TEST1XXX"))
                .andExpect(status().isOk())
                .andExpect(content().string("Employee deleted successfully"));
    }
}
