package com.example.demo.Commands;

import com.example.demo.Config.JwtTokenProvider;
import com.example.demo.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
public class UpdateUserCommand extends Command{

    @Autowired
    JwtTokenProvider jwtTokenProvider;
    public UpdateUserCommand(UserService userService) {
        super(userService);
    }

    @Override
    public String getName() {
        return "updateuser";
    }

    @Override
    public Object execute(Object obj) {
        HashMap<String,String> map = (HashMap<String, String>) obj;
        try {

            Long userID = jwtTokenProvider.getIDFromToken(map.get("token"));
            if(userID == null){
                ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Bad Request");
            }
            return getService().updateUser(userID, map.get("name"),map.get("email"),map.get("password"));
        }
        catch(Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
