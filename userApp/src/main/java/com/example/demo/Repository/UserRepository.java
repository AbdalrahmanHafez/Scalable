package com.example.demo.Repository;

import com.example.demo.Models.User;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Repository;

import java.util.concurrent.CompletableFuture;

@Configuration
@Repository
@ComponentScan
public interface UserRepository extends JpaRepository<User, Long> {

    @Async
    CompletableFuture<User> findUserByEmail(String email);




}