package com.example.marketplace.service;

import java.util.List;
import org.springframework.stereotype.Service;
import com.example.marketplace.entity.User;
import com.example.marketplace.repository.UserRepository;
import com.example.marketplace.util.ValidationUtil;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final ValidationUtil validationUtil;
    private final EmailService emailService;

    public UserService(UserRepository userRepository, ValidationUtil validationUtil, EmailService emailService) {
        this.userRepository = userRepository;
        this.validationUtil = validationUtil;
        this.emailService = emailService;
    }

    public User createUser(User user) {
        validationUtil.validate(user);
        if (userRepository.findByEmailIgnoreCase(user.getEmail()) != null) {
            validationUtil.fail("email", "is already registered");
        }
        if (userRepository.findByUsernameIgnoreCase(user.getUsername()) != null) {
            validationUtil.fail("username", "is already taken");
        }
        User savedUser = userRepository.save(user);
        emailService.sendWelcomeEmailAsync(savedUser);
        return savedUser;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(int id) {
        return userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
    }

    public User updateUser(int id, User userDetails) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        user.setUsername(userDetails.getUsername());
        user.setEmail(userDetails.getEmail());
        user.setPassword(userDetails.getPassword());
        user.setRole(userDetails.getRole());
        user.setPhone(userDetails.getPhone());
        user.setActive(userDetails.isActive());
        validationUtil.validate(user);
        return userRepository.save(user);
    }

    public void deleteUser(int id) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        userRepository.delete(user);
    }
}
