package com.example.demo.Commands;

import com.example.demo.Services.UserService;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
public class FreezeCommand extends Command{

    public String getName()
    {
        return "freeze";
    }
    public FreezeCommand(UserService userService) {
        super(userService);
    }

    public Object execute(Object obj) {
        userService.freezeServer();  // Freeze the microservice
        return "App frozen";
    }

}
