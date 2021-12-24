package com.example.springjwt.controller;

import com.example.springjwt.model.Role;
import com.example.springjwt.model.User;
import com.example.springjwt.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriBuilder;

import java.net.URI;
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
        //.ok means it returns a 200 HTTP response back if it works
        return ResponseEntity.ok().body(userService.getUsers());
    }

    @GetMapping("/user/{username}")
    public ResponseEntity<User>getUser(@PathVariable String username){

        return ResponseEntity.ok().body(userService.getUser(username));
    }

    @PostMapping("/user/save")
    public ResponseEntity<User> saveUser(@RequestBody User user){
        //here we want to return HTTP 201 which indicates a resource is created on the server which requires the uri of the created resource
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/user/save").toUriString());
        return ResponseEntity.created(uri).body(userService.saveUser(user));
    }

    @PostMapping("/role/save")
    public ResponseEntity<Role> saveRole(@RequestBody Role role){
        //here we want to return HTTP 201 which indicates a resource is created on the server which requires the uri of the created resource
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/role/save").toUriString());
        return ResponseEntity.created(uri).body(userService.saveRole(role));
    }

    @PostMapping("/role/addtouser")
    public ResponseEntity<?> saveRoleToUser(@RequestBody RoleToUserForm form ){
        //here we just want to return a HTTP OK reponse with no body
       userService.addRoleToUser(form.getUser(), form.getRole());
       return ResponseEntity.ok().build();
    }

}

//making use of this utility class in the saveRoleToUser method of the controller
class RoleToUserForm{

    private String role;
    private String user;

    public String getRole() {
        return role;
    }

    public String getUser() {
        return user;
    }
}
