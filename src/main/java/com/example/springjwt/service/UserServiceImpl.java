package com.example.springjwt.service;

import com.example.springjwt.model.Role;
import com.example.springjwt.model.RoleRepository;
import com.example.springjwt.model.User;
import com.example.springjwt.model.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service @Transactional @Slf4j
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserRepository userRepo;
    private final RoleRepository roleRepo;
    @Autowired
    PasswordEncoder passwordEncoder; // this works because we have a bean which returns a BcryptPasswordEncoder in the main method

    @Autowired
    public UserServiceImpl(UserRepository userRepo, RoleRepository roleRepo){
        this.userRepo = userRepo;
        this.roleRepo = roleRepo;
    }

   /*
   Method to implement for UserDetailsService
    */

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepo.findUserByUsername(username);
        if(user == null){
            log.error("No user with username : {} found", user.getUsername());
            throw new UsernameNotFoundException("user not found in the database");
        }else{
            log.info("user: {} found in the database", user.getUsername());
        }
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        user.getRoles().forEach(role -> {
            authorities.add(new SimpleGrantedAuthority(role.getName()));
        });

        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), authorities);
    }

    @Override
    public User saveUser(User user) {
       user.setPassword(passwordEncoder.encode(user.getPassword()));
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
