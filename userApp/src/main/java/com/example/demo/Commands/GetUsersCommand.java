package com.example.demo.Commands;

import com.example.demo.Services.UserService;
import org.springframework.stereotype.Component;
@Component
public class GetUsersCommand extends Command {

    public GetUsersCommand(UserService userService) {
        super(userService);
    }
    public String getName()
    {
        return "getusers";
    }
    @Override
    public Object execute(Object userID)
    {
        return getService().getUsers();
    }
}
