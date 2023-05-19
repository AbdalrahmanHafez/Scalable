package com.example.demo.userApp;


import com.example.demo.repository.userRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;


@Service
@Configuration

@ComponentScan("com.example.demo.repository")
public class userService {

    private final userRepository userRepository;

    @Autowired
    public userService(userRepository userRepository) {
        this.userRepository = userRepository;
    }


    public List<user> getUsers(){
        return userRepository.findAll();
    }

    public void addNewUser(user user){
        userRepository.save(user);
    }

    public void deleteUser(Long userID){
        boolean userExists = userRepository.existsById(userID);
        if(userExists){
            userRepository.deleteById(userID);
        }
        else{
            throw new IllegalStateException("User not found, user ID: " + userID);
        }
    }

    public void updateUser(Long userID, String name, String email, String password){
        user user = userRepository.findById(userID).orElseThrow(()-> new IllegalStateException("User with id " + userID + "does not exist"));
        if(name != null && name.length() > 0 && !Objects.equals(user.getName(), name)){
            user.setName(name);
            userRepository.save(user);
        }

        if(email != null && email.length() > 0 && !Objects.equals(user.getEmail(), email)){
            user.setEmail(email);
            userRepository.save(user);
        }

        if(password != null && password.length() > 0 && !Objects.equals(user.getPassword(), password)){
            user.setPassword(password);
            userRepository.save(user);
        }
    }


}

