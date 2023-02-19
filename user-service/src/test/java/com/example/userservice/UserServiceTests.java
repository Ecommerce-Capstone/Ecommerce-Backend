package com.example.userservice;

import com.example.userservice.application.user.entity.User;
import com.example.userservice.application.user.repository.UserRepository;
import com.example.userservice.application.user.service.UserService;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

@SpringBootTest
class UserServiceTests {
    @Mock
    UserRepository userRepository;
    @InjectMocks
    UserService userService;
    @Test
    public void getUsers_WhenUsersExist_ShouldReturnListOfUsers() {
        EasyRandom easyRandom = new EasyRandom();
        // Given
        List<User> expectedUsers = new ArrayList<>();
        expectedUsers.add(easyRandom.nextObject(User.class));
        expectedUsers.add(easyRandom.nextObject(User.class));
        expectedUsers.add(easyRandom.nextObject(User.class));
        given(userRepository.findAll()).willReturn(expectedUsers);
        // When
        List<User> actualUsers = userService.getUsers();
        // Then
        assertEquals(expectedUsers, actualUsers);
    }

    @Test
    public void saveUser_WhenUserValid_ShouldReturnUser(){
        EasyRandom easyRandom = new EasyRandom();
        // Given
        User expectedUser = easyRandom.nextObject(User.class);
        given(userRepository.save(expectedUser)).willReturn(expectedUser);
        // When
        User actualUser = userService.saveUser(expectedUser);
        // Then
        assertEquals(expectedUser, actualUser);
    }
}
