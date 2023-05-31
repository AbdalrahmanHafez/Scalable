package com.example.productApp.kafka;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("appsMessages")
public class AppController {

    private final KafkaTemplate<String , String> kafkaTemplate;

    public AppController(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;

    }

    @PostMapping
    public void publish(@RequestBody MessageRequest request){
        kafkaTemplate.send("productApp", request.message());
    }
}
