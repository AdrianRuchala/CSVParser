package com.example.csv_parser;

import com.example.csv_parser.api.model.Employee;
import com.example.csv_parser.api.repository.EmployeeRepository;
import com.example.csv_parser.api.service.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CsvParserServiceTests {    //unit Tests

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeService employeeService;

    private Employee testEmployee;

    @BeforeEach
    void setUp() {
        testEmployee = new Employee(
                "TEST1XXX",
                "Jan",
                "Kowalski",
                "Manager",
                "IT",
                "jan.kowalski@examplecompany.com",
                "123-456-789"
        );
    }

    @Test
    void shouldReturnAllEmployees() {
        when(employeeRepository.findAll()).thenReturn(List.of(testEmployee));

        List<Employee> employees = employeeService.getAllEmployees();

        assertThat(employees).hasSize(1);
        verify(employeeRepository, times(1)).findAll();
    }

    @Test
    void shouldReturnEmployeeById() {
        when(employeeRepository.findById("TEST1XXX")).thenReturn(Optional.of(testEmployee));

        Optional<Employee> employee = employeeService.getEmployee("TEST1XXX");

        assertThat(employee).isPresent();
        assertThat(employee.get().getFirstName()).isEqualTo("Jan");
    }

    @Test
    void shouldAddNewEmployee() {
        employeeService.addNewEmployee(testEmployee);

        ArgumentCaptor<Employee> employeeCaptor = ArgumentCaptor.forClass(Employee.class);
        verify(employeeRepository, times(1)).save(employeeCaptor.capture());
        assertThat(employeeCaptor.getValue().getEmployeeCode()).isEqualTo("TEST1XXX");
    }

    @Test
    void shouldDeleteEmployee() {
        employeeService.deleteEmployee("TEST1XXX");

        verify(employeeRepository, times(1)).deleteById("TEST1XXX");
    }

    @Test
    void shouldReturnEmployeesByDepartment() {
        Employee testEmployee2 = new Employee(
                "TEST2XXX",
                "Maria",
                "Nowak",
                "Manager",
                "HR",
                "maria.nowak@examplecompany.com",
                "987-654-321"
        );

        when(employeeRepository.findAll()).thenReturn(Arrays.asList(testEmployee, testEmployee2));

        List<Employee> hrEmployees = employeeService.getEmployeesByDepartment("HR");

        assertThat(hrEmployees).hasSize(1);
        assertThat(hrEmployees.get(0).getDepartment()).isEqualToIgnoringCase("HR");
    }

    @Test
    void shouldReturnDepartmentManagers() {
        Employee testEmployee2 = new Employee(
                "TEST2XXX",
                "Maria",
                "Nowak",
                "Java Developer",
                "HR",
                "maria.nowak@examplecompany.com",
                "987-654-321"
        );

        when(employeeRepository.findAll()).thenReturn(Arrays.asList(testEmployee, testEmployee2));

        List<Employee> managers = employeeService.getDepartmentsManagers();

        assertThat(managers).hasSize(1);
        assertThat(managers.get(0).getPosition().toLowerCase()).contains("manager");
    }
}
