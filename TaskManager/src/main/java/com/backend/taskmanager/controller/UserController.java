package com.backend.taskmanager.controller;


import com.backend.taskmanager.entity.Achievement;
import com.backend.taskmanager.entity.UserAchievement;
import com.backend.taskmanager.entity.User;
import com.backend.taskmanager.service.UserService;
import com.backend.taskmanager.entity.AchievementCriteria;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;



@RestController
@RequestMapping("api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List <User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUsersById(@PathVariable Long id){
        return userService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping ("/email/{email}")
    public ResponseEntity<User> getUserByEmail(@PathVariable String email) {
        return userService.findByEmail(email)
                .map(ResponseEntity::ok)
                .orElseThrow(()-> new RuntimeException("user not found with email: " + email));
    }

    @GetMapping ("/username/{username}")
    public ResponseEntity<User> getUserByUsername(@PathVariable String username){
        return userService.findByUsername(username)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }




}
