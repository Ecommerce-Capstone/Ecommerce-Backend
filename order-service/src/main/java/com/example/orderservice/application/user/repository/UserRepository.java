package com.example.orderservice.application.user.repository;

import com.example.orderservice.application.user.entity.User;

import java.util.List;

public interface UserRepository {
    List<User> getUsers(List<Long> ids);
    User getUser(Long id);
}
