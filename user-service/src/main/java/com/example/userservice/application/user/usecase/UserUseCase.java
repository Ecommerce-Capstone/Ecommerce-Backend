package com.example.userservice.application.user.usecase;

import com.example.userservice.application.user.dto.UserSafe;
import com.example.userservice.application.user.entity.User;
import org.springframework.data.domain.Page;

import java.util.List;

public interface UserUseCase {
    List<User> getUsers();
    Page<User> getUsers(Integer page, Integer size);
    User getUserById(Long id);
    User getUserByUsername(String username);
    User saveUser(User user);
    User saveUser(Long id, User user);
    List<UserSafe> castToUserSafe(List<User> users);
    Page<UserSafe> castToUserSafe(Page<User> users);
    UserSafe castToUserSafe(User user);
    boolean deleteUserById(Long id);
    List<User> getUsers(List<Long> ids);

}
