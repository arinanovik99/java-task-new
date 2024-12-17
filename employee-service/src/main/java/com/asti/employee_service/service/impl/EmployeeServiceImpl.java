package com.asti.employee_service.service.impl;

import com.asti.employee_service.model.Employee;
import com.asti.employee_service.record.CreateUpdateEmployeeRequest;
import com.asti.employee_service.record.EmployeeResponse;
import com.asti.employee_service.repository.EmployeeRepository;
import com.asti.employee_service.service.EmployeeService;
import com.asti.employee_service.specification.EmployeeSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;

    @Override
    public PageImpl<EmployeeResponse> findAllEmployees(int pageNumber, int pageSize, String searchString) {
        final var pageable = PageRequest.of(pageNumber, pageSize);

        final var spec = Specification.where(EmployeeSpecification.hasName(searchString));

        final var page = employeeRepository.findAll(spec, pageable);
        final var content = page.getContent().stream()
                .map(this::mapToResponse)
                .toList();
        return new PageImpl<>(content, pageable, page.getTotalElements());
    }

    @Override
    public EmployeeResponse updateEmployee(Long id, CreateUpdateEmployeeRequest request) {
        Employee existing = employeeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Employee not found"));
        existing.setName(request.name());
        existing.setDepartmentId(request.departmentId());
        Employee updatedEmployee = employeeRepository.save(existing);
        return mapToResponse(updatedEmployee);
    }

    @Override
    public EmployeeResponse getEmployeeById(Long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Employee not found"));
        return mapToResponse(employee);
    }

    @Override
    public List<EmployeeResponse> getEmployeesByDepartment(String departmentId) {
        return employeeRepository.findByDepartmentId(departmentId).stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public void deleteEmployee(Long id) {
        employeeRepository.deleteById(id);
    }

    private EmployeeResponse mapToResponse(Employee employee) {
        return new EmployeeResponse(
                employee.getId(),
                employee.getName(),
                employee.getDepartmentId()
        );
    }
}
