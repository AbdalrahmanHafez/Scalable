package com.example.demo.Commands;

import com.example.demo.Services.UserService;
import org.springframework.stereotype.Component;

@Component
public class LoggingCommand extends Command{
    public LoggingCommand(UserService userService) {
        super(userService);
    }

    public String getName() {
        return "logging";
    }

    @Override
    public Object execute(Object obj) {
        String newLogg = (String) obj;
        userService.setLoggingLevel(newLogg);
        System.out.println("enter logging command");
        return "Message Queue address set to: " + newLogg;
    }
}
