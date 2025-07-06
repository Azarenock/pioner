package com.pioner.bankapp.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Table(name = "phone_data")
@Getter @Setter
public class PhoneData {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @Column(nullable = false, unique = true, length = 13)
  @Pattern(regexp = "^7\\d{10}$", message = "Phone must be 11 digits starting with 7")
  private String phone;
}