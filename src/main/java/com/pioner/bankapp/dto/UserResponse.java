package com.pioner.bankapp.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserResponse {
  private Long id;
  private String name;
  @JsonFormat(pattern = "yyyy-MM-dd")
  private LocalDate dateOfBirth;
  private List<String> emails;
  private List<String> phones;
  private BigDecimal balance;
}
