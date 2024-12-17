package com.asti.department_service.service.impl;

import com.asti.department_service.client.EmployeeServiceClient;
import com.asti.department_service.model.Department;
import com.asti.department_service.record.DepartmentRequest;
import com.asti.department_service.record.DepartmentResponse;
import com.asti.department_service.record.DepartmentWithEmployeesResponse;
import com.asti.department_service.record.Employee;
import com.asti.department_service.repository.DepartmentRepository;
import com.asti.department_service.service.DepartmentService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DepartmentServiceImpl implements DepartmentService {

  private final DepartmentRepository departmentRepository;

  private final EmployeeServiceClient employeeServiceClient;

  @Override
  public List<DepartmentResponse> findAllDepartments() {
    return departmentRepository.findAll().stream().map(this::mapToResponse).toList();
  }

  @Override
  public DepartmentWithEmployeesResponse findDepartmentById(final String id) {
    return departmentRepository
        .findById(id)
        .map(this::mapToResponse)
        .map(
            departmentResponse -> {
              List<Employee> employees = employeeServiceClient.getEmployeesByDepartment(id);
              return new DepartmentWithEmployeesResponse(
                  departmentResponse.id(), departmentResponse.name(), employees);
            })
        .orElseThrow(() -> new RuntimeException("Department not found with id: " + id));
  }

  @Override
  public DepartmentResponse createDepartment(DepartmentRequest request) {
    final var departmentToSave = new Department(request.name());
    return mapToResponse(departmentRepository.save(departmentToSave));
  }

  @Override
  public DepartmentResponse updateDepartment(String id, DepartmentRequest request) {
    final var department = departmentRepository.findById(id).get();
    department.setName(request.name());
    return mapToResponse(departmentRepository.save(department));
  }

  private DepartmentResponse mapToResponse(Department department) {
    return new DepartmentResponse(department.getId(), department.getName());
  }
}
