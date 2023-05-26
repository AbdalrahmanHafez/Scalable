package com.example.demo.Services;


import com.example.demo.Config.JwtTokenProvider;
import com.example.demo.Models.User;
import com.example.demo.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
@Configuration
@ComponentScan("com.example.demo.repository")
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    private final ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtTokenProvider jwtTokenProvider, ThreadPoolTaskExecutor threadPoolTaskExecutor) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
        this.threadPoolTaskExecutor = threadPoolTaskExecutor;
    }


    public List<User> getUsers(){
        return userRepository.findAll();
    }

    public CompletableFuture<List<User>> getUsersAsync() {
        CompletableFuture<List<User>> users = CompletableFuture.supplyAsync(() -> {
            System.out.println(" method started. Thread: " + Thread.currentThread().getName());
            List<User> tempUsers = userRepository.findAll();
            System.out.println(" method ended. Thread: " + Thread.currentThread().getName());
            return tempUsers;
        }, threadPoolTaskExecutor);

       return users;
    }



    public ResponseEntity<String> addNewUser(User user){

        CompletableFuture<ResponseEntity<String>> response = userRepository.findUserByEmail(user.getEmail()).thenApply(duplicateUser ->{
            if(duplicateUser == null){
                String hashedPassword = passwordEncoder.encode(user.getPassword());
                user.setPassword(hashedPassword);
                userRepository.save(user);
                return ResponseEntity.ok("User Created successfully");
            }
            else{
                return ResponseEntity.status(HttpStatus.CONFLICT).body("A user with this email already exists, email: " + user.getEmail());
            }
        });

        try {
            return response.get();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    public ResponseEntity<String> deleteUser(Long userID){
        boolean userExists = userRepository.existsById(userID);
        if(userExists){
            userRepository.deleteById(userID);
            return ResponseEntity.ok("User Deleted successfully");
        }
        else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found, user ID: " + userID);
        }
    }

    public ResponseEntity<String> updateUser(Long userID, String name, String email, String password){
        Optional<User> optionalUser = userRepository.findById(userID);
        Boolean userExists = optionalUser.isPresent();

        if(!userExists){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found, user ID: " + userID);
        }

        else{
            User user = optionalUser.get();

            if(name != null && name.length() > 0 && !Objects.equals(user.getName(), name)){
                user.setName(name);
            }

            if(email != null && email.length() > 0 && !Objects.equals(user.getEmail(), email)){
                CompletableFuture<User> duplicateUser = userRepository.findUserByEmail(email);

                try {
                    if(duplicateUser.get() == null)
                        user.setEmail(email);
                    else return ResponseEntity.status(HttpStatus.CONFLICT).body("A user with this email already exists, email: " + email);
                } catch (Exception e) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
                }
            }

            if(password != null && password.length() > 0 && !Objects.equals(user.getPassword(), password)){
                user.setPassword(password);
            }

            userRepository.save(user);
            return ResponseEntity.ok("User Updated successfully");
        }
    }

    @Async
    public CompletableFuture<String> login(String email, String password) {

        CompletableFuture<String> result = CompletableFuture.supplyAsync(() -> {
            System.out.println("Login method started. Thread: " + Thread.currentThread().getName());

            CompletableFuture<User> loggedFuture = userRepository.findUserByEmail(email);

            User logged = null;
            try {
                logged = loggedFuture.get();
            } catch (Exception e) {
                return null;
            }

            if (logged != null) {
                boolean verified = false;
                try {
                    verified = passwordEncoder.matches(password, logged.getPassword());
                } catch (Exception e) {
                    return null;
                }
                if (verified) {
                    // Generate JWT token
                    System.out.println("Login method ended. Thread: " + Thread.currentThread().getName());
                    return jwtTokenProvider.generateToken(logged.getId());
                }
            }
            return null; // Return null if login fails
        }, threadPoolTaskExecutor);


        return result;
    }

    public ResponseEntity<String> setThreadPoolSize(int threadCount){
        try{
            threadPoolTaskExecutor.setCorePoolSize(threadCount);
            System.out.println("New thread pool size: " + threadPoolTaskExecutor.getCorePoolSize()); // Change to log
            return ResponseEntity.ok("Thread pool size changed to: " + threadCount);
        }
        catch(Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}