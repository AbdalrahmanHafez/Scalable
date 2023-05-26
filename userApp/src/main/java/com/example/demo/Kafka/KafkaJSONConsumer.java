package com.example.demo.Kafka;

import com.example.demo.userApp.User;
import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaJSONConsumer
{
    private static final Logger LOGGER = LoggerFactory.getLogger(com.example.demo.Kafka.KafkaConsumer.class);
    @KafkaListener(topics = "UserClusterJson",groupId = "UserConsumerGroup")
    public void consume(User user)
    {
        LOGGER.info(String.format("Message received -> %s",user.toString()));
    }
}