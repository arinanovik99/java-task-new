package com.asti.api_gateway.config;

import java.util.Collection;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import reactor.core.publisher.Mono;

public class JwtAuthenticationConverter
    implements Converter<Jwt, Mono<AbstractAuthenticationToken>> {

  @Override
  public Mono<AbstractAuthenticationToken> convert(Jwt jwt) {

    Collection<GrantedAuthority> authorities = new JwtRoleConverter().convert(jwt);

    AbstractAuthenticationToken authenticationToken =
        new UsernamePasswordAuthenticationToken(jwt.getSubject(), null, authorities);

    return Mono.just(authenticationToken);
  }
}
