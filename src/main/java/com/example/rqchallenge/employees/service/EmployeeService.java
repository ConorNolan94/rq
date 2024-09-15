package com.example.rqchallenge.employees.service;

import com.example.rqchallenge.employees.model.Employee;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class EmployeeService {
    private static final String ORIGINAL_URL = "https://dummy.restapiexample.com/api/v1";
    private final RestTemplate restTemplate;
    private final ObjectMapper mapper;
    private static final String STATUS = "status";
    private static final String SUCCESS = "success";

    private static final Logger logger = LoggerFactory.getLogger(EmployeeService.class);

    public EmployeeService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        this.mapper = new ObjectMapper();
    }

    public List<Employee> getAllEmployees() {
        String employeeUrl = ORIGINAL_URL + "/employees";

        try {
            ResponseEntity<Map<String, Object>> response = restTemplate.exchange(employeeUrl, HttpMethod.GET, null, new ParameterizedTypeReference<>() {});

            Map<String, Object> responseMap = response.getBody();

            if (responseMap != null && SUCCESS.equalsIgnoreCase((String) responseMap.get(STATUS))) {
                List<Map<String, Object>> data = (List<Map<String, Object>>) responseMap.get("data");
                return data.stream()
                        .map(map -> mapper.convertValue(map, Employee.class))
                        .collect(Collectors.toList());
            }
        } catch (HttpClientErrorException.TooManyRequests e) {
            String retryAfter = Objects.requireNonNull(e.getResponseHeaders()).getFirst("Retry-After");
            logger.warn("Rate limit exceeded. Retry after: {}", retryAfter);
        }
        catch (Exception e) {
            logger.error("Failed to fetch employees", e);
        }
        return Collections.emptyList();
    }

    public Employee getEmployeeByID(String id) {
        String employeeUrl = ORIGINAL_URL + "/employee/" + id;

        try {
            ResponseEntity<Map<String, Object>> response = restTemplate.exchange(employeeUrl, HttpMethod.GET, null, new ParameterizedTypeReference<>() {});

            Map<String, Object> responseMap = response.getBody();
            if (responseMap != null && SUCCESS.equalsIgnoreCase((String) responseMap.get(STATUS))) {
                Map<String, Object> dataMap = (Map<String, Object>) responseMap.get("data");
                return mapper.convertValue(dataMap, Employee.class);
            }
        } catch (HttpClientErrorException.TooManyRequests e) {
            String retryAfter = Objects.requireNonNull(e.getResponseHeaders()).getFirst("Retry-After");
            logger.warn("Rate limit exceeded. Retry after: {}", retryAfter);
        } catch (HttpClientErrorException.NotFound e) {
            logger.warn("Employee with id {} not found", id);
        } catch (Exception e) {
            logger.error("Failed to fetch employee with id {}", id, e);
        }
        return null;
    }

    public List<Employee> getEmployeesByNameSearch(String name) {
        List<Employee> allEmployees = getAllEmployees();

        //We already have a method to getAllEmployees, so we just implement some logic on this call using Streams API

        return allEmployees.stream()
                .filter(employee -> employee.getEmployeeName().toLowerCase().contains(name.toLowerCase()))
                .collect(Collectors.toList());
    }

    public int getHighestSalaryOfEmployees() {
        List<Employee> allEmployees = getAllEmployees();

        //We already have a method to getAllEmployees, so we just implement some logic on this call using Streams API

        return allEmployees.stream()
                .mapToInt(employee -> Integer.parseInt(employee.getSalary()))
                .max()
                .orElse(0);
    }

    public List<String> getTop10HighestEarningEmployeeNames() {
        List<Employee> allEmployees = getAllEmployees();

        //We already have a method to getAllEmployees, so we just implement some logic on this call using Streams API

        return allEmployees.stream()
                .sorted(Comparator.comparingInt((Employee e) -> Integer.parseInt(e.getSalary())).reversed())
                .limit(10)
                .map(Employee::getEmployeeName)
                .collect(Collectors.toList());
    }

    public Employee createEmployee(Map<String, Object> employeeInput) {
        String employeeUrl = ORIGINAL_URL + "/create";

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(employeeInput, headers);

            ResponseEntity<Map<String, Object>> response = restTemplate.exchange(employeeUrl, HttpMethod.POST, requestEntity, new ParameterizedTypeReference<>() {});

            Map<String, Object> responseBody = response.getBody();

            if (response.getStatusCode().is2xxSuccessful() && responseBody != null && SUCCESS.equalsIgnoreCase((String) responseBody.get(STATUS))) {
                Map<String, Object> employeeData = (Map<String, Object>) responseBody.get("data");
                return mapper.convertValue(employeeData, Employee.class);
            }
        } catch (HttpClientErrorException.TooManyRequests e) {
            String length = Objects.requireNonNull(e.getResponseHeaders()).getFirst("Retry-After");
            logger.warn("Rate limit exceeded. Retry after: {}", length);
        } catch (RestClientException e) {
            logger.error("Failed to create employee", e);
        }

        return null;
    }

    public String deleteEmployeeById(String id) {
        String employeeUrl = ORIGINAL_URL + "/delete/" + id;

        try {
            ResponseEntity<Map<String, Object>> response = restTemplate.exchange(employeeUrl, HttpMethod.DELETE, null, new ParameterizedTypeReference<>() {});
            Map<String, Object> responseBody = response.getBody();

            if (response.getStatusCode().is2xxSuccessful() && responseBody != null && SUCCESS.equalsIgnoreCase((String) responseBody.get(STATUS))) {
                return (String) responseBody.get("message");
            }

            return "Failed to delete";
        } catch (HttpClientErrorException.TooManyRequests e) {
            String length = Objects.requireNonNull(e.getResponseHeaders()).getFirst("Retry-After");
            logger.warn("Rate limit has been hit, retry after: {}", length);
            return "Rate limit exdeeded.";
        } catch (HttpClientErrorException.NotFound e) {
            logger.warn("Employee with ID {} hasn't been found or doesn't exist.", id);
            return "Employee not found";
        } catch (RestClientException e) {
            logger.error("Failed to delete employee with ID {}", id, e);
            return "Failed to delete";
        }
    }

}
