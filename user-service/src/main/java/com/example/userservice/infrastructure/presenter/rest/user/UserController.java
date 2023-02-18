package com.example.userservice.infrastructure.presenter.rest.user;

import com.example.userservice.application.user.entity.User;
import com.example.userservice.application.user.usecase.UserUseCase;
import com.example.userservice.infrastructure.presenter.rest.Response;
import com.example.userservice.infrastructure.presenter.rest.authentication.dto.AuthenticatedUser;
import com.example.userservice.infrastructure.presenter.rest.exception.ResourceForbiddenException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserUseCase userUseCase;
    @GetMapping
    public ResponseEntity<Object> getUsers(@RequestParam(required = false, defaultValue = "0") Integer page, @RequestParam(required = false, defaultValue = "10") Integer size){
        log.info("GET /users called with param page: {}, size: {}", page, size);
        Response response = new Response();
        Page<User> users = userUseCase.getUsers(page, size);
        response.setPageData(userUseCase.castToUserSafe(users));
        return response.getResponse();
    }
    @GetMapping(path = "/{id}")
    public ResponseEntity<Object> getUser(@PathVariable Long id) throws Exception {
        log.info("GET /users/{} called", id);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        AuthenticatedUser authenticatedUser = (AuthenticatedUser) authentication.getPrincipal();
        if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_customer")) && id != authenticatedUser.getUserId()){
            throw new ResourceForbiddenException();
        }

        Response response = new Response();
        User user = userUseCase.getUserById(id);
        response.setData(userUseCase.castToUserSafe(user));
        return response.getResponse();
    }
    @PostMapping
    public ResponseEntity<Object> createUser(@Valid @RequestBody User user){
        log.info("POST /users called with body: {}", user);
        Response response = new Response();
        user.setRole("customer");
        user = userUseCase.saveUser(user);
        response.setData(userUseCase.castToUserSafe(user));
        response.setHttpCode(201);
        return response.getResponse();
    }
    @PatchMapping(path = "/{id}")
    public ResponseEntity<Object> updateUser(@PathVariable Long id, @RequestBody User user) throws Exception {
        log.info("PATCH /users/{} called with body: {}", id, user);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        AuthenticatedUser authenticatedUser = (AuthenticatedUser) authentication.getPrincipal();
        if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_customer")) && id != authenticatedUser.getUserId()){
            throw new ResourceForbiddenException();
        }

        Response response = new Response();
        user.setRole("customer");
        user = userUseCase.saveUser(id, user);
        response.setData(userUseCase.castToUserSafe(user));
        return response.getResponse();
    }
    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Object> deleteUser(@PathVariable Long id){
        log.info("DELETE /users/{} called", id);
        Response response = new Response();
        userUseCase.deleteUserById(id);
        response.setHttpCode(204);
        return response.getResponse();
    }
}
