package com.example.productApp.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class ProductTopicConfig {

    @Bean
    public NewTopic kafkaTopic(){
        return TopicBuilder.name("productApp")
                .build();
    }
    @Bean
    public NewTopic productControllerTopic(){
        return TopicBuilder.name("product-controller")
                .build();
    }
}
