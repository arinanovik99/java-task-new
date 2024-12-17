package com.asti.api_gateway.config;

import java.nio.charset.StandardCharsets;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import reactor.core.publisher.Mono;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

  @Value("${jwt.secret}")
  private String jwtSecret;

  @Bean
  public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
    http.csrf()
        .disable()
        .authorizeExchange(
            authorizeExchangeSpec ->
                authorizeExchangeSpec
                    .pathMatchers("/auth/**")
                    .permitAll()
                    .pathMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html")
                    .permitAll()
                    .pathMatchers(HttpMethod.POST, "/employees/**")
                    .hasRole("ROLE_HR")
                    .pathMatchers(HttpMethod.PUT, "/employees/**")
                    .hasRole("ROLE_HR")
                    .pathMatchers(HttpMethod.DELETE, "/employees/**")
                    .hasRole("ROLE_HR")
                    .pathMatchers(HttpMethod.GET, "/departments/{id}")
                    .hasRole("ROLE_HR")
                    .pathMatchers(HttpMethod.POST, "/departments")
                    .hasRole("ROLE_HR")
                    .pathMatchers(HttpMethod.PUT, "/departments/{id}")
                    .hasRole("ROLE_HR")
                    .anyExchange()
                    .authenticated())
        .oauth2ResourceServer(
            oauth2 ->
                oauth2.jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter())));
    return http.build();
  }

  @Bean
  public ReactiveJwtDecoder reactiveJwtDecoder() {
    byte[] secretBytes = jwtSecret.getBytes(StandardCharsets.UTF_8);
    SecretKey secretKey = new SecretKeySpec(secretBytes, "HmacSHA256");
    return NimbusReactiveJwtDecoder.withSecretKey(secretKey).build();
  }

  @Bean
  public Converter<Jwt, Mono<AbstractAuthenticationToken>> jwtAuthenticationConverter() {
    return new JwtAuthenticationConverter();
  }
}
