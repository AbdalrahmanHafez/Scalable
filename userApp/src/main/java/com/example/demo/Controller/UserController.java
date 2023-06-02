package com.example.demo.Controller;
import com.example.demo.Config.JwtTokenProvider;
import com.example.demo.Kafka.ControllerProducer;
import com.example.demo.Models.RequestThread;
import com.example.demo.Repository.UserRepository;
import com.example.demo.Models.User;
import com.example.demo.Services.UserService;
import com.example.demo.Services.LoggingService;
import com.example.demo.Kafka.KafkaJSONProducer;
import com.example.demo.Kafka.KafkaProducer;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;


@RestController
@RequestMapping(path ="api/v1")

public class UserController {
    @Autowired private HttpServletResponse response;
    private final JwtTokenProvider jwtTokenProvider;
    private Long verifyUser(String token){

        if(jwtTokenProvider.validateToken(token))
            return jwtTokenProvider.getIDFromToken(token);
        else
            return null;
    }

    private final KafkaProducer kafkaProducer;
    private final ControllerProducer kafkaJSONProducer;
    private final UserService userServices;
    private final UserRepository userRepository;
    @Autowired
    private LoggingService loggingService;

    @Autowired
    public UserController(UserService userServices, JwtTokenProvider jwtTokenProvider, UserRepository userRepository, KafkaProducer kafkaProducer, ControllerProducer kafkaJSONProducer) {
        this.userServices = userServices;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userRepository = userRepository;
        this.kafkaProducer = kafkaProducer;
        this.kafkaJSONProducer = kafkaJSONProducer;
    }

    @GetMapping(path = "/admin/user")
    @Async
    public CompletableFuture<List<User>> getUsers(){
        CompletableFuture<List<User>> users = userServices.getUsersAsync();
        loggingService.logInfo("Returning all users in DB");
        return users;
    }

    @PostMapping(path = "/admin/user")
    @Async
    public ResponseEntity<String> registerNewUser(@RequestBody User user){
        return userServices.addNewUser(user);
    }

    @DeleteMapping(path = "/admin/user/{user_id}")
    @Async
    public ResponseEntity<String> adminDeleteUser(@PathVariable("user_id") long id){
        return userServices.deleteUser(id);
    }

    @DeleteMapping(path = "/user")
    @Async
    public ResponseEntity<String> deleteUser(@RequestHeader("user-token") String token){
        Long userID = verifyUser(token);
        if(userID != null){
            return userServices.deleteUser(userID);
        }
        else{
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid user");
        }
    }

    @PatchMapping(path = "/user/{user_id}")
    @Async
    public ResponseEntity<String> updateUser(@RequestHeader("user-token") String token, @RequestParam(required = false) String name,
                           @RequestParam(required = false) String email, @RequestParam(required = false) String password){

        try {
            Long userID = jwtTokenProvider.getIDFromToken(token);
            if(userID == null){
                loggingService.logError("Invalid user Token");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid user Token");

            }
             loggingService.logInfo("user update successfully", userID);
            return userServices.updateUser(userID, name, email, password);

        }
        catch(Exception e){
            loggingService.logError(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/user/login")
    @Async
    public ResponseEntity<String> login(@RequestBody User body) {
        CompletableFuture<String> tokenFuture = userServices.login(body.getEmail(), body.getPassword());
        String token = null;
        try {
            token = tokenFuture.get();
        } catch (Exception e) {
            loggingService.logError("Invalid login credentials.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid login credentials.");
        }
        if (token != null) {
            response.setHeader("user-token", token);
            loggingService.logInfo("User logged in successfully");
            return ResponseEntity.ok("User logged in successfully");
        } else {
            loggingService.logError("Invalid login credentials.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid login credentials.");
        }
    }

    @PostMapping(path = "/admin/threadCount")
    public ResponseEntity<String> adjustThreads(@RequestBody RequestThread threads){
         return userServices.setThreadPoolSize(threads.getThreadCount());
    }

    @GetMapping("/kafka/publish")
    public ResponseEntity<String> publish(@RequestParam("message") String message)
    {
        kafkaProducer.sendMessage(message);
        return ResponseEntity.ok("Message sent to UserCluster");
    }

    @PostMapping("/kafka/publishJson")
    public ResponseEntity<String> publishJson()
    {
        HashMap<String,Object> data = new HashMap<String, Object>() ;
//        HashMap<String,String> userData = new HashMap<String, String>() ;
//        userData.put("name","NewUser3");
//        userData.put("email","newuser3@email.com");
//        userData.put("password","user3@pass");
//
//        data.put("adduser",userData);

        HashMap<String,String> loginData = new HashMap<String, String>() ;
//        loginData.put("email","yousseif@email.com");
//        loginData.put("password","yousseif@pass");

        //data.put("setthreadpoolsize",15);
        //data.put("login",loginData);
        data.put("logging","ERROR");
        kafkaJSONProducer.sendMessage(data);
        return ResponseEntity.ok("Message sent to user-controller");
    }


    @PostMapping(path = "/freeze")
    public void freezeServer(){
        userServices.freezeServer();
    }

    @PostMapping(path = "/unfreeze")
    public void unFreeze(){
        userServices.unfreezeServer();
    }

    @GetMapping(path = "/getFreezeStatus")
    public boolean getFreezeStatus(){
        return userServices.isServerFrozen();
    }




}
