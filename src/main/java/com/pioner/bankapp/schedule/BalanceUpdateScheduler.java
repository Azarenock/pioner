package com.pioner.bankapp.schedule;

import com.pioner.bankapp.repository.AccountRepository;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BalanceUpdateScheduler {
  private final AccountRepository accountRepository;
  private static final BigDecimal MAX_INCREASE = new BigDecimal("2.07");
  private static final BigDecimal INCREASE_RATE = new BigDecimal("1.10");

  @Scheduled(fixedRate = 30000)
  @Transactional
  public void updateBalances() {
    accountRepository.findAll().forEach(account -> {
      BigDecimal newBalance = account.getBalance().multiply(INCREASE_RATE);
      BigDecimal maxAllowed = account.getInitialDeposit().multiply(MAX_INCREASE);

      account.setBalance(newBalance.min(maxAllowed));
    });
  }
}
