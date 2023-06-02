package com.example.demo.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
public class ThreadPoolConfig {

    @Bean
    public ThreadPoolTaskExecutor threadPoolTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(10); // Set the core pool size
        executor.setMaxPoolSize(50); // Set the maximum pool size
        executor.setQueueCapacity(50); // Set the queue capacity
        executor.setThreadNamePrefix("MyThreadPool-"); // Set the thread name prefix
        executor.initialize();
        return executor;
    }

}