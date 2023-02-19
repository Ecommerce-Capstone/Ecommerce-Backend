package com.example.userservice.application.user.service;

import com.example.userservice.application.shared.GetNullProperties;
import com.example.userservice.application.user.dto.UserSafe;
import com.example.userservice.application.user.entity.User;
import com.example.userservice.application.user.repository.UserRepository;
import com.example.userservice.application.user.usecase.UserUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.example.userservice.application.shared.GetNullProperties.getNullPropertyNames;

@Service
@RequiredArgsConstructor
public class UserService implements UserUseCase {
    private final UserRepository userRepository;
    @Override
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    @Override
    public Page<User> getUsers(Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        return userRepository.findAll(pageable);
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public User getUserByUsername(String username) {
        return userRepository.findUserByUsername(username);
    }

    @Override
    public User saveUser(@Valid User user) {
        user.setPassword(this.hashPassword(user.getPassword()));
        return userRepository.save(user);
    }
    @Override
    public User saveUser(Long id, @Valid User userParam) {
        User user = this.getUserById(id);
        if (userParam.getPassword() != null){
            userParam.setPassword(this.hashPassword(userParam.getPassword()));
        }
        BeanUtils.copyProperties(userParam, user, getNullPropertyNames(userParam));
        return userRepository.save(user);
    }

    @Override
    public List<UserSafe> castToUserSafe(List<User> users) {
        List<UserSafe> userSafes = new ArrayList<>();
        for (User user : users){
            UserSafe userSafe = new UserSafe();
            BeanUtils.copyProperties(user, userSafe);
            userSafes.add(userSafe);
        }
        return userSafes;
    }

    @Override
    public Page<UserSafe> castToUserSafe(Page<User> users) {
        List<UserSafe> userSafes = new ArrayList<>();
        for (User user: users.getContent()){
            UserSafe userSafe = new UserSafe();
            BeanUtils.copyProperties(user, userSafe);
            userSafes.add(userSafe);
        }
        return new PageImpl<>(userSafes, users.getPageable(), users.getTotalElements());
    }

    @Override
    public UserSafe castToUserSafe(User user) {
        UserSafe userSafe = new UserSafe();
        BeanUtils.copyProperties(user, userSafe);
        return userSafe;
    }

    @Override
    public boolean deleteUserById(Long id) {
        userRepository.deleteById(id);
        return true;
    }

    @Override
    public List<User> getUsers(List<Long> ids) {
        return userRepository.findAll(ids);
    }

    private String hashPassword(String plainText){
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.encode(plainText);
    }


}
