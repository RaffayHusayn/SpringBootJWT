package com.example.springjwt;

import ch.qos.logback.core.pattern.color.BoldCyanCompositeConverter;
import com.example.springjwt.model.Role;
import com.example.springjwt.model.User;
import com.example.springjwt.service.UserService;
import com.example.springjwt.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

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


    /*
    Bean for the password encoder
     */
    @Bean
    BCryptPasswordEncoder getBcryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }



    @Autowired
    UserServiceImpl userService;
    @Override
    public void run(String... args) throws Exception {

        userService.saveRole(new Role(1, "ROLE_USER"));
        userService.saveRole(new Role(2, "ROLE_MANAGER"));
        userService.saveRole(new Role(3, "ROLE_ADMIN"));
        userService.saveRole(new Role(4, "ROLE_SUPER_ADMIN"));

        userService.saveUser(new User(1001, "raffay","raffayhusayn","password", new ArrayList<>()));
        userService.saveUser(new User(1002, "ayesha","ayeshahusayn","password2", new ArrayList<>()));
        userService.saveUser(new User(1003, "nabia","nabia5","hmhmhm", new ArrayList<>()));
        userService.saveUser(new User(1004, "shehla","shehlatab","hmhmhm", new ArrayList<>()));
        userService.saveUser(new User(1003, "zafar","zafarhusayn","password", new ArrayList<>()));

        userService.addRoleToUser("raffayhusayn", "ROLE_USER");
        userService.addRoleToUser("raffayhusayn", "ROLE_ADMIN");
        userService.addRoleToUser("ayeshahusayn", "ROLE_USER");
        userService.addRoleToUser("nabia5", "ROLE_ADMIN");
        userService.addRoleToUser("shehlatab", "ROLE_USER");
        userService.addRoleToUser("shehlatab", "ROLE_MANAGER");
        userService.addRoleToUser("zafarhusayn", "ROLE_USER");
        userService.addRoleToUser("zafarhusayn", "ROLE_SUPER_ADMIN");


    }
}
