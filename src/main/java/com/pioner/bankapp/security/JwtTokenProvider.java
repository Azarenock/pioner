package com.pioner.bankapp.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;

@Slf4j
@Component
public class JwtTokenProvider {
  private final SecretKey secretKey;
  private final long jwtExpirationMs;

  public JwtTokenProvider(
      @Value("${jwt.secret}") String base64Secret,
      @Value("${jwt.expiration}") long jwtExpirationMs) {

    try {
      String cleanedSecret = base64Secret.replaceAll("[^A-Za-z0-9+/=]", "");
      byte[] keyBytes = Base64.getDecoder().decode(cleanedSecret);

      if (keyBytes.length < 64) {
        throw new IllegalArgumentException("Key must be at least 512 bits (64 bytes)");
      }

      this.secretKey = Keys.hmacShaKeyFor(keyBytes);
      this.jwtExpirationMs = jwtExpirationMs;

      log.info("JWT secret initialized successfully. Key length: {} bits", keyBytes.length * 8);
    } catch (IllegalArgumentException e) {
      throw new IllegalArgumentException("Failed to initialize JWT secret: " + e.getMessage(), e);
    }
  }

  public String resolveToken(HttpServletRequest request) {
    String bearerToken = request.getHeader("Authorization");
    if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
      return bearerToken.substring(7);
    }
    return null;
  }

  public boolean validateToken(String token) {
    try {
      Jwts.parser()
          .verifyWith(secretKey)
          .build()
          .parseSignedClaims(token);
      return true;
    } catch (JwtException | IllegalArgumentException e) {
      log.error("Invalid JWT token: {}", e.getMessage());
      return false;
    }
  }

  public Authentication getAuthentication(String token) {
    Long userId = getUserIdFromToken(token);
    UserPrincipal principal = new UserPrincipal(userId);
    return new UsernamePasswordAuthenticationToken(principal, "", principal.getAuthorities());
  }

  public String generateToken(Long userId) {
    return Jwts.builder()
        .subject(userId.toString())
        .issuedAt(new Date())
        .expiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
        .signWith(secretKey, Jwts.SIG.HS512)
        .compact();
  }

  public Long getUserIdFromToken(String token) {
    return Long.parseLong(
        Jwts.parser()
            .verifyWith(secretKey)
            .build()
            .parseSignedClaims(token)
            .getPayload()
            .getSubject()
    );
  }
}