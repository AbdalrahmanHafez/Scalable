package com.example.demo.Commands;

import com.example.demo.Services.UserService;
import org.springframework.stereotype.Component;


@Component
public class SetMaxDBConnectionsCommand extends Command{
    public SetMaxDBConnectionsCommand(UserService userService) {
        super(userService);
    }

    public String getName() {
        return "set_max_db_connections_count";
    }

    @Override
    public Object execute(Object obj) {
        int newLogg = (int) obj;
        userService.setProperty("spring.datasource.maxActive", newLogg + "");
        return "Maximum db connections set to: " + newLogg;
    }
}

