package com.example.marketplace.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotBlank(message = "is required")
    @Size(min = 3, max = 50, message = "must be between 3 and 50 characters")
    private String username;

    @NotBlank(message = "is required")
    @Email(message = "must be a valid email address")
    private String email;

    @NotBlank(message = "is required")
    @Size(min = 6, message = "must be at least 6 characters")
    private String password;

    @NotBlank(message = "is required")
    @Pattern(regexp = "CUSTOMER|SELLER|ADMIN", message = "must be one of CUSTOMER, SELLER, ADMIN")
    private String role;

    @Pattern(regexp = "^$|^[0-9]{10}$", message = "must be a 10-digit phone number")
    private String phone;

    private boolean active;

    public User() {}

    public User(String username, String email, String password, String role, String phone, boolean active) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
        this.phone = phone;
        this.active = active;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

}