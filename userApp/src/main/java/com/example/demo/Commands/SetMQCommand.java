package com.example.demo.Commands;

import com.example.demo.Services.UserService;
import org.springframework.stereotype.Component;

@Component
public class SetMQCommand extends Command {


    public SetMQCommand(UserService userService) {
        super(userService);
    }

    @Override
    public String getName() {
        return "set_mq";
    }

    @Override
    public Object execute(Object obj) {
        String newMQAddress = (String) obj;
        userService.setProperty("mq.address",newMQAddress);
        return "Message Queue address set to: " + newMQAddress;
    }
}
