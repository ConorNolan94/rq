package com.example.rqchallenge;

import com.example.rqchallenge.employees.model.Employee;
import com.example.rqchallenge.employees.service.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class EmployeeServiceTests {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private EmployeeService employeeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllEmployees() {
        // Mock response
        Map<String, Object> mockResponse = new HashMap<>();
        mockResponse.put("status", "success");
        mockResponse.put("data", Collections.singletonList(new HashMap<String, Object>() {{
            put("id", "1");
            put("employee_name", "Conor");
            put("employee_salary", "10000");
            put("employee_age", "29");
            put("profile_image", "picture");
        }}));

        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), isNull(), any(ParameterizedTypeReference.class)))
                .thenReturn(new ResponseEntity<>(mockResponse, HttpStatus.OK));

        // get employees
        List<Employee> employees = employeeService.getAllEmployees();

        // Assertions
        assertNotNull(employees);
        assertEquals(1, employees.size());
        assertEquals("Conor", employees.get(0).getEmployeeName());
    }

    @Test
    void testGetEmployeeByID() {
        // Mock response
        Map<String, Object> mockResponse = new HashMap<>();
        mockResponse.put("status", "success");
        mockResponse.put("data", new HashMap<String, Object>() {{
            put("id", "1");
            put("employee_name", "Conor");
            put("employee_salary", "10000");
            put("employee_age", "29");
            put("profile_image", "picture");
        }});

        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), isNull(), any(ParameterizedTypeReference.class)))
                .thenReturn(new ResponseEntity<>(mockResponse, HttpStatus.OK));

        // Get employeee
        Employee employee = employeeService.getEmployeeByID("1");

        // Assertions
        assertNotNull(employee);
        assertEquals("Conor", employee.getEmployeeName());
    }


    @Test
    void testCreateEmployee_Success() {
        // Mock the response
        Map<String, Object> mockResponse = new HashMap<>();
        mockResponse.put("status", "success");
        mockResponse.put("data", new HashMap<String, Object>() {{
            put("id", "1");
            put("employee_name", "Conor");
            put("employee_salary", "10000");
            put("employee_age", "29");
            put("profile_image", "pic");
        }});

        when(restTemplate.exchange(anyString(), eq(HttpMethod.POST), any(HttpEntity.class), any(ParameterizedTypeReference.class)))
                .thenReturn(new ResponseEntity<>(mockResponse, HttpStatus.CREATED));

        Map<String, Object> employeeInput = new HashMap<>();
        employeeInput.put("name", "Conor");
        employeeInput.put("employee_salary", "10000");
        employeeInput.put("employee_age", "29");

        // Call the method under test
        Employee employee = employeeService.createEmployee(employeeInput);

        // Assertions
        assertNotNull(employee);
        assertEquals("Conor", employee.getEmployeeName());
    }

    @Test
    void testGetEmployeesByNameSearch() {
        // Mock the response for getAllEmployees
        Map<String, Object> mockResponse = new HashMap<>();
        mockResponse.put("status", "success");
        mockResponse.put("data", List.of(
                Map.of("id", "1", "employee_name", "Conor", "employee_salary", "1000000", "employee_age", "32", "profile_image", "pic2"),
                Map.of("id", "2", "employee_name", "John Doe", "employee_salary", "10000", "employee_age", "29", "profile_image", "pic")
        ));

        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), isNull(), any(ParameterizedTypeReference.class)))
                .thenReturn(new ResponseEntity<>(mockResponse, HttpStatus.OK));

        // Call the method under test
        List<Employee> employees = employeeService.getEmployeesByNameSearch("Conor");

        // Assertions
        assertNotNull(employees);
        assertEquals(1, employees.size());
        assertEquals("Conor", employees.get(0).getEmployeeName());
    }

    @Test
    void testGetHighestemployee_salaryOfEmployees() {
        // Mock the response for getAllEmployees
        Map<String, Object> mockResponse = new HashMap<>();
        mockResponse.put("status", "success");
        mockResponse.put("data", List.of(
                Map.of("id", "1", "employee_name", "Conor", "employee_salary", "1000000", "employee_age", "32", "profile_image", "pic2"),
                Map.of("id", "2", "employee_name", "John Doe", "employee_salary", "10000", "employee_age", "29", "profile_image", "pic")
        ));

        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), isNull(), any(ParameterizedTypeReference.class)))
                .thenReturn(new ResponseEntity<>(mockResponse, HttpStatus.OK));

        // Call the method under test
        int highestemployee_salary = employeeService.getHighestSalaryOfEmployees();

        // Assertions
        assertEquals(1000000, highestemployee_salary);
    }

    @Test
    void testGetTop10HighestEarningemployee_names() {
        // take mocked response from getAllEmployees
        Map<String, Object> mockResponse = new HashMap<>();
        mockResponse.put("status", "success");
        mockResponse.put("data", List.of(
                Map.of("id", "1", "employee_name", "John 1", "employee_salary", "1", "employee_age", "30", "profile_image", ""),
                Map.of("id", "2", "employee_name", "John 2", "employee_salary", "2", "employee_age", "40", "profile_image", ""),
                Map.of("id", "3", "employee_name", "John 3", "employee_salary", "3", "employee_age", "35", "profile_image", ""),
                Map.of("id", "1", "employee_name", "John 4", "employee_salary", "4", "employee_age", "30", "profile_image", ""),
                Map.of("id", "2", "employee_name", "John 5", "employee_salary", "5", "employee_age", "40", "profile_image", ""),
                Map.of("id", "3", "employee_name", "John 6", "employee_salary", "6", "employee_age", "35", "profile_image", ""),
                Map.of("id", "1", "employee_name", "John 7", "employee_salary", "7", "employee_age", "30", "profile_image", ""),
                Map.of("id", "2", "employee_name", "John 8", "employee_salary", "8", "employee_age", "40", "profile_image", ""),
                Map.of("id", "3", "employee_name", "John 9", "employee_salary", "9", "employee_age", "35", "profile_image", ""),
                Map.of("id", "1", "employee_name", "John 10", "employee_salary", "10", "employee_age", "30", "profile_image", ""),
                Map.of("id", "2", "employee_name", "John 11", "employee_salary", "11", "employee_age", "40", "profile_image", ""),
                Map.of("id", "3", "employee_name", "John 12", "employee_salary", "12", "employee_age", "35", "profile_image", ""),
                Map.of("id", "1", "employee_name", "John 13", "employee_salary", "13", "employee_age", "30", "profile_image", ""),
                Map.of("id", "2", "employee_name", "John 14", "employee_salary", "14", "employee_age", "40", "profile_image", ""),
                Map.of("id", "3", "employee_name", "John 15", "employee_salary", "15", "employee_age", "35", "profile_image", "")
        ));

        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), isNull(), any(ParameterizedTypeReference.class)))
                .thenReturn(new ResponseEntity<>(mockResponse, HttpStatus.OK));

        // Call method
        List<String> topEarningNames = employeeService.getTop10HighestEarningEmployeeNames();

        // Assertions
        assertNotNull(topEarningNames);
        assertEquals(10, topEarningNames.size());
        assertEquals("John 15", topEarningNames.get(0));  // The highest employee_salary should be the first
        assertEquals("John 12", topEarningNames.get(3));  // The next highest
    }

    @Test
    void testDeleteEmployeeById() {
        // Mock the response
        Map<String, Object> mockResponse = new HashMap<>();
        mockResponse.put("status", "success");
        mockResponse.put("message", "Successfully deleted employee");

        when(restTemplate.exchange(anyString(), eq(HttpMethod.DELETE), isNull(), any(ParameterizedTypeReference.class)))
                .thenReturn(new ResponseEntity<>(mockResponse, HttpStatus.OK));

        // Call methof for id 1
        String result = employeeService.deleteEmployeeById("1");

        // Assertions
        assertEquals("Successfully deleted employee", result);
    }

}