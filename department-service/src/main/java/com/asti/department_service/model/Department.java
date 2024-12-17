package com.asti.department_service.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "departments")
@NoArgsConstructor
@AllArgsConstructor
public class Department {

  @Id private String id;

  @Indexed(unique = true)
  private String name;

  public Department(String name) {
    this.name = name;
  }
}
