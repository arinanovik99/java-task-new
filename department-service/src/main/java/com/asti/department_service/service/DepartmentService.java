package com.asti.department_service.service;

import com.asti.department_service.record.DepartmentRequest;
import com.asti.department_service.record.DepartmentResponse;
import com.asti.department_service.record.DepartmentWithEmployeesResponse;

import java.util.List;

public interface DepartmentService {

  List<DepartmentResponse> findAllDepartments();

  DepartmentWithEmployeesResponse findDepartmentById(String id);

  DepartmentResponse createDepartment(DepartmentRequest departmentDto);

  DepartmentResponse updateDepartment(String id, DepartmentRequest departmentDto);
}
