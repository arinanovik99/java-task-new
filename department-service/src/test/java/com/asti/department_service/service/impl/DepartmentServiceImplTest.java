package com.asti.department_service.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.asti.department_service.client.EmployeeServiceClient;
import com.asti.department_service.model.Department;
import com.asti.department_service.record.DepartmentRequest;
import com.asti.department_service.record.DepartmentResponse;
import com.asti.department_service.record.Employee;
import com.asti.department_service.repository.DepartmentRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DepartmentServiceImplTest {

    @Mock
    private DepartmentRepository departmentRepository;

    @Mock
    private EmployeeServiceClient employeeServiceClient;

    @InjectMocks
    private DepartmentServiceImpl departmentService;

    private Department department;
    private DepartmentRequest departmentRequest;
    private List<Employee> employees;

    @BeforeEach
    void setUp() {
        department = new Department("1", "HR");
        departmentRequest = new DepartmentRequest("Finance");
        employees = List.of(new Employee("1", "John Doe", "1"));
    }

    @Test
    void testFindAllDepartments() {
        when(departmentRepository.findAll()).thenReturn(List.of(department));

        var result = departmentService.findAllDepartments();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("HR", result.get(0).name());
        verify(departmentRepository, times(1)).findAll();
    }

    @Test
    void testFindDepartmentById() {
        when(departmentRepository.findById("1")).thenReturn(Optional.of(department));
        when(employeeServiceClient.getEmployeesByDepartment("1")).thenReturn(employees);

        var result = departmentService.findDepartmentById("1");

        assertNotNull(result);
        assertEquals("HR", result.name());
        assertEquals(1, result.employees().size());
        verify(departmentRepository, times(1)).findById("1");
        verify(employeeServiceClient, times(1)).getEmployeesByDepartment("1");
    }

    @Test
    void testFindDepartmentById_NotFound() {
        when(departmentRepository.findById("1")).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            departmentService.findDepartmentById("1");
        });

        assertEquals("Department not found with id: 1", exception.getMessage());
        verify(departmentRepository, times(1)).findById("1");
        verifyNoInteractions(employeeServiceClient);
    }

    @Test
    void testCreateDepartment() {
        when(departmentRepository.save(any(Department.class))).thenReturn(department);

        var result = departmentService.createDepartment(departmentRequest);

        assertNotNull(result);
        assertEquals("HR", result.name());
        verify(departmentRepository, times(1)).save(any(Department.class));
    }

    @Test
    void testUpdateDepartment() {
        when(departmentRepository.findById("1")).thenReturn(Optional.of(department));
        when(departmentRepository.save(any(Department.class))).thenReturn(department);

        var result = departmentService.updateDepartment("1", departmentRequest);

        assertNotNull(result);
        assertEquals("Finance", result.name());
        verify(departmentRepository, times(1)).findById("1");
        verify(departmentRepository, times(1)).save(department);
    }
}

