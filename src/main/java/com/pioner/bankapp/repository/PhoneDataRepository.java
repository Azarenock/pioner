package com.pioner.bankapp.repository;

import com.pioner.bankapp.model.PhoneData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PhoneDataRepository extends JpaRepository<PhoneData, Long> {
  boolean existsByPhone(String phone);
}
