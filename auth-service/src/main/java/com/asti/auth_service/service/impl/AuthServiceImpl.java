package com.asti.auth_service.service.impl;

import com.asti.auth_service.dto.LoginRequest;
import com.asti.auth_service.dto.LoginResponse;
import com.asti.auth_service.dto.RegisterRequest;
import com.asti.auth_service.model.UserEntity;
import com.asti.auth_service.repository.UserRepository;
import com.asti.auth_service.service.AuthService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.stream.Collectors;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final AuthenticationManager authenticationManager;

  @Value("${jwt.secret}")
  private String jwtSecret;

  @Override
  public void register(RegisterRequest request) {
    if (userRepository.findByEmail(request.email()).isPresent()) {
      throw new IllegalArgumentException("Email is already taken");
    }

    UserEntity user = new UserEntity();
    user.setEmail(request.email());
    user.setPassword(passwordEncoder.encode(request.password()));
    user.setName(request.name());
    user.setRole(request.role());
    userRepository.save(user);
  }

  @Override
  public ResponseEntity<LoginResponse> login(LoginRequest request) {

    try {
      Authentication authentication =
          authenticationManager.authenticate(
              new UsernamePasswordAuthenticationToken(request.email(), request.password()));
      UserDetails userDetails = (UserDetails) authentication.getPrincipal();

      final var token =
          Jwts.builder()
              .setSubject(userDetails.getUsername())
              .claim(
                  "roles",
                  userDetails.getAuthorities().stream()
                      .map(GrantedAuthority::getAuthority)
                      .collect(Collectors.toList()))
              .setIssuedAt(new Date())
              .setExpiration(new Date(System.currentTimeMillis() + 864_000_00))
              .signWith(getSigningKey(), SignatureAlgorithm.HS256)
              .compact();

      return ResponseEntity.ok(new LoginResponse(token));
    } catch (AuthenticationException e) {

      return ResponseEntity.status(401).body(new LoginResponse("Invalid credentials"));
    }
  }

  private SecretKey getSigningKey() {
    return new SecretKeySpec(
        jwtSecret.getBytes(StandardCharsets.UTF_8), SignatureAlgorithm.HS256.getJcaName());
  }
}
