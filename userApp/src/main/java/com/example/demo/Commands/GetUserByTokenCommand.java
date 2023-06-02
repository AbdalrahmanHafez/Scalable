package com.example.demo.Commands;

import com.example.demo.Services.UserService;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;


@Component
public class GetUserByTokenCommand  extends Command {

    public GetUserByTokenCommand(UserService userService) {
        super(userService);
    }
    public String getName()
    {
        return "getuserbytoken";
    }
    @Override
    public Object execute(Object userToken)
    {
        CompletableFuture<Long> userID = getService().getUserByToken((String)userToken);
        try {
            return userID.get();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }
}

