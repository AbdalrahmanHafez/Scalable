package com.example.demo.Commands;
import com.example.demo.Services.UserService;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
public class ContinueCommand extends Command {

    public String getName()
    {
        return "continue";
    }
    public ContinueCommand(UserService userService) {
        super(userService);
    }

    public Object execute(Object obj) {
        userService.unfreezeServer();  // Freeze the microservice
        return "App unfrozen";
    }
}
