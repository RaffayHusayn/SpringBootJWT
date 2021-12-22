package com.example.springjwt.controller;

import com.example.springjwt.model.User;
import com.example.springjwt.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserController {

    private final UserServiceImpl userService;

    @Autowired
    public UserController(UserServiceImpl userService){
        this.userService = userService;
    }
    @GetMapping("/users")
    public ResponseEntity<List<User>> getUsers(){
        return ResponseEntity.ok().body(userService.getUsers());
    }
}
