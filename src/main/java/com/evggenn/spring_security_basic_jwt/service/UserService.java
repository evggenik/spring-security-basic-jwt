package com.evggenn.spring_security_basic_jwt.service;


import com.evggenn.spring_security_basic_jwt.dto.UserMapper;
import com.evggenn.spring_security_basic_jwt.dto.UserRequest;
import com.evggenn.spring_security_basic_jwt.dto.UserResponse;
import com.evggenn.spring_security_basic_jwt.model.User;
import com.evggenn.spring_security_basic_jwt.repository.UserRepo;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;


import java.util.List;


@Service
public class UserService {

    private final UserRepo userRepo;
    private final AuthenticationManager authManager;
    private final JwtService jwtService;
    private final UserMapper userMapper;

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public UserService(UserRepo userRepo, AuthenticationManager authManager, JwtService jwtService, UserMapper userMapper) {
        this.userRepo = userRepo;
        this.authManager = authManager;
        this.jwtService = jwtService;
        this.userMapper = userMapper;
    }

    public List<User> getAllUsers() {
        return userRepo.findAll();
    }

    public UserResponse register(UserRequest userRequest) {
        User user = userMapper.toUser(userRequest);
        user.setPassword(encoder.encode(user.getPassword()));
        User savedUser = userRepo.save(user);
        return userMapper.toUserResponse(savedUser);
    }

    public String verify(User user) {
        Authentication authentication = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getName(), user.getPassword()));
        if (authentication.isAuthenticated()) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            return jwtService.generateToken(userDetails);
        }
        return "Login failed";

    }

}
