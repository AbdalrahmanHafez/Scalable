package com.example.demo.Commands;

import com.example.demo.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;

public class  Command implements CommandInterface{

    @Autowired
    protected UserService userService;

    @Autowired
    public Command(UserService userService) {
        this.userService = userService;
    }
    public UserService getService() {
        return userService;
    }
    @Autowired
    public final void setService(UserService service) {
        this.userService = service;
    }

    @Override
    public Object execute(Object obj) {
        return obj;
    }

    public String getName()
    {
        return "Command";
    }
}
