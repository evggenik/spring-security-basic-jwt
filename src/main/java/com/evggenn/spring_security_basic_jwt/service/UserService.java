package com.evggenn.spring_security_basic_jwt.service;


import com.evggenn.spring_security_basic_jwt.dto.UserMapper;
import com.evggenn.spring_security_basic_jwt.dto.UserRequest;
import com.evggenn.spring_security_basic_jwt.dto.UserResponse;
import com.evggenn.spring_security_basic_jwt.exceptions.AuthenticationFailedException;
import com.evggenn.spring_security_basic_jwt.exceptions.UserAlreadyExistsException;
import com.evggenn.spring_security_basic_jwt.model.User;
import com.evggenn.spring_security_basic_jwt.repository.UserRepo;
import org.springframework.dao.DataIntegrityViolationException;
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

    private final BCryptPasswordEncoder encoder;

    public UserService(UserRepo userRepo, AuthenticationManager authManager, JwtService jwtService, UserMapper userMapper, BCryptPasswordEncoder encoder) {
        this.userRepo = userRepo;
        this.authManager = authManager;
        this.jwtService = jwtService;
        this.userMapper = userMapper;
        this.encoder = encoder;
    }

    public List<User> getAllUsers() {
        return userRepo.findAll();
    }

    public UserResponse register(UserRequest userRequest) {
        try {
            User user = userMapper.toUser(userRequest);
            user.setPassword(encoder.encode(user.getPassword()));
            User savedUser = userRepo.save(user);
            return userMapper.toUserResponse(savedUser);
        } catch (DataIntegrityViolationException e) {
            throw new UserAlreadyExistsException("User with this username already exists.");
        }

    }

    public String verify(User user) {
        Authentication authentication = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getName(), user.getPassword()));
        if (authentication.isAuthenticated()) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            return jwtService.generateToken(userDetails);
        }
        throw new AuthenticationFailedException("Login failed");

    }

}
