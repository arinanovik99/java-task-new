package com.asti.auth_service.service;

import com.asti.auth_service.dto.LoginRequest;
import com.asti.auth_service.dto.LoginResponse;
import com.asti.auth_service.dto.RegisterRequest;
import org.springframework.http.ResponseEntity;

public interface AuthService {
  void register(RegisterRequest request);

  ResponseEntity<LoginResponse> login(LoginRequest request);
}
