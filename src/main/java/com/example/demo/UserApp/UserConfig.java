package com.example.demo.UserApp;


import com.example.demo.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class UserConfig {

    CommandLineRunner commandLineRunner(UserRepository repository){

        return args -> {
            User us1 = new User("youssef1", "ymgendy1@hotmail.com", "p4pass1");
            User us2 = new User("youssef2", "ymgendy2@hotmail.com", "p4pass2");
            repository.saveAll(List.of(us1, us2));
        };
    }
}
