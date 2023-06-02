package com.example.demo.Services;


import com.example.demo.Config.JwtTokenProvider;
import com.example.demo.Models.User;
import com.example.demo.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.logging.LogLevel;
import org.springframework.boot.logging.LoggingSystem;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
@Configuration
@Component
@ComponentScan("com.example.demo.repository")
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final Environment environment;
    @Autowired
    private LoggingService loggingService;

    @Autowired
    private final ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtTokenProvider jwtTokenProvider, Environment environment, ThreadPoolTaskExecutor threadPoolTaskExecutor) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
        this.environment = environment;
        this.threadPoolTaskExecutor = threadPoolTaskExecutor;
    }


    public List<User> getUsers(){
        return userRepository.findAll();
    }

    public CompletableFuture<List<User>> getUsersAsync() {
        CompletableFuture<List<User>> users = CompletableFuture.supplyAsync(() -> {
            loggingService.logInfo(" method started. Thread: " + Thread.currentThread().getName());
            List<User> tempUsers = userRepository.findAll();
            loggingService.logInfo(" method ended. Thread: " + Thread.currentThread().getName());
            return tempUsers;
        }, threadPoolTaskExecutor);

       return users;
    }

    public CompletableFuture<Long> getUserByToken(String token) {
        return CompletableFuture.supplyAsync(() -> {
            loggingService.logInfo(" method started. Thread: " + Thread.currentThread().getName());
            Long userID = jwtTokenProvider.getIDFromToken(token);
            loggingService.logInfo(" method ended. Thread: " + Thread.currentThread().getName());
            return userID;
        }, threadPoolTaskExecutor);

    }


    public ResponseEntity<String> addNewUser(User user){

        CompletableFuture<ResponseEntity<String>> response = userRepository.findUserByEmail(user.getEmail()).thenApply(duplicateUser ->{
            if(duplicateUser == null){
                String hashedPassword = passwordEncoder.encode(user.getPassword());
                user.setPassword(hashedPassword);
                userRepository.save(user);
                loggingService.logInfo("User Created successfully",user.getId());
                return ResponseEntity.ok("User Created successfully");
            }
            else{
                loggingService.logError("A user with this email already exists, email: " + user.getEmail());
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
            loggingService.logError("User not found, user ID: " + userID);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found, user ID: " + userID);
        }
    }

    public ResponseEntity<String> updateUser(Long userID, String name, String email, String password){
        Optional<User> optionalUser = userRepository.findById(userID);
        Boolean userExists = optionalUser.isPresent();

        if(!userExists){
            loggingService.logError("User not found, user ID: " + userID);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found, user ID: " + userID);
        }

        else{
            User user = optionalUser.get();

            if(name != null && name.length() > 0 && !Objects.equals(user.getName(), name)){
                loggingService.logInfo("Name has been changed.",user.getId());
                user.setName(name);
            }

            if(email != null && email.length() > 0 && !Objects.equals(user.getEmail(), email)){
                CompletableFuture<User> duplicateUser = userRepository.findUserByEmail(email);

                try {
                    if(duplicateUser.get() == null){
                        user.setEmail(email);
                        loggingService.logInfo("Email has been changed.", user.getId());
                    }

                    else {
                        loggingService.logError("A user with this email already exists, email: " + email);
                        return ResponseEntity.status(HttpStatus.CONFLICT).body("A user with this email already exists, email: " + email);}
                } catch (Exception e) {
                    loggingService.logError(e.getMessage());
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
                }
            }

            if(password != null && password.length() > 0 && !Objects.equals(user.getPassword(), password)){
                loggingService.logInfo("Password has been changed.", user.getId());
                String hashedPassword = passwordEncoder.encode(user.getPassword());
                user.setPassword(hashedPassword);
            }

            userRepository.save(user);
            loggingService.logInfo("User Updated successfully", user.getId());
            return ResponseEntity.ok("User Updated successfully");
        }
    }

    @Async
    public CompletableFuture<String> login(String email, String password) {

        CompletableFuture<String> result = CompletableFuture.supplyAsync(() -> {
            loggingService.logInfo("Login method started. Thread: " + Thread.currentThread().getName());

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
                    loggingService.logInfo("Login method ended. Thread: " + Thread.currentThread().getName());
                    loggingService.logInfo("generated Token for ", logged.getId());
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
            loggingService.logInfo("New thread pool size: " + threadPoolTaskExecutor.getCorePoolSize()); // Change to log
            return ResponseEntity.ok("Thread pool size changed to: " + threadCount);
        }
        catch(Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @Override
    public String toString() {
        return "service";
    }

    public String getProperty(String key) {
        return environment.getProperty(key);
    }

    public void setProperty(String key, String value) {
        System.setProperty(key, value);
    }

    public void setLoggingLevel(String logLevel) {
        LoggingSystem.get(getClass().getClassLoader()).setLogLevel("root", LogLevel.valueOf(logLevel));
    }

    public void freezeServer(){
        setProperty("app.freeze.enabled", "true");
    }

    public void unfreezeServer(){
        setProperty("app.freeze.enabled", "false");
    }

    public boolean isServerFrozen(){
        return Boolean.parseBoolean(getProperty("app.freeze.enabled"));
    }


}