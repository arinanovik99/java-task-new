package com.asti.employee_service.record;

public record CreateUpdateEmployeeRequest(
        String name,
        String departmentId
) {}
