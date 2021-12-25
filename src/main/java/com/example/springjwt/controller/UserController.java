package com.example.springjwt.controller;

import com.example.springjwt.model.Role;
import com.example.springjwt.model.User;
import com.example.springjwt.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
        if(userService.getUser(username) == null){
            return ResponseEntity.notFound().build();
        }else {
            return ResponseEntity.ok().body(userService.getUser(username));
        }
    }

    @DeleteMapping("/user/{username}")
    public ResponseEntity<User> deleteUser(@PathVariable String username){
        if(userService.getUser(username) == null){
            return ResponseEntity.notFound().build();
        }else {
            return ResponseEntity.ok().body(userService.deleteUser(username));
        }
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



    //json raw request to use: { "username":---- , "role":----} because of the RoleToUserForm class field names
    @PostMapping("/role/addtouser")
    public ResponseEntity<?> saveRoleToUser(@RequestBody RoleToUserForm form ){
        if(userService.roleExistInUser(form.getUsername(), form.getRole())){
            //here we just want to return HTTP 409 conflict means the role already exist with the user so can't add again
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        //here we just want to return a HTTP OK reponse with no body
       userService.addRoleToUser(form.getUsername(), form.getRole());
       return ResponseEntity.ok().build();
    }

    //json raw request to use: { "username":---- , "role":----} because of the RoleToUserForm class field names
    @PostMapping("role/deletefromuser")
    public ResponseEntity<?> deleteRoleFromUser(@RequestBody RoleToUserForm form){
        if(userService.roleExistInUser(form.getUsername(), form.getRole())) {
            userService.deleteRoleFromUser(form.getUsername(), form.getRole());
            return ResponseEntity.ok().build();
        }else{
            return ResponseEntity.notFound().build();
        }
    }

}

//making use of this utility class in the saveRoleToUser method of the controller
class RoleToUserForm{

    private String role;
    private String username;

    public String getRole() {
        return role;
    }

    public String getUsername() {
        return username;
    }
}
