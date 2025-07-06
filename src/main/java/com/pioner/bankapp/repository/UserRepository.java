package com.pioner.bankapp.repository;

import com.pioner.bankapp.model.User;
import java.time.LocalDate;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
  @Query("SELECT u FROM User u WHERE EXISTS (SELECT 1 FROM u.emails e WHERE e.email = :login) OR " +
      "EXISTS (SELECT 1 FROM u.phones p WHERE p.phone = :login)")
  Optional<User> findByEmailOrPhone(@Param("login") String login);

  @Query("SELECT u FROM User u JOIN u.emails e WHERE e.email = :login")
  Optional<User> findByEmail(@Param("login") String login);

  @Query("SELECT u FROM User u JOIN u.phones p WHERE p.phone = :login")
  Optional<User> findByPhone(@Param("login") String login);

  @Query("SELECT DISTINCT u FROM User u " +
      "WHERE (:name IS NULL OR u.name LIKE :name%) " +
      "AND (:email IS NULL OR EXISTS (SELECT 1 FROM u.emails e WHERE e.email = :email)) " +
      "AND (:phone IS NULL OR EXISTS (SELECT 1 FROM u.phones p WHERE p.phone = :phone)) " +
      "AND (:dateOfBirth IS NULL OR u.dateOfBirth > :dateOfBirth)")
  Page<User> searchUsers(
      @Param("name") String name,
      @Param("email") String email,
      @Param("phone") String phone,
      @Param("dateOfBirth") LocalDate dateOfBirth,
      Pageable pageable
  );
}