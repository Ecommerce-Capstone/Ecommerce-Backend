package com.example.userservice.application.user.entity;

import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private Long id;
    @NotEmpty(message = "Username is required")
    private String username;
    private String password;
    @NotEmpty(message = "Fullname is required")
    private String fullName;
    @NotEmpty(message = "Email is required")
    @Email
    private String email;
    private String photo;
    private String role;
    private String address;
    private Date createdAt;
    private Date updatedAt;
    private Date deletedAt;
}
