package com.example.rqchallenge;

import com.example.rqchallenge.employees.model.Employee;
import com.example.rqchallenge.employees.service.EmployeeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@SpringBootTest
class EmployeeServiceIntegrationTest {

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private RestTemplate restTemplate;

    private MockRestServiceServer mockServer;
    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockServer = MockRestServiceServer.createServer(restTemplate);
    }

    @Test
    void testGetAllEmployees() throws Exception {
        Map<String, Object> mockResponse = new HashMap<>();
        mockResponse.put("status", "success");
        mockResponse.put("data", List.of(
                Map.of("id", "1", "employee_name", "Conor", "employee_salary", "1000000", "employee_age", "32", "profile_image", "pic2")
        ));

        // set up our mock server
        mockServer.expect(requestTo("https://dummy.restapiexample.com/api/v1/employees"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(objectMapper.writeValueAsString(mockResponse), MediaType.APPLICATION_JSON));

        // call method
        List<Employee> employees = employeeService.getAllEmployees();

        // assertions
        assertNotNull(employees);
        assertEquals(1, employees.size());
        assertEquals("Conor", employees.get(0).getEmployeeName());

    }

    @Test
    void testCreateEmployee() throws Exception {
        Map<String, Object> employeeInput = new HashMap<>();
        employeeInput.put("name", "Conor");
        employeeInput.put("salary", "5000");
        employeeInput.put("age", "30");

        Map<String, Object> mockResponse = new HashMap<>();
        mockResponse.put("status", "success");
        mockResponse.put("data", Map.of("id", "1", "employee_name", "Conor", "employee_salary", "1000000", "employee_age", "32", "profile_image", "pic2"));

        // set up mock server
        mockServer.expect(requestTo("https://dummy.restapiexample.com/api/v1/create"))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withSuccess(objectMapper.writeValueAsString(mockResponse), MediaType.APPLICATION_JSON));

        // call method
        Employee employee = employeeService.createEmployee(employeeInput);

        // assert
        assertNotNull(employee);
        assertEquals("Conor", employee.getEmployeeName());

    }
}
