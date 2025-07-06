package com.pioner.bankapp.repository;

import com.pioner.bankapp.model.EmailData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmailDataRepository extends JpaRepository<EmailData, Long> {
  boolean existsByEmail(String email);
}
