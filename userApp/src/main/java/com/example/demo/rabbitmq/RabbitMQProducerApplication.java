package com.example.demo.Rabbitmq;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class RabbitMQProducerApplication {

    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public RabbitMQProducerApplication(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public static void main(String[] args) {
        SpringApplication.run(RabbitMQProducerApplication.class, args);
    }

    @PostMapping("/publish")
    public String publishMessage(@RequestBody String message) {
        rabbitTemplate.convertAndSend("myQueue", message);
        System.out.println("Message sent to RabbitMQ: " + message);
        return "Message published to RabbitMQ: " + message;
    }
}
