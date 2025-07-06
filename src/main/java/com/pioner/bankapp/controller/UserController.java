package com.pioner.bankapp.controller;

import com.pioner.bankapp.dto.UserResponse;
import com.pioner.bankapp.service.UserService;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
  private final UserService userService;

  @GetMapping
  public ResponseEntity<Page<UserResponse>> searchUsers(
      @RequestParam(required = false) String name,
      @RequestParam(required = false) String email,
      @RequestParam(required = false) String phone,
      @RequestParam(required = false) @DateTimeFormat(pattern = "dd.MM.yyyy") LocalDate dateOfBirth,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size) {

    return ResponseEntity.ok(
        userService.searchUsers(name, email, phone, dateOfBirth, PageRequest.of(page, size))
    );
  }
}