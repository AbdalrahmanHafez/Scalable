package com.example.demo.Controller;


import com.example.demo.UserApp.User;
import com.example.demo.UserApp.UserServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {
    @Autowired
    private final UserServices userServices;


    @Autowired
    public UserController(UserServices userServices) {
        this.userServices = userServices;
    }

    @GetMapping("user")
    public List<User> getUsers(){
        return userServices.getUsers();
    }

    @PostMapping("/user")
    public void registerNewUser(@RequestBody User user){
        userServices.addNewUser(user);
    }

    @DeleteMapping(path = "/user/{userID}")
    public void deleteUser(@PathVariable("{userID}") long id){
        userServices.deleteUser(id);
    }

    @PutMapping(path = "/user/{userID}")
    public void updateUser(@PathVariable("{userID}") long id, String name, String email, String password){
        userServices.updateUser(id, name, email, password);
    }
}
