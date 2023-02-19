package com.example.userservice.application.user.repository;

import com.example.userservice.application.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserRepository {
    List<User> findAll();
    Page<User> findAll(Pageable pageable);
    User findById(Long id);
    User findUserByUsername(String username);
    User save(User user);
    boolean deleteById(Long id);
    List<User> findAll(List<Long> ids);
}
