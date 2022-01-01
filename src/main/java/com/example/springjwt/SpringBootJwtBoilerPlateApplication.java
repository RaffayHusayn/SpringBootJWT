package com.example.springjwt;

import com.example.springjwt.model.Role;
import com.example.springjwt.model.User;
import com.example.springjwt.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;

@SpringBootApplication
public class SpringBootJwtBoilerPlateApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootJwtBoilerPlateApplication.class, args);


    }


    /*
    Bean for the password encoder
     */
    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }



    @Bean
    CommandLineRunner run(UserService userService) {

        return args-> {
            userService.saveRole(new Role(1, "ROLE_USER"));
            userService.saveRole(new Role(2, "ROLE_MANAGER"));
            userService.saveRole(new Role(3, "ROLE_ADMIN"));
            userService.saveRole(new Role(4, "ROLE_SUPER_ADMIN"));

            userService.saveUser(new User(1001, "raffay", "boss", "password", new ArrayList<>()));
            userService.saveUser(new User(1002, "ayesha", "ayesha", "password", new ArrayList<>()));
            userService.saveUser(new User(1003, "nabia", "nabia5", "hmhmhm", new ArrayList<>()));
            userService.saveUser(new User(1004, "shehla", "shehlatab", "hmhmhm", new ArrayList<>()));
            userService.saveUser(new User(1005, "zafar", "zafarhusayn", "password", new ArrayList<>()));

            userService.addRoleToUser("boss", "ROLE_USER");
            userService.addRoleToUser("boss", "ROLE_ADMIN");
            userService.addRoleToUser("boss", "ROLE_MANAGER");
            userService.addRoleToUser("boss", "ROLE_SUPER_ADMIN");
            userService.addRoleToUser("ayesha", "ROLE_SUPER_ADMIN");
            userService.addRoleToUser("nabia5", "ROLE_ADMIN");
            userService.addRoleToUser("shehlatab", "ROLE_MANAGER");
            userService.addRoleToUser("zafarhusayn", "ROLE_USER");

        };

    }
}
