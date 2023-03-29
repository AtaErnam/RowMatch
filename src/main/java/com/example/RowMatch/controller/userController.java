package com.example.RowMatch.controller;


import com.example.RowMatch.Models.User;
import com.example.RowMatch.service.userService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class userController {

    @Autowired
    private userService service;

    //USER CONTROLS

    @PostMapping("/createUserRequest")
    public String createUserRequest(){
        return service.createUserRequest();
    }

    @PutMapping ("/updateUserRequest/{id}")
    public ResponseEntity<String> updateUserRequest(@PathVariable int id){
        return service.updateLevelRequest(id);
    }

    // ADMIN CONTROLS

    @PostMapping("/addUser")
    public User addUser(@RequestBody User user){
        return service.saveUser(user);
    }

    @GetMapping("/users")
    public List<User> findAllUsers(){
        return service.getUsers();
    }

    @GetMapping("/user/{id}")
    public User findUser(@PathVariable int id){
        return service.getUserbyId(id);
    }

    @PutMapping("/updateUser")
    public User updateUser(@RequestBody User user){
        return service.updateUser(user);
    }

    @DeleteMapping("/deleteUser/{id}")
    public String deleteUser(@PathVariable int id){
        return service.deleteUser(id);
    }
}
