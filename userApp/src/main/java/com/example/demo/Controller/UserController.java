package com.example.demo.Controller;

import com.example.demo.Config.JwtTokenProvider;
import com.example.demo.Kafka.KafkaJSONProducer;
import com.example.demo.Kafka.KafkaProducer;
import com.example.demo.repository.UserRepository;
import com.example.demo.userApp.User;
import com.example.demo.userApp.UserService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    private KafkaProducer kafkaProducer;
    private KafkaJSONProducer kafkaJSONProducer;
    private final UserService userServices;
    private final UserRepository userRepository;

    @Autowired
    public UserController(UserService userServices, JwtTokenProvider jwtTokenProvider, UserRepository userRepository, KafkaProducer kafkaProducer, KafkaJSONProducer kafkaJSONProducer) {
        this.userServices = userServices;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userRepository = userRepository;
        this.kafkaProducer = kafkaProducer;
        this.kafkaJSONProducer = kafkaJSONProducer;
    }

    @GetMapping(path = "/admin/user")
    public List<User> getUsers(){
        return userServices.getUsers();
    }

    @PostMapping(path = "/admin/user")
    public ResponseEntity<String> registerNewUser(@RequestBody User user){
        return userServices.addNewUser(user);
    }

    @DeleteMapping(path = "/admin/user/{user_id}")
    public ResponseEntity<String> adminDeleteUser(@PathVariable("user_id") long id){
        return userServices.deleteUser(id);
    }

    @DeleteMapping(path = "/user")
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
    public ResponseEntity<String> updateUser(@RequestHeader("user-token") String token, @RequestParam(required = false) String name,
                           @RequestParam(required = false) String email, @RequestParam(required = false) String password){

        try {
            Long userID = jwtTokenProvider.getIDFromToken(token);
            if(userID == null)
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid user");

            return userServices.updateUser(userID, name, email, password);

        }
        catch(Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid user");
        }
    }

    @PostMapping("/user/login")
    public ResponseEntity<String> login(@RequestBody User body) {
        String token = userServices.login(body.getEmail(), body.getPassword());
        if (token != null) {
            response.setHeader("user-token", token);
            return ResponseEntity.ok("User logged in successfully");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid login credentials.");
        }
    }

    @GetMapping("/kafka/publish")
    public ResponseEntity<String> publish(@RequestParam("message") String message)
    {
        kafkaProducer.sendMessage(message);
        return ResponseEntity.ok("Message sent to UserCluster");
    }

    @PostMapping("/kafka/publishJson")
    public ResponseEntity<String> publishJson(@RequestBody User user)
    {
        kafkaJSONProducer.sendMessage(user);
        return ResponseEntity.ok("Message sent to UserCluster");
    }



}