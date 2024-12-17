package com.asti.department_service.client;

import com.asti.department_service.record.Employee;
import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "employee-service", path = "/employees")
public interface EmployeeServiceClient {

  @GetMapping("/department/{departmentId}")
  List<Employee> getEmployeesByDepartment(@PathVariable("departmentId") String departmentId);
}
