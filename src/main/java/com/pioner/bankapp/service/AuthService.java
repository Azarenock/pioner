package com.pioner.bankapp.service;

import com.pioner.bankapp.exception.NotFoundException;
import com.pioner.bankapp.model.User;
import com.pioner.bankapp.repository.UserRepository;
import com.pioner.bankapp.security.JwtTokenProvider;
import jakarta.xml.bind.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtTokenProvider jwtTokenProvider;

  @Cacheable(value = "authTokens", key = "#login")
  public String authenticate(String login, String password) throws ValidationException {
    log.debug("Attempting authentication for login: {}", login);

    User user = userRepository.findByEmail(login)
        .or(() -> userRepository.findByPhone(login))
        .orElseThrow(() -> {
          log.warn("User not found for login: {}", login);
          return new NotFoundException("Invalid login credentials");
        });

    if (!passwordEncoder.matches(password, user.getPassword())) {
      log.warn("Password mismatch for user: {}", login);
      throw new ValidationException("Invalid password");
    }

    return jwtTokenProvider.generateToken(user.getId());
  }

  @CacheEvict(value = "authTokens", key = "#login")
  public void logout(String login) {
    log.info("Invalidating token for user: {}", login);
  }
}