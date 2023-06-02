package com.example.demo.Commands;

import com.example.demo.Models.User;
import com.example.demo.Services.UserService;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
public class AddUserCommand extends Command{

    public AddUserCommand(UserService userService) {
        super(userService);
    }

    public String getName()
    {
        return "adduser";
    }
    @Override
    public Object execute(Object userData)
    {
        HashMap<String,String> userDataMap = (HashMap<String, String>) userData;
        return getService().addNewUser(new User(userDataMap.get("name"),userDataMap.get("email"),userDataMap.get("password")));
    }
}
