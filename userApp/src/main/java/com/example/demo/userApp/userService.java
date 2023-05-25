package com.example.demo.userApp;


import com.example.demo.Config.JwtTokenProvider;
import com.example.demo.repository.userRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@Configuration
@ComponentScan("com.example.demo.repository")
public class userService {
    private final userRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public userService(userRepository userRepository, PasswordEncoder passwordEncoder, JwtTokenProvider jwtTokenProvider) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public List<user> getUsers(){
        return userRepository.findAll();
    }

    public void addNewUser(user user){
        String hashedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(hashedPassword);
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
            System.out.println(user.getName());
            user.setName(name);
            System.out.println(user.getName());
        }

        if(email != null && email.length() > 0 && !Objects.equals(user.getEmail(), email)){
            user.setEmail(email);
        }

        if(password != null && password.length() > 0 && !Objects.equals(user.getPassword(), password)){
            user.setPassword(password);
        }

        userRepository.save(user);
    }

    public String login(String email, String password){
        user logged = userRepository.findUserByEmail(email);
        if(logged != null){
            boolean verified = passwordEncoder.matches(password, logged.getPassword());
            if (verified) {
                // Generate JWT token
                return jwtTokenProvider.generateToken(email);
            }
        }
        return null; // Return null if login fails
    }
}
