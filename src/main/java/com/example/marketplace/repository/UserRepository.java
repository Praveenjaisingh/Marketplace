package com.example.marketplace.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.marketplace.entity.User;

public interface UserRepository extends JpaRepository<User, Integer> {

    User findByEmailIgnoreCase(String email);

    User findByUsernameIgnoreCase(String username);

}
