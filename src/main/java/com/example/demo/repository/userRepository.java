package com.example.demo.repository;

import com.example.demo.userApp.user;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Configuration

@Repository
@ComponentScan
public interface userRepository extends JpaRepository<user, Long> {



}