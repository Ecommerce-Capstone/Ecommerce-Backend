package com.example.orderservice.infrastructure.data.rest.product;

import com.example.orderservice.application.product.entity.Product;
import com.example.orderservice.application.product.repository.ProductRepository;
import com.example.orderservice.infrastructure.data.rest.dto.RestResponse;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
@Slf4j
public class ProductRepositoryImpl implements ProductRepository {
    @Value("${service.product.host}")
    String PRODUCT_SERVICE_HOST;
    @Override
    public List<Product> getProducts(List<Long> id) {
        RestTemplate restTemplate = new RestTemplate();
        StringBuilder ids = new StringBuilder();
        for (Long i: id){
            ids.append(i);
            ids.append(",");
        }
        String response = restTemplate.getForObject(PRODUCT_SERVICE_HOST + "/products?ids=" + ids, String.class);
        List<Product> products = null;
        try {
            JSONObject json = new JSONObject(response);
            ObjectMapper mapper = new ObjectMapper();
            mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
            products = mapper.readValue(json.getJSONArray("data").toString(), new TypeReference<List<Product>>(){});

        } catch (Exception e){
            e.printStackTrace();
            log.error(e.getMessage());
        }
        return products;
    }
}
