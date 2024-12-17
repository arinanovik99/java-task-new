package com.asti.department_service.datamigration;

import com.asti.department_service.model.Department;
import jakarta.annotation.PostConstruct;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DataMigrationService {

  private final MongoTemplate mongoTemplate;

  @PostConstruct
  public void migrateData() {
    final var department1 = new Department("HR");
    final var department2 = new Department("Marketing");
    final var department3 = new Department("IT");
    final var department4 = new Department("Security");

    mongoTemplate.dropCollection("departments");
    mongoTemplate.insertAll(List.of(department1, department2, department3, department4));
  }
}
