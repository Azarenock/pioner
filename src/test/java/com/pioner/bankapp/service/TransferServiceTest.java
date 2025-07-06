package com.pioner.bankapp.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import com.pioner.bankapp.exception.InsufficientFundsException;
import com.pioner.bankapp.model.Account;
import com.pioner.bankapp.repository.AccountRepository;
import jakarta.xml.bind.ValidationException;
import java.math.BigDecimal;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TransferServiceTest {

  @Mock
  private AccountRepository accountRepository;

  @InjectMocks
  private TransferService transferService;

  @Test
  void transferMoneyUpdatesBalancesCorrectly() throws ValidationException {
    Account fromAccount = new Account();
    fromAccount.setId(1L);
    fromAccount.setBalance(BigDecimal.valueOf(500));

    Account toAccount = new Account();
    toAccount.setId(2L);
    toAccount.setBalance(BigDecimal.valueOf(200));

    when(accountRepository.findByIdWithLock(1L)).thenReturn(Optional.of(fromAccount));
    when(accountRepository.findByIdWithLock(2L)).thenReturn(Optional.of(toAccount));

    transferService.transferMoney(1L, 2L, BigDecimal.valueOf(100));

    assertEquals(BigDecimal.valueOf(400), fromAccount.getBalance());
    assertEquals(BigDecimal.valueOf(300), toAccount.getBalance());
  }

  @Test
  void transferMoneyThrowsWhenNegativeAmount() {
    assertThrows(ValidationException.class, () ->
        transferService.transferMoney(1L, 2L, BigDecimal.valueOf(-100)));
  }

  @Test
  void getAccountBalanceReturnsCorrectValue() {
    Account account = new Account();
    account.setBalance(BigDecimal.valueOf(1000));

    when(accountRepository.findById(1L)).thenReturn(Optional.of(account));

    BigDecimal balance = transferService.getAccountBalance(1L);

    assertEquals(BigDecimal.valueOf(1000), balance);
  }
}
