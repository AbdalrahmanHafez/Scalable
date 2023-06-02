package com.example.productApp.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class ProductUserMQ {

    private final KafkaTemplate<Object, Map<String, Map<String, String>>> kafkaTemplate;

    public ProductUserMQ(KafkaTemplate<Object, Map<String, Map<String, String>>> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void logInhUserId(){
        Map<String , Map<String , String>> request = new HashMap<>();
        Map<String ,String> user = new HashMap<>();
        user.put("email","password");
        request.put("login" , user);
        kafkaTemplate.send("user-product" , request);
        log.info("logged in");
    }
}
