// package com.example.demo;

// import org.springframework.amqp.rabbit.core.RabbitTemplate;
// import org.springframework.stereotype.Service;

// @Service
// public class UserRegistrationService {

// private final RabbitTemplate rabbitTemplate;

// public UserRegistrationService(RabbitTemplate rabbitTemplate) {
// this.rabbitTemplate = rabbitTemplate;
// }

// public void registerUser(String userEmail) {
// // Save the user to the database

// // Send a message to the email service to send a welcome email
// rabbitTemplate.convertAndSend("user-registration-exchange",
// "user-registration.email", userEmail);
// }
// }