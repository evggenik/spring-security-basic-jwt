package com.evggenn.spring_security_basic_jwt.service;

import com.evggenn.spring_security_basic_jwt.model.User;
import com.evggenn.spring_security_basic_jwt.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepo userRepo;

    public List<User> getAllUsers() {
        return userRepo.findAll();
    }

}
