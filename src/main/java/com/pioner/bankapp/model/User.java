package com.pioner.bankapp.model;

import jakarta.persistence.Cacheable;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.xml.bind.ValidationException;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Table(name = "users")
@Getter @Setter
public class User {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, length = 500)
  private String name;

  @Column(name = "date_of_birth")
  private LocalDate dateOfBirth;

  @Column(nullable = false, length = 500)
  private String password;

  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
  private Set<EmailData> emails = new HashSet<>();

  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
  private Set<PhoneData> phones = new HashSet<>();

  @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
  private Account account;

  @PrePersist
  private void validate() throws ValidationException {
    if (emails.isEmpty() || phones.isEmpty()) {
      throw new ValidationException("User must have at least one email and phone");
    }
  }
}
