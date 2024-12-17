package com.asti.employee_service.service;

import com.asti.employee_service.record.CreateUpdateEmployeeRequest;
import com.asti.employee_service.record.EmployeeResponse;
import org.springframework.data.domain.PageImpl;

import java.util.List;

public interface EmployeeService {

    PageImpl<EmployeeResponse> findAllEmployees(int pageNumber, int pageSize, String searchString);

    EmployeeResponse updateEmployee(Long id, CreateUpdateEmployeeRequest request);

    EmployeeResponse getEmployeeById(Long id);

    List<EmployeeResponse> getEmployeesByDepartment(String departmentId);

    void deleteEmployee(Long id);
}
