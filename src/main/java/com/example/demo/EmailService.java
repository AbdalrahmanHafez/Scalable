// package com.example.demo;

// import org.springframework.amqp.rabbit.annotation.RabbitListener;
// import org.springframework.amqp.rabbit.core.RabbitTemplate;
// import org.springframework.stereotype.Service;

// @Service
// public class EmailService {

// private final RabbitTemplate rabbitTemplate;

// public EmailService(RabbitTemplate rabbitTemplate) {
// this.rabbitTemplate = rabbitTemplate;
// }

// @RabbitListener(queues = "user-registration.email")
// public void sendWelcomeEmail(String email) {
// // Send a welcome email to the new user
// String message = "Welcome to our platform, " + email + "!";
// rabbitTemplate.convertAndSend("email-response", message);
// }
// }