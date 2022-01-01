package com.example.springjwt.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.springjwt.model.Role;
import com.example.springjwt.model.User;
import com.example.springjwt.service.UserServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@Slf4j
public class UserController {

    private final UserServiceImpl userService;


    @Autowired
    public UserController(UserServiceImpl userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> getUsers() {
        //.ok means it returns a 200 HTTP response back if it works
        return ResponseEntity.ok().body(userService.getUsers());
    }

    @GetMapping("/user/{username}")
    public ResponseEntity<User> getUser(@PathVariable String username) {
        if (userService.getUser(username) == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok().body(userService.getUser(username));
        }
    }

    @DeleteMapping("/delete/user/{username}")
    public ResponseEntity<User> deleteUser(@PathVariable String username) {
        if (userService.getUser(username) == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok().body(userService.deleteUser(username));
        }
    }

    @PostMapping("/user/save")
    public ResponseEntity<User> saveUser(@RequestBody User user) {
        //here we want to return HTTP 201 which indicates a resource is created on the server which requires the uri of the created resource
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/user/save").toUriString());
        return ResponseEntity.created(uri).body(userService.saveUser(user));
    }

    @PostMapping("/role/save")
    public ResponseEntity<Role> saveRole(@RequestBody Role role) {
        //here we want to return HTTP 201 which indicates a resource is created on the server which requires the uri of the created resource
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/role/save").toUriString());
        return ResponseEntity.created(uri).body(userService.saveRole(role));
    }

    //json raw request to use: { "username":---- , "role":----} because of the RoleToUserForm class field names
    @PostMapping("/role/addtouser")
    public ResponseEntity<?> saveRoleToUser(@RequestBody RoleToUserForm form) {
        if (userService.roleExistInUser(form.getUsername(), form.getRole())) {
            //here we just want to return HTTP 409 conflict means the role already exist with the user so can't add again
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        //here we just want to return a HTTP OK reponse with no body
        userService.addRoleToUser(form.getUsername(), form.getRole());
        return ResponseEntity.ok().build();
    }

    //json raw request to use: { "username":---- , "role":----} because of the RoleToUserForm class field names
    @PostMapping("role/deletefromuser")
    public ResponseEntity<?> deleteRoleFromUser(@RequestBody RoleToUserForm form) {
        if (userService.roleExistInUser(form.getUsername(), form.getRole())) {
            userService.deleteRoleFromUser(form.getUsername(), form.getRole());
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/token/refresh")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String authorizationHeader = request.getHeader(AUTHORIZATION);
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {

            try {
                //basically just removing the starting substring to get rid of Bearer infront of the JWT token
                String refreshToken = authorizationHeader.substring("Bearer ".length());

                //redoing it here to get the same algorithm to decode the Token that we used to create it with the same secret and algorithm
                Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
                JWTVerifier verifier = JWT.require(algorithm).build();
                DecodedJWT decodedJWT = verifier.verify(refreshToken);

                //getting the contents out of the Token ie username and it's authorities/roles
                String username = decodedJWT.getSubject();
                User user = userService.getUser(username);

              /*
        Access Token which expires quickly- 10 mins in our case, here we won't create a new refresh token, we'll just return the same refresh token that they gave us
         */
                String accessToken = JWT.create()
                        .withSubject(user.getUsername())
                        .withExpiresAt(new Date(System.currentTimeMillis() + 10 * 60 * 1000)) //expires in 10 mins
                        .withIssuer(request.getRequestURL().toString())
                        //:: is called a method reference introduced in java8, getAuthority is a method that returns a String
                        .withClaim("roles", user.getRoles().stream().map(Role::getName).collect(Collectors.toList()))
                        .sign(algorithm);


        /*
        Sending the JWT tokens in the response Header
         */
//        response.setHeader("access_token", accessToken);
//        response.setHeader("refresh_token", refreshToken);
        /*
        Sending JWT tokens in the response Body as JSON objects
         */
                Map<String, String> token = new HashMap<>();
                token.put("access_token", accessToken);
                token.put("refresh_token", refreshToken);
                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), token);


            } catch (Exception e) {
                log.error("Error : {}", e.getMessage());
                response.setHeader("Error", e.getMessage());
                response.setStatus(FORBIDDEN.value());
                Map<String, String> error = new HashMap<>();
                error.put("error_message", e.getMessage());
                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), error);

            }

        }else{
            throw new RuntimeException("Refresh Token is missing");

        }


    }

}

//making use of this utility class in the saveRoleToUser method of the controller
class RoleToUserForm {

    private String role;
    private String username;

    public String getRole() {
        return role;
    }

    public String getUsername() {
        return username;
    }
}
