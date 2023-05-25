package com.example.demo.userApp;


import com.example.demo.Config.JwtTokenProvider;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;


import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Configuration
@ComponentScan("com.example.demo.repository")
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    private LoggingService loggingService;


    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtTokenProvider jwtTokenProvider) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public List<User> getUsers(){
        return userRepository.findAll();
    }

    public ResponseEntity<String> addNewUser(User user){
        User duplicateUser = userRepository.findUserByEmail(user.getEmail());
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
    }

    public ResponseEntity<String> deleteUser(Long userID){
        boolean userExists = userRepository.existsById(userID);
        if(userExists){
            userRepository.deleteById(userID);
            loggingService.logInfo("User Deleted successfully", userID);
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
                User duplicateUser = userRepository.findUserByEmail(email);
                if(duplicateUser == null){
                    user.setEmail(email);
                    loggingService.logInfo("Email has been changed.", user.getId());
                }

                else {
                    loggingService.logError("A user with this email already exists, email: " + email);
                    return ResponseEntity.status(HttpStatus.CONFLICT).body("A user with this email already exists, email: " + email);
                }
            }

            if(password != null && password.length() > 0 && !Objects.equals(user.getPassword(), password)){
                loggingService.logInfo("Password has been changed.", user.getId());
                user.setPassword(password);
            }

            userRepository.save(user);
            loggingService.logInfo("User Updated successfully", user.getId());
            return ResponseEntity.ok("User Updated successfully");
        }
    }

    public String login(String email, String password){
        User logged = userRepository.findUserByEmail(email);
        if(logged != null){
            boolean verified = passwordEncoder.matches(password, logged.getPassword());
            if (verified) {
                // Generate JWT token
                loggingService.logInfo("generated Token for ", logged.getId());
                return jwtTokenProvider.generateToken(logged.getId());
            }
        }

        return null; // Return null if login fails
    }
}
