package com.example.springjwt.service;

import com.example.springjwt.model.Role;
import com.example.springjwt.model.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {
    User saveUser(User user);
    Role saveRole(Role role);
    void addRoleToUser(String username, String roleName);
    User getUser(String username);
    List<User> getUsers();
    User deleteUser(String username);
    void deleteRoleFromUser(String username, String roleName);
    Boolean roleExistInUser(String username, String role);
}
