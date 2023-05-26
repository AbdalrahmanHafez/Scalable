package com.example.demo.rabbitmq;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

//@Component
public class RabbitMQReciever {

    @RabbitListener(queues = "Queue")
    public void recieveMessage(@Payload String message) {
    	 System.out.println("Received test message: " + message);
    	 }
}

