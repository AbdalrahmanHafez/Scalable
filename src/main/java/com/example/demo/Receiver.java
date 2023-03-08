package com.example.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class Receiver {

	// private static final Logger LOGGER = LoggerFactory.getLogger(Receiver.class);

	@RabbitListener(queues = { "myqueue" })
	public void receiveMessage(@Payload String message) {
		System.out.println("Received message: " + message);
		// LOGGER.info("Received message: {}", message);
	}
}