package com.asti.employee_service.service.impl;

import com.asti.employee_service.model.Employee;
import com.asti.employee_service.record.CreateUpdateEmployeeRequest;
import com.asti.employee_service.record.EmployeeResponse;
import com.asti.employee_service.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceImplTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    private Employee employee;
    private EmployeeResponse employeeResponse;
    private CreateUpdateEmployeeRequest updateRequest;

    @BeforeEach
    void setUp() {
        employee = new Employee();
        employee.setId(1L);
        employee.setName("John Doe");
        employee.setDepartmentId("IT");

        employeeResponse = new EmployeeResponse(1L, "John Doe", "IT");

        updateRequest = new CreateUpdateEmployeeRequest("Jane Doe", "HR");
    }

    @Test
    void testFindAllEmployees() {
        PageRequest pageable = PageRequest.of(0, 5);
        List<Employee> employees = List.of(employee);
        Page<Employee> page = new PageImpl<>(employees, pageable, employees.size());

        when(employeeRepository.findAll(any(Specification.class), eq(pageable))).thenReturn(page);

        var result = employeeService.findAllEmployees(0, 5, "John");

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        verify(employeeRepository, times(1)).findAll(any(Specification.class), eq(pageable));
    }

    @Test
    void testUpdateEmployee() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
        when(employeeRepository.save(any(Employee.class))).thenReturn(employee);

        var result = employeeService.updateEmployee(1L, updateRequest);

        assertNotNull(result);
        assertEquals("Jane Doe", employee.getName());
        assertEquals("HR", employee.getDepartmentId());
        verify(employeeRepository, times(1)).findById(1L);
        verify(employeeRepository, times(1)).save(employee);
    }

    @Test
    void testUpdateEmployee_NotFound() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            employeeService.updateEmployee(1L, updateRequest);
        });

        assertEquals("Employee not found", exception.getMessage());
        verify(employeeRepository, times(1)).findById(1L);
        verify(employeeRepository, never()).save(any(Employee.class));
    }

    @Test
    void testGetEmployeeById() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));

        var result = employeeService.getEmployeeById(1L);

        assertNotNull(result);
        assertEquals(employeeResponse.id(), result.id());
        verify(employeeRepository, times(1)).findById(1L);
    }

    @Test
    void testGetEmployeeById_NotFound() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            employeeService.getEmployeeById(1L);
        });

        assertEquals("Employee not found", exception.getMessage());
        verify(employeeRepository, times(1)).findById(1L);
    }

    @Test
    void testGetEmployeesByDepartment() {
        when(employeeRepository.findByDepartmentId("IT")).thenReturn(List.of(employee));

        var result = employeeService.getEmployeesByDepartment("IT");

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(employeeRepository, times(1)).findByDepartmentId("IT");
    }

    @Test
    void testDeleteEmployee() {
        doNothing().when(employeeRepository).deleteById(1L);

        assertDoesNotThrow(() -> employeeService.deleteEmployee(1L));
        verify(employeeRepository, times(1)).deleteById(1L);
    }
}
