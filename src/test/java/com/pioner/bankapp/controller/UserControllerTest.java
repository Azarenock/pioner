package com.pioner.bankapp.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.pioner.bankapp.dto.UserResponse;
import com.pioner.bankapp.service.UserService;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

  @Mock
  private UserService userService;

  @InjectMocks
  private UserController userController;

  @Test
  void searchUsersReturnsOkResponseWithUsers() {
    UserResponse userResponse = UserResponse.builder()
        .id(1L)
        .name("John Doe")
        .dateOfBirth(LocalDate.of(1990, 1, 1))
        .emails(List.of("john@example.com"))
        .phones(List.of("1234567890"))
        .balance(BigDecimal.valueOf(1000))
        .build();

    Page<UserResponse> expectedPage = new PageImpl<>(List.of(userResponse));
    when(userService.searchUsers(any(), any(), any(), any(), any())).thenReturn(expectedPage);

    ResponseEntity<Page<UserResponse>> response = userController.searchUsers(
        "John", "john@example.com", "1234567890", LocalDate.of(1990, 1, 1), 0, 10);

    assertEquals(200, response.getStatusCodeValue());
    assertEquals(1, response.getBody().getTotalElements());
  }

  @Test
  void searchUsersWithNullParamsReturnsEmptyPage() {
    Page<UserResponse> expectedPage = new PageImpl<>(List.of());
    when(userService.searchUsers(any(), any(), any(), any(), any())).thenReturn(expectedPage);

    ResponseEntity<Page<UserResponse>> response = userController.searchUsers(
        null, null, null, null, 0, 10);

    assertEquals(200, response.getStatusCodeValue());
    assertTrue(response.getBody().isEmpty());
  }
}