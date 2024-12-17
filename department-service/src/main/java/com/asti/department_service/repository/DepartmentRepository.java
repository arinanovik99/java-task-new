package com.asti.department_service.repository;

import com.asti.department_service.model.Department;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DepartmentRepository extends MongoRepository<Department, String> {}
