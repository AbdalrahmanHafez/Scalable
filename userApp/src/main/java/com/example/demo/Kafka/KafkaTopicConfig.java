package com.example.demo.Kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

    @Bean
    public NewTopic topicBuilder()
    {
        return TopicBuilder.name("UserCluster").partitions(10).build();
    }
    @Bean
    public NewTopic topicControllerBuilder()
    {
        return TopicBuilder.name("user-controller").partitions(10).build();
    }
    @Bean
    public NewTopic topicJsonBuilder()
    {
        return TopicBuilder.name("UserClusterJson").partitions(10).build();
    }
    @Bean
    public NewTopic topicProductBuilder()
    {
        return TopicBuilder.name("user-product").partitions(10).build();
    }
}
