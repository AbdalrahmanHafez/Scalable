package com.example.demo;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Sender {

	// private final RabbitTemplate rabbitTemplate;

	// public Sender(RabbitTemplate rabbitTemplate) {
	// this.rabbitTemplate = rabbitTemplate;
	// }

	@Autowired
	private RabbitTemplate rabbitTemplate;

	public void sendMessage(String message) {

		rabbitTemplate.convertAndSend("myqueue", message);
		System.out.println("Sent message: " + message);
	}
}