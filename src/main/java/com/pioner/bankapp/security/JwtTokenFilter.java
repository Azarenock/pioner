package com.pioner.bankapp.security;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;


public class JwtTokenFilter extends OncePerRequestFilter {
  private final JwtTokenProvider jwtTokenProvider;

  public JwtTokenFilter(JwtTokenProvider jwtTokenProvider) {
    this.jwtTokenProvider = jwtTokenProvider;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request,
      HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {
    try {
      String token = jwtTokenProvider.resolveToken(request);
      if (token != null && jwtTokenProvider.validateToken(token)) {
        Authentication auth = jwtTokenProvider.getAuthentication(token);
        SecurityContextHolder.getContext().setAuthentication(auth);
      }
    } catch (JwtException e) {
      response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid JWT token");
      return;
    }

    filterChain.doFilter(request, response);
  }
}
