package com.pioner.bankapp.controller;

import com.pioner.bankapp.dto.AuthRequest;
import com.pioner.bankapp.service.AuthService;
import jakarta.xml.bind.ValidationException;
import java.util.Collections;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
  private final AuthService authService;

  @PostMapping("/login")
  public ResponseEntity<Map<String, String>> login(@RequestBody AuthRequest request)
      throws ValidationException {
    String token = authService.authenticate(request.getLogin(), request.getPassword());
    return ResponseEntity.ok(Collections.singletonMap("token", token));
  }
}