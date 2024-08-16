package com.evggenn.spring_security_basic_jwt.service;

import com.evggenn.spring_security_basic_jwt.dto.UserMapper;
import com.evggenn.spring_security_basic_jwt.model.Role;
import com.evggenn.spring_security_basic_jwt.model.User;
import com.evggenn.spring_security_basic_jwt.repository.UserRepo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepo userRepo;

    @Mock
    private AuthenticationManager authManager;

    @Mock
    private JwtService jwtService;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserService userService;


    @Test
    void testGetAllUsers() {

        // Arrange
        Role userRole = new Role(1, "USER");
        Role adminRole = new Role(2, "ADMIN");
        Set<Role> roles = new HashSet<>(List.of(userRole, adminRole));
        User user1 = new User(1, "wasya", "qwerty1&", roles);
        User user2 = new User(2, "kolya", "qwerty1&", roles);
        List<User> users = Arrays.asList(user1, user2);

        when(userRepo.findAll()).thenReturn(users);

        // Act
        List<User> result = userService.getAllUsers();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.contains(user1));
        assertTrue(result.contains(user2));
        assertTrue(result.containsAll(users));
        assertTrue(result.stream().allMatch(user -> !user.getRoles().isEmpty()));
        verify(userRepo).findAll();

    }

    @Test
    void testGetAllUsers_EmptyList() {
        // Arrange
        when(userRepo.findAll()).thenReturn(Collections.emptyList());

        // Act
        List<User> result = userService.getAllUsers();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(userRepo).findAll();
    }


}