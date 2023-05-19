package com.example.demo.Controller;



import com.example.demo.userApp.user;
import com.example.demo.userApp.userService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path ="api/v1/user")
public class userController {
    @Autowired
    private final userService userServices;


    @Autowired
    public userController(userService userServices) {
        this.userServices = userServices;
    }

    @GetMapping()
    public List<user> getUsers(){
        return userServices.getUsers();
    }

    @PostMapping()
    public void registerNewUser(@RequestBody user user){
        userServices.addNewUser(user);
    }

    @DeleteMapping(path = "{userID}")
    public void deleteUser(@PathVariable("userID") long id){
        userServices.deleteUser(id);
    }

    @PutMapping(path = "{userID}")
    public void updateUser(@PathVariable("userID") long id, String name, String email, String password){
        userServices.updateUser(id, name, email, password);
    }
}