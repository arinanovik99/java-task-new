package com.asti.department_service.client;

import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

@Configuration
public class FeignClientConfig {

  @Bean
  public RequestInterceptor requestInterceptor() {
    return template -> {
      Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
      if (authentication instanceof JwtAuthenticationToken jwtAuthenticationToken) {
        Jwt jwt = (Jwt) jwtAuthenticationToken.getCredentials();
        String tokenValue = jwt.getTokenValue();
        template.header("Authorization", "Bearer " + tokenValue);
      }
    };
  }
}
