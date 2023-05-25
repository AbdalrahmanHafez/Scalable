package com.example.demo.userApp;




import com.example.demo.repository.userRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class userConfig {

    @Bean
    CommandLineRunner commandLineRunner(userRepository repository){

        return args -> {

        };
    }
}
