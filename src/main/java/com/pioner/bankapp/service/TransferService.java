package com.pioner.bankapp.service;

import com.pioner.bankapp.exception.InsufficientFundsException;
import com.pioner.bankapp.exception.NotFoundException;
import com.pioner.bankapp.model.Account;
import com.pioner.bankapp.repository.AccountRepository;
import jakarta.xml.bind.ValidationException;
import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(isolation = Isolation.SERIALIZABLE)
public class TransferService {
  private final AccountRepository accountRepository;

  @Cacheable(value = "accountBalance", key = "#accountId")
  public BigDecimal getAccountBalance(Long accountId) {
    return accountRepository.findById(accountId)
        .orElseThrow(() -> new NotFoundException("Account not found"))
        .getBalance();
  }

  @CacheEvict(value = "accountBalance", key = "{#fromAccountId, #toAccountId}")
  public void transferMoney(Long fromAccountId, Long toAccountId, BigDecimal amount)
      throws ValidationException {
    if (amount.compareTo(BigDecimal.ZERO) <= 0) {
      throw new ValidationException("Amount must be positive");
    }

    if (fromAccountId.equals(toAccountId)) {
      throw new ValidationException("Cannot transfer to the same account");
    }

    Account fromAccount = accountRepository.findByIdWithLock(fromAccountId)
        .orElseThrow(() -> new NotFoundException("Sender account not found"));

    Account toAccount = accountRepository.findByIdWithLock(toAccountId)
        .orElseThrow(() -> new NotFoundException("Recipient account not found"));

    if (fromAccount.getBalance().compareTo(amount) < 0) {
      throw new InsufficientFundsException("Insufficient funds");
    }

    fromAccount.setBalance(fromAccount.getBalance().subtract(amount));
    toAccount.setBalance(toAccount.getBalance().add(amount));

    log.info("Transfer completed: {} from account {} to account {}",
        amount, fromAccountId, toAccountId);
  }
}