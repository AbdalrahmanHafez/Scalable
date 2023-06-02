package com.example.demo.Commands;

import com.example.demo.Models.User;
import com.example.demo.Services.UserService;
import org.springframework.stereotype.Component;

@Component
public class DeleteUserCommand extends Command{

    public DeleteUserCommand(UserService userService) {
        super(userService);
    }
    public String getName()
    {
        return "deleteuser";
    }
    @Override
    public Object execute(Object obj) {
        Integer x = (Integer) obj;

        return getService().deleteUser(x.longValue()) ;
    }
}
