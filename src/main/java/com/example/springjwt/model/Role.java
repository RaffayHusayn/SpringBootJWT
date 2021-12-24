package com.example.springjwt.model;

import net.bytebuddy.dynamic.loading.InjectionClassLoader;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Role {
    @Id
//    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String name;

    /*
    constructors
     */

    public Role() {

    }

    public Role(long id, String name){
        this.id = id;
        this.name = name;
    }

    /*
    setters and getters
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

    @Override
    public String toString() {
        return "Role{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
