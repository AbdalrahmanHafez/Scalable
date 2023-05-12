package com.example.demo.UserApp;


import jakarta.persistence.*;


@Entity
@Table
public class User {
    @Id
    @SequenceGenerator(name = "usersequence", sequenceName = "usersequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "usersequence")
    public String name;
    public String email;
    public String password;

    public User(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
