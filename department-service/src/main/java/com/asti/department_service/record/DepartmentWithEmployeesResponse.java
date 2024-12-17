package com.asti.department_service.record;

import java.util.List;

public record DepartmentWithEmployeesResponse(
    String id, String name, List<Employee> employees) {}
