package com.example.springjwt;

import com.example.springjwt.model.Role;
import com.example.springjwt.model.User;
import com.example.springjwt.service.UserService;
import com.example.springjwt.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

@SpringBootApplication
public class SpringBootJwtBoilerPlateApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootJwtBoilerPlateApplication.class, args);


    }


    @Autowired
    UserServiceImpl userService;
    @Override
    public void run(String... args) throws Exception {

        userService.saveRole(new Role(1, "ROLE_USER"));
        userService.saveRole(new Role(2, "ROLE_ADMIN"));

        userService.saveUser(new User(1001, "raffay","raffayhusayn","password", new ArrayList<>()));
        userService.saveUser(new User(1002, "ayesha","ayeshahusayn","password2", new ArrayList<>()));
        userService.saveUser(new User(1003, "nabia","nabia5","hmhmhm", new ArrayList<>()));

        userService.addRoleToUser("raffayhusayn", "ROLE_USER");
        userService.addRoleToUser("raffayhusayn", "ROLE_ADMIN");
        userService.addRoleToUser("ayeshahusayn", "ROLE_USER");
        userService.addRoleToUser("nabia5", "ROLE_ADMIN");

    }
}
