package com.pioner.bankapp.controller;

import com.pioner.bankapp.dto.TransferRequest;
import com.pioner.bankapp.exception.InsufficientFundsException;
import com.pioner.bankapp.exception.NotFoundException;
import com.pioner.bankapp.repository.AccountRepository;
import com.pioner.bankapp.security.JwtTokenProvider;
import com.pioner.bankapp.service.TransferService;
import jakarta.xml.bind.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/transfer")
@RequiredArgsConstructor
public class TransferController {
  private final TransferService transferService;
  private final JwtTokenProvider jwtTokenProvider;
  private final AccountRepository accountRepository; // Добавлено

  @PostMapping
  public ResponseEntity<?> transfer(
      @RequestHeader("Authorization") String token,
      @RequestBody TransferRequest request) {

    try {
      String jwt = token.startsWith("Bearer ") ? token.substring(7) : token;
      Long userId = jwtTokenProvider.getUserIdFromToken(jwt);

      if (!accountRepository.existsByIdAndUserId(request.getFromAccountId(), userId)) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
      }

      transferService.transferMoney(
          request.getFromAccountId(),
          request.getToAccountId(),
          request.getAmount()
      );

      return ResponseEntity.ok().build();

    } catch (NotFoundException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    } catch (ValidationException | InsufficientFundsException e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    } catch (Exception e) {
      return ResponseEntity.internalServerError().body("Transfer failed");
    }
  }
}