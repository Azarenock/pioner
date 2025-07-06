package com.pioner.bankapp.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import lombok.Data;

@Data
public class TransferRequest {

  @NotNull
  private Long fromAccountId;

  @NotNull
  private Long toAccountId;

  @NotNull
  @DecimalMin(value = "0.01", message = "Amount must be at least 0.01")
  private BigDecimal amount;
}
