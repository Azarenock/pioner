package com.pioner.bankapp.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AuthRequest {
  @NotBlank(message = "Login cannot be blank")
  private String login;

  @NotBlank(message = "Password cannot be blank")
  @Size(min = 8, max = 500, message = "Password must be 8-500 characters long")
  private String password;
}