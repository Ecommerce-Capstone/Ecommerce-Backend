package com.example.orderservice.infrastructure.data.rest.user;

import com.example.orderservice.application.product.entity.Product;
import com.example.orderservice.application.user.entity.User;
import com.example.orderservice.application.user.repository.UserRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Repository
public class UserRepositoryImpl implements UserRepository {
    @Value("${service.user.host}")
    String USER_SERVICE_HOST;
    @Override
    public List<User> getUsers(List<Long> ids) {
        RestTemplate restTemplate = new RestTemplate();
        StringBuilder sb = new StringBuilder();
        for (Long i: ids){
            sb.append(i);
            sb.append(",");
        }
        String response = restTemplate.getForObject(USER_SERVICE_HOST + "/users?ids=" + sb, String.class);
        List<User> users = null;
        try {
            JSONObject json = new JSONObject(response);
            ObjectMapper mapper = new ObjectMapper();
            mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
            users = mapper.readValue(json.getJSONArray("data").toString(), new TypeReference<List<User>>(){});

        } catch (Exception e){
            e.printStackTrace();
            log.error(e.getMessage());
        }
        return users;
    }

    @Override
    public User getUser(Long id) {
        RestTemplate restTemplate = new RestTemplate();
        String response = restTemplate.getForObject(USER_SERVICE_HOST + "/users/" + id.toString(), String.class);
        User user = null;
        try {
            JSONObject json = new JSONObject(response);
            ObjectMapper mapper = new ObjectMapper();
            mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
            user = mapper.readValue(json.getJSONObject("data").toString(), new TypeReference<User>(){});

        } catch (Exception e){
            e.printStackTrace();
            log.error(e.getMessage());
        }
        return user;
    }
}
