package com.example.demo.repository;

import com.example.demo.UserApp.User;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Configuration
@EnableJpaRepositories(basePackages = "com.example.demo.repository")
@Repository
@ComponentScan
public interface UserRepository  extends JpaRepository<User, Long> {
    public List<User> findAll();


}
