package com.example.demo.Controller;

import com.example.demo.Config.JwtTokenProvider;
import com.example.demo.userApp.user;
import com.example.demo.userApp.userService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path ="api/v1/user")

public class userController {
    @Autowired private HttpServletResponse response;
    private final JwtTokenProvider jwtTokenProvider;
    private String verifyUser(String token){

        if(jwtTokenProvider.validateToken(token))
            return jwtTokenProvider.getEmailFromToken(token);
        else
            return null;
    }


    private final userService userServices;

    @Autowired
    public userController(userService userServices, JwtTokenProvider jwtTokenProvider) {
        this.userServices = userServices;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @GetMapping()
    public List<user> getUsers(){
        return userServices.getUsers();
    }

    @PostMapping()
    public void registerNewUser(@RequestBody user user){
        userServices.addNewUser(user);
    }

    @DeleteMapping(path = "{user_id}")
    public void deleteUser(@PathVariable("user_id") long id){
        userServices.deleteUser(id);
    }

    @PutMapping(path = "{user_id}")
    public void updateUser(@PathVariable("user_id") long id, @RequestParam(required = false) String name,
                           @RequestParam(required = false) String email, @RequestParam(required = false) String password){
        userServices.updateUser(id, name, email, password);
    }

    @PostMapping("/login")
    public String login(@RequestBody user body) {
        String token = userServices.login(body.getEmail(), body.getPassword());
        if (token != null) {
            response.setHeader("user-token", token);
            return token;
        } else {
            throw new IllegalStateException("Invalid login credentials.");
        }
    }
}