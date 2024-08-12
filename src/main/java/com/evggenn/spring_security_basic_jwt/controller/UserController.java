package com.evggenn.spring_security_basic_jwt.controller;

import com.evggenn.spring_security_basic_jwt.model.User;
import com.evggenn.spring_security_basic_jwt.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public List<User> getUsers() {
        System.out.println("Roles of user: " + userService.getAllUsers().get(0));
        return userService.getAllUsers();
    }

    @PostMapping("/register")
    public User register(@RequestBody User user) {
        return userService.register(user);
    }

    @PostMapping("/login")
    public String login(@RequestBody User user) {
        System.out.println(user);
        return userService.verify(user);
    }


}
