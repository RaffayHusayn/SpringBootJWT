package com.example.springjwt.service;

import com.example.springjwt.model.Role;
import com.example.springjwt.model.RoleRepository;
import com.example.springjwt.model.User;
import com.example.springjwt.model.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import javax.transaction.Transactional;
import java.util.List;

@Service @Transactional @Slf4j
public class UserServiceImpl implements UserService{

    private final UserRepository userRepo;
    private final RoleRepository roleRepo;

    @Autowired
    public UserServiceImpl(UserRepository userRepo, RoleRepository roleRepo){
        this.userRepo = userRepo;
        this.roleRepo = roleRepo;
    }

    @Override
    public User saveUser(User user) {
       userRepo.save(user);
       log.info("user {} saved", user.getUsername());
       return user;
    }

    @Override
    public Role saveRole(Role role) {
        roleRepo.save(role);
        log.info("role {} saved", role.getName());
        return role;
    }

    @Override
    public void addRoleToUser(String username, String roleName) {
        User user = userRepo.findUserByUsername(username);
        Role role = roleRepo.findRoleByName(roleName);
        if(user != null && role != null) {
            user.getRoles().add(role);
            log.info("role {} added to user {}", role.getName(), user.getUsername());
        }
    }

    @Override
    public User getUser(String username) {
       User user = null;
       if(userRepo.existsByUsername(username)){
           user = userRepo.findUserByUsername(username);
           log.info("fetching user {}", user.getUsername());
           return user;
       }else {
           log.info("this user doesn't exist");
           return user;
       }

    }

    @Override
    public List<User> getUsers() {
        List<User> userList = userRepo.findAll();
        log.info("fetching all users");
        return userList;
    }

    @Override
    public User deleteUser(String username) {
        User user = null;
      if(userRepo.existsByUsername(username)){
         user = userRepo.findUserByUsername(username);
         userRepo.delete(user);
         log.info("deleting user {} from the database", user.getUsername());
         return user;
      }else{
          log.info("user you want to delete doesn't exist");
          return user;
      }

    }

    @Override
    public void deleteRoleFromUser(String username, String roleName) {
        User user = userRepo.findUserByUsername(username);
        Role role = roleRepo.findRoleByName(roleName);
        if(user != null && role != null){
            user.getRoles().remove(role);
        }
    }

    @Override
    public Boolean roleExistInUser(String username, String role) {
        User user = userRepo.findUserByUsername(username);
        Role rolename = roleRepo.findRoleByName(role);
        if(user.getRoles().contains(rolename)){
            return true;
        }
        return false;
    }
}
