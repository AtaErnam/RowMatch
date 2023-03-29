package com.example.RowMatch.service;


import com.example.RowMatch.Models.User;
import com.example.RowMatch.repository.userRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class userService {

    private final userRepository repository;

    @Autowired
    public userService(userRepository repository) {
        this.repository = repository;
    }

    // USER SERVICE
    public String createUserRequest(){
        User userReq = new User(1000,1);
        repository.save(userReq);
        return userReq.toString();
    }

    public ResponseEntity<String> updateLevelRequest(int id){
        User existingUser = repository.findById(id).orElse(null);
        if (existingUser == null){
            return ResponseEntity.status(400).body("User doesn't exist!!");
        }
        existingUser.setCoins(existingUser.getCoins() + 10000);
        existingUser.setLevel(existingUser.getLevel() + 1);

        repository.save(existingUser);
        return ResponseEntity.status(200).body(existingUser.toString());
    }

    // ADMIN SERVICE
    public User saveUser(User user){
        return  repository.save(user);
    }

    public List<User> getUsers(){
        return repository.findAll();
    }

    public User getUserbyId(int id){
        return repository.findById(id).orElseThrow(() -> new EntityNotFoundException(String.format("User doesnt exist for id = %s!", id)));
    }

    public String deleteUser(int id){
        repository.deleteById(id);
        return "User with id:" + id + " is removed!";
    }

    public User updateUser(User user){
        User existingUser = repository.findById(user.getId()).orElseThrow(() -> new EntityNotFoundException(String.format("User doesnt exist for id = %s!", user.getId())));
        existingUser.setCoins(user.getCoins());
        existingUser.setLevel(user.getLevel());

        return repository.save(existingUser);
    }


}
