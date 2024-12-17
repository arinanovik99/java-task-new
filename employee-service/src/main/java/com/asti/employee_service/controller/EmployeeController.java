package com.asti.employee_service.controller;

import com.asti.employee_service.record.CreateUpdateEmployeeRequest;
import com.asti.employee_service.record.EmployeeResponse;
import com.asti.employee_service.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    @GetMapping
    public PageImpl<EmployeeResponse> getAllEmployees(@RequestParam(name = "pageNumber", required = false, defaultValue = "0") final int pageNumber,
                                                      @RequestParam(name = "pageSize", required = false, defaultValue = "10") final int pageSize,
                                                      @RequestParam(name = "searchString", required = false, defaultValue = "") final String searchString) {
        return employeeService.findAllEmployees(pageNumber, pageSize, searchString);
    }

    @GetMapping("/{id}")
    public EmployeeResponse getEmployeeById(@PathVariable Long id) {
        return employeeService.getEmployeeById(id);
    }

    @PutMapping("/{id}")
    public EmployeeResponse updateEmployee(@PathVariable Long id, @RequestBody CreateUpdateEmployeeRequest request) {
        return employeeService.updateEmployee(id, request);
    }

    @GetMapping("/department/{departmentId}")
    public List<EmployeeResponse> getEmployeesByDepartment(@PathVariable String departmentId) {
        return employeeService.getEmployeesByDepartment(departmentId);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteEmployee(@PathVariable Long id) {
        employeeService.deleteEmployee(id);
    }
}
