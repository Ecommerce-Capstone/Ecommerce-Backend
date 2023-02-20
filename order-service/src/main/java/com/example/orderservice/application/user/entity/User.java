package com.example.orderservice.application.user.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private Long id;
    private String username;
    private String fullName;
    private String email;
    private String photo;
    private String role;
    private String address;
    private Date createdAt;
    private Date updatedAt;
    private Date deletedAt;
}
