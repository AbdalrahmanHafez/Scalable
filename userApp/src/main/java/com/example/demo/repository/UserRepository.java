package com.example.demo.repository;

import com.example.demo.userApp.User;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Configuration

@Repository
@ComponentScan
public interface UserRepository extends JpaRepository<User, Long> {

    User findUserByEmail(String email);


}