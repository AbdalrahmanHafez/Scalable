package com.example.demo.UserApp;

import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;


@Service
@Configuration
@EnableJpaRepositories("com.example.demo.repository.UserRepository")
@ComponentScan("com.example.demo.repository")
public class UserServices {

    private final UserRepository userRepository;

    @Autowired
    public UserServices(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    public List<User> getUsers(){
        return List.of(new User("youssef", "ymgendy@hotmail.com", "p4pass"));
    }

    public void addNewUser(User user){
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
        User user = userRepository.findById(userID).orElseThrow(()-> new IllegalStateException("User with id " + userID + "does not exist"));
        if(name != null && name.length() > 0 && !Objects.equals(user.getName(), name)){
            user.setName(name);
        }

        if(email != null && email.length() > 0 && !Objects.equals(user.getEmail(), email)){
            user.setEmail(email);
        }

        if(password != null && password.length() > 0 && !Objects.equals(user.getPassword(), password)){
            user.setPassword(password);
        }
    }


}
