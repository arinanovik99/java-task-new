package com.asti.auth_service.config;

import com.asti.auth_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

  private final UserRepository userRepository;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    final var user = userRepository.findByEmail(username);
    if (user.isEmpty()) {
      throw new IllegalArgumentException("Invalid email or password");
    }
    return new CustomUserDetails(user.get());
  }
}
