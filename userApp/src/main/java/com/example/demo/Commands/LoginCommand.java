package com.example.demo.Commands;

import com.example.demo.Services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

@Component
public class LoginCommand extends Command{
    public LoginCommand(UserService userService) {
        super(userService);
    }
    public String getName()
    {
        return "login";
    }

    @Override
    public Object execute(Object obj) {
        HashMap<String,String> map = (HashMap<String, String>) obj;
        CompletableFuture<String> tokenFuture = getService().login(map.get("email"), map.get("password"));

        String token = null;
        try {
            token = tokenFuture.get();
        } catch (Exception e) {
            return ("Invalid login credentials.");
        }
        if (token != null) {
            return token;
        } else {
            return ("Invalid login credentials.");
        }
    }
}
