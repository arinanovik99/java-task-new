package com.asti.api_gateway.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Mono;

@Slf4j
@Configuration
public class GatewayConfig {

  @Bean
  public GlobalFilter loggingFilter() {
    return (exchange, chain) -> {
      String requestPath = exchange.getRequest().getPath().toString();
      log.info(
          "Incoming Request: Method={}, Path={}", exchange.getRequest().getMethod(), requestPath);

      return chain
          .filter(exchange)
          .then(
              Mono.fromRunnable(
                  () -> {
                    int statusCode = exchange.getResponse().getStatusCode().value();
                    log.info("Response Status: Path={}, StatusCode={}", requestPath, statusCode);
                  }));
    };
  }

  @Bean
  public RouteLocator routes(RouteLocatorBuilder builder) {
    return builder
        .routes()
        .route(
            "auth-service",
            r ->
                r.path("/auth/**")
                    .filters(
                        f ->
                            f.filter(
                                (exchange, chain) -> {
                                  log.info(
                                      "Redirecting to AUTH-SERVICE: {}",
                                      exchange.getRequest().getPath());
                                  return chain.filter(exchange);
                                }))
                    .uri("lb://auth-service"))
        .route(
            "employee-service",
            r ->
                r.path("/employees/**")
                    .filters(
                        f ->
                            f.filter(
                                (exchange, chain) -> {
                                  log.info(
                                      "Redirecting to EMPLOYEE-SERVICE: {}",
                                      exchange.getRequest().getPath());
                                  return chain.filter(exchange);
                                }))
                    .uri("lb://employee-service"))
        .route(
            "department-service",
            r ->
                r.path("/departments/**")
                    .filters(
                        f ->
                            f.filter(
                                (exchange, chain) -> {
                                  log.info(
                                      "Redirecting to DEPARTMENT-SERVICE: {}",
                                      exchange.getRequest().getPath());
                                  return chain.filter(exchange);
                                }))
                    .uri("lb://department-service"))
        .build();
  }
}
