package com.pioner.bankapp.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Table(name = "accounts")
@Getter @Setter
public class Account {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @OneToOne
  @JoinColumn(name = "user_id", nullable = false, unique = true)
  private User user;

  @Column(nullable = false, precision = 19, scale = 2)
  private BigDecimal balance;

  @Column(name = "initial_deposit", nullable = false, precision = 19, scale = 2)
  private BigDecimal initialDeposit;
}