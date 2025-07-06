package com.pioner.bankapp.service;

import com.pioner.bankapp.dto.UserResponse;
import com.pioner.bankapp.exception.NotFoundException;
import com.pioner.bankapp.model.EmailData;
import com.pioner.bankapp.model.PhoneData;
import com.pioner.bankapp.model.User;
import com.pioner.bankapp.repository.EmailDataRepository;
import com.pioner.bankapp.repository.PhoneDataRepository;
import com.pioner.bankapp.repository.UserRepository;
import jakarta.transaction.Transactional;
import jakarta.xml.bind.ValidationException;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
  private final UserRepository userRepository;
  private final EmailDataRepository emailDataRepository;
  private final PhoneDataRepository phoneDataRepository;


  public Page<UserResponse> searchUsers(String name, String email, String phone,
      LocalDate dateOfBirth, Pageable pageable) {
    return userRepository.searchUsers(name, email, phone, dateOfBirth, pageable)
        .map(this::convertToResponse);
  }

  @Transactional
  @CacheEvict(value = {"userData", "userSearch"}, allEntries = true)
  public void addEmail(Long userId, String email) throws ValidationException {
    if (emailDataRepository.existsByEmail(email)) {
      throw new ValidationException("Email already exists");
    }

    User user = userRepository.findById(userId)
        .orElseThrow(() -> new NotFoundException("User not found"));

    EmailData emailData = new EmailData();
    emailData.setEmail(email);
    emailData.setUser(user);
    user.getEmails().add(emailData);

    userRepository.save(user);
  }

  @Transactional
  @CacheEvict(value = {"userData", "userSearch"}, allEntries = true)
  public void addPhone(Long userId, String phone) throws ValidationException {
    if (phoneDataRepository.existsByPhone(phone)) {
      throw new ValidationException("Phone already exists");
    }

    User user = userRepository.findById(userId)
        .orElseThrow(() -> new NotFoundException("User not found"));

    PhoneData phoneData = new PhoneData();
    phoneData.setPhone(phone);
    phoneData.setUser(user);
    user.getPhones().add(phoneData);

    userRepository.save(user);
  }

  private UserResponse convertToResponse(User user) {
    return UserResponse.builder()
        .id(user.getId())
        .name(user.getName())
        .dateOfBirth(user.getDateOfBirth())
        .emails(user.getEmails().stream().map(EmailData::getEmail).toList())
        .phones(user.getPhones().stream().map(PhoneData::getPhone).toList())
        .balance(user.getAccount().getBalance())
        .build();
  }
}