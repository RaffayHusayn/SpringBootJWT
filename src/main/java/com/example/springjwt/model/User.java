package com.example.springjwt.model;


import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Entity
public class User {
    @Id
//    @GeneratedValue(strategy = AUTO)
    private long id;
    private String name;
    private String username;
    private String password;
    @ManyToMany(fetch= FetchType.EAGER )
    private List<Role> roles  = new ArrayList<>();

    /*
    Constructors
     */

    public User() {

    }
    public User(long id, String name, String username, String password, List<Role> roles){
        this.id = id;
        this.name = name;
        this.username = username;
        this.password = password;
        this.roles = roles;
    }

    /*
    Getters and Setters
     */

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", roles=" + roles +
                '}';
    }
}
