package com.springboot.backend.service;

import com.springboot.backend.model.Employee;
import com.springboot.backend.repository.EmployeeRepository;
import com.springboot.backend.service.impl.EmployeeServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceTest {

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    @Mock
    private EmployeeRepository employeeRepository;

    private Employee mogauEmployee;
    private Employee godfreyEmployee;

    @BeforeEach
    void init(){
        mogauEmployee = new Employee();
        mogauEmployee.setId(1L);
        mogauEmployee.setFirstName("Mogau");
        mogauEmployee.setLastName("Ngwatle");
        mogauEmployee.setEmail("mogau@gmail.com");

        godfreyEmployee = new Employee();
        godfreyEmployee.setId(2L);
        godfreyEmployee.setFirstName("Godfrey");
        godfreyEmployee.setLastName("Mohlala");
        godfreyEmployee.setEmail("godfrey@gmail.com");
    }

    @Test
    @DisplayName("Should save the employee object to database")
    void save(){

        when(employeeRepository.save(mogauEmployee)).thenReturn(mogauEmployee);

        Employee newEmployee = employeeService.saveEmployee(mogauEmployee);

        assertNotNull(newEmployee);
        assertThat(newEmployee.getFirstName()).isEqualTo("Mogau");
    }

    @Test
    @DisplayName("Should return list of employees of size 2")
    void getAllEmployees(){

        List<Employee> list = new ArrayList<>();
        list.add(mogauEmployee);
        list.add(godfreyEmployee);

        when(employeeRepository.findAll()).thenReturn(list);

        List<Employee> employees = employeeService.getALlEmployees();

        assertEquals(2, employees.size());
        assertNotNull(employees);
    }

    @Test
    @DisplayName("Should return the single employee object")
    void getMovieById(){

        when(employeeRepository.findById(anyLong())).thenReturn(Optional.ofNullable(mogauEmployee));

        Employee existingEmployee = employeeService.getEmployeeById(1L);

        assertNotNull(existingEmployee);
        assertThat(existingEmployee.getId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("Should throw the exception")
    void getMovieByIdForException(){

        when(employeeRepository.findById(1L)).thenReturn(Optional.ofNullable(mogauEmployee));

        assertThrows(RuntimeException.class, () -> {
            employeeService.getEmployeeById(2L);
        });
    }

    @Test
    @DisplayName("Should update the employee into the database")
    void updateEmployee(){

        when(employeeRepository.findById(anyLong())).thenReturn(Optional.ofNullable(mogauEmployee));

        when(employeeRepository.save(any(Employee.class))).thenReturn(mogauEmployee);
        mogauEmployee.setFirstName("Skomane");

        Employee existingEmployee = employeeService.updateEmployee(mogauEmployee, 1L);

        assertNotNull(existingEmployee);
        assertEquals("Skomane", mogauEmployee.getFirstName());
    }

    @Test
    @DisplayName("Should delete an existing employee")
    void deleteMovie(){

        Long employeeId = 1L;
        when(employeeRepository.findById(anyLong())).thenReturn(Optional.of(mogauEmployee));
        doNothing().when(employeeRepository).delete(any(Employee.class));

        employeeService.deleteEmployee(employeeId);

        verify(employeeRepository, times(1)).delete(mogauEmployee);
    }

}
