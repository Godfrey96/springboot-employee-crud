package com.springboot.backend.integration;

import com.springboot.backend.model.Employee;
import com.springboot.backend.repository.EmployeeRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EmployeeIntegrationTest {

    @LocalServerPort
    private int port;
    private String baseUrl = "http://localhost";
    private static RestTemplate restTemplate;

    @Autowired
    private EmployeeRepository employeeRepository;
    private Employee mogauEmployee;
    private Employee godfreyEmployee;

    @BeforeAll
    public static void init(){
        restTemplate = new RestTemplate();
    }

    @BeforeEach
    public void beforeSetup(){
        baseUrl = baseUrl + ":" + port + "/api/employees";

        mogauEmployee = new Employee();
        mogauEmployee.setFirstName("Mogau");
        mogauEmployee.setLastName("Ngwatle");
        mogauEmployee.setEmail("mogau@gmail.com");

        godfreyEmployee = new Employee();
        godfreyEmployee.setFirstName("Godfrey");
        godfreyEmployee.setLastName("Mohlala");
        godfreyEmployee.setEmail("godfrey@gmail.com");

        mogauEmployee = employeeRepository.save(mogauEmployee);
        godfreyEmployee = employeeRepository.save(godfreyEmployee);
    }

    @AfterEach
    public void afterSetup(){
        employeeRepository.deleteAll();;
    }

    @Test
    void shouldFetchEmployeesTest(){

        List<Employee> employees = restTemplate.getForObject(baseUrl, List.class);

        assertNotNull(employees);
        assertThat(employees.size()).isEqualTo(2);
    }

    @Test
    void shouldCreateEmployeeTest(){
        Employee mogauEmployee = new Employee();
        mogauEmployee.setFirstName("Mogau");
        mogauEmployee.setLastName("Ngwatle");
        mogauEmployee.setEmail("mogau@gmail.com");

        Employee newEmployee = restTemplate.postForObject(baseUrl, mogauEmployee, Employee.class);

        assertNotNull(newEmployee);
        assertThat(newEmployee.getId()).isNotNull();
    }

    @Test
    void shouldFetchSingleEmployee(){

        Employee existingEmployee = restTemplate.getForObject(baseUrl + "/" + mogauEmployee.getId(), Employee.class);

        assertNotNull(existingEmployee);
        assertEquals("Mogau", existingEmployee.getFirstName());
    }

    @Test
    void shouldUpdateEmployee(){

        mogauEmployee.setFirstName("Skomane");

        restTemplate.put(baseUrl + "/{id}", mogauEmployee,  mogauEmployee.getId());

        Employee existingEmployee = restTemplate.getForObject(baseUrl + "/" + mogauEmployee.getId(), Employee.class);

        assertNotNull(existingEmployee);
        assertEquals("Skomane", existingEmployee.getFirstName());
    }

    @Test
    void shouldDeleteExistingEmployee(){

        restTemplate.delete(baseUrl + "/" + mogauEmployee.getId());

        int count = employeeRepository.findAll().size();

        assertEquals(1, count);
    }

}
