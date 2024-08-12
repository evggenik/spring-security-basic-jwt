package com.evggenn.spring_security_basic_jwt.service;

import com.evggenn.spring_security_basic_jwt.model.Role;
import com.evggenn.spring_security_basic_jwt.model.User;
import com.evggenn.spring_security_basic_jwt.repository.RoleRepo;
import com.evggenn.spring_security_basic_jwt.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private RoleRepo roleRepo;

    @Autowired
    private AuthenticationManager authManager;

    @Autowired
    private JwtService jwtService;


    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public List<User> getAllUsers() {
        return userRepo.findAll();
    }

    public User register(User user) {
        user.setPassword(encoder.encode(user.getPassword()));

        Set<Role> roles = new HashSet<>();

        for (Role role : user.getRoles()) {
            Role existingRole = roleRepo.findByName(role.getName());
            if (existingRole != null) {
                roles.add(existingRole);
            } else {
                throw new IllegalArgumentException("Role " + role.getName() + " does not exist");
            }
        }
        user.setRoles(roles);
        return userRepo.save(user);
    }

    public String verify(User user) {
        Authentication authentication = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getPassword(), user.getName()));
        if (authentication.isAuthenticated())
            return jwtService.generateToken(user.getName());
        return "Login failed";

    }

}
