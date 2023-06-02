package com.example.demo.Commands;

import com.example.demo.Services.UserService;
import org.springframework.stereotype.Component;

@Component
public class LoggingCommand extends Command{
    public LoggingCommand(UserService userService) {
        super(userService);
    }

    public String getName() {
        return "set_error_reporting_level";
    }

    @Override
    public Object execute(Object obj) {
        String newLogg = (String) obj;
        userService.setLoggingLevel(newLogg);
        return "Message Queue address set to: " + newLogg;
    }
}
