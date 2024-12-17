package com.asti.api_gateway.config;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerAggregatorConfig {

  @Bean
  public GroupedOpenApi authServiceApi() {
    return GroupedOpenApi.builder()
        .group("auth-service")
        .pathsToMatch("/auth/**")
        .addOpenApiCustomizer(
            openApi ->
                openApi.info(
                    new io.swagger.v3.oas.models.info.Info()
                        .title("Auth Service API")
                        .description("API authorization service")
                        .version("1.0")))
        .build();
  }

  @Bean
  public GroupedOpenApi employeeServiceApi() {
    return GroupedOpenApi.builder()
        .group("employee-service")
        .pathsToMatch("/employees/**")
        .addOpenApiCustomizer(
            openApi ->
                openApi.info(
                    new io.swagger.v3.oas.models.info.Info()
                        .title("Employee Service API")
                        .description("API employees")
                        .version("1.0")))
        .build();
  }

  @Bean
  public GroupedOpenApi departmentServiceApi() {
    return GroupedOpenApi.builder()
        .group("department-service")
        .pathsToMatch("/departments/**")
        .addOpenApiCustomizer(
            openApi ->
                openApi.info(
                    new io.swagger.v3.oas.models.info.Info()
                        .title("Department Service API")
                        .description("API departments")
                        .version("1.0")))
        .build();
  }
}
