package com.example.productApp.controllers;

import com.example.productApp.commands.Command;
import com.example.productApp.commands.Invoker;
import com.example.productApp.messageQueue.MessageRequest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Constructor;
import java.util.HashMap;

@RestController
@RequestMapping("/productsMessages")
public class ProductMQController {

    private final KafkaTemplate<String , String> kafkaTemplate;

    private Invoker invoker;

    //ReplyingKafkaTemplate<String , String ,String> replyingKafkaTemplate;

    public ProductMQController(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;

    }
    @PostMapping
    public void publish(@RequestBody MessageRequest request){
        kafkaTemplate.send("productApp", request.message());
    }


}
