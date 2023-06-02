package com.example.demo.Commands;

import com.example.demo.Services.UserService;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
public class SetThreadPoolSizeCommand extends Command{
    public SetThreadPoolSizeCommand(UserService userService) {
        super(userService);
    }
    public String getName()
    {
        return "setthreadpoolsize";
    }
    @Override
    public Object execute(Object obj) {
        return getService().setThreadPoolSize((int) obj);
    }
}
