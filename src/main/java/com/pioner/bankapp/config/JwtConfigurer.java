package com.pioner.bankapp.config;

import com.pioner.bankapp.security.JwtTokenFilter;
import com.pioner.bankapp.security.JwtTokenProvider;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class JwtConfigurer extends
    SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {
  private final JwtTokenProvider jwtTokenProvider;

  public JwtConfigurer(JwtTokenProvider jwtTokenProvider) {
    this.jwtTokenProvider = jwtTokenProvider;
  }

  @Override
  public void configure(HttpSecurity http) {
    JwtTokenFilter customFilter = new JwtTokenFilter(jwtTokenProvider);
    http.addFilterBefore(customFilter, UsernamePasswordAuthenticationFilter.class);
  }
}
