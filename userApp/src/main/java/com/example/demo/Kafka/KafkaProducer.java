package com.example.demo.Kafka;

import org.slf4j.ILoggerFactory;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

@Service
public class KafkaProducer
{

    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaProducer.class);
    private KafkaTemplate<String,String> kafkaTemplat;

    public KafkaProducer(KafkaTemplate<String,String> kafkaTemplate)
    {
        this.kafkaTemplat = kafkaTemplate;
    }

    public  void sendMessage(String message)
    {
        LOGGER.info(String.format("Message sent %s",message));
        kafkaTemplat.send("UserCluster",message);
    }
}
