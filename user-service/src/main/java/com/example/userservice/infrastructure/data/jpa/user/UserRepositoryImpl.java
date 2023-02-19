package com.example.userservice.infrastructure.data.jpa.user;

import com.example.userservice.application.user.entity.User;
import com.example.userservice.application.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {
    private final JpaUserRepository jpaUserRepository;
    @Override
    public List<User> findAll() {
        List<UserEntity> userEntities = jpaUserRepository.findAll();
        return this.castUserEntityToUser(userEntities);
    }

    @Override
    public Page<User> findAll(Pageable pageable) {
        Page<UserEntity> userEntities = jpaUserRepository.findAll(pageable);
        return this.castUserEntityToUser(userEntities, pageable);
    }

    @Override
    public User findById(Long id) {
        Optional<UserEntity> userEntity = jpaUserRepository.findById(id);
        if (userEntity.isEmpty()){
            throw new NoSuchElementException();
        }
        User user = new User();
        BeanUtils.copyProperties(userEntity.get(), user);
        return user;
    }

    @Override
    public User findUserByUsername(String username) {
        Optional<UserEntity> userEntity = jpaUserRepository.findByUsername(username);
        if (userEntity.isEmpty()){
            throw new NoSuchElementException();
        }
        User user = new User();
        BeanUtils.copyProperties(userEntity.get(), user);
        return user;
    }

    @Override
    public User save(User user) {
        UserEntity userEntity = new UserEntity();
        BeanUtils.copyProperties(user, userEntity);
        userEntity = jpaUserRepository.save(userEntity);
        BeanUtils.copyProperties(userEntity, user);
        return user;
    }

    @Override
    public boolean deleteById(Long id) {
        this.findById(id);
        jpaUserRepository.deleteById(id);
        return true;
    }

    @Override
    public List<User> findAll(List<Long> ids) {
        List<UserEntity> userEntities = jpaUserRepository.findAllByIdIn(ids);
        return this.castUserEntityToUser(userEntities);
    }

    private List<User> castUserEntityToUser(List<UserEntity> userEntities){
        List<User> users = new ArrayList<>();
        for (UserEntity userEntity: userEntities){
            User user = new User();
            BeanUtils.copyProperties(userEntity, user);
            users.add(user);
        }
        return users;
    }

    private Page<User> castUserEntityToUser(Page<UserEntity> userEntities, Pageable pageable){
        List<User> users = new ArrayList<>();
        for (UserEntity userEntity: userEntities.getContent()){
            User user = new User();
            BeanUtils.copyProperties(userEntity, user);
            users.add(user);
        }
        return new PageImpl<>(users, pageable, userEntities.getTotalElements());
    }
}
