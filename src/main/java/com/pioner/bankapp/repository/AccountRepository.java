package com.pioner.bankapp.repository;

import com.pioner.bankapp.model.Account;
import jakarta.persistence.LockModeType;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
  @Lock(LockModeType.PESSIMISTIC_WRITE)
  @Query("SELECT a FROM Account a WHERE a.id = :id")
  Optional<Account> findByIdWithLock(@Param("id") Long id);

  boolean existsByIdAndUserId(Long accountId, Long userId);
}