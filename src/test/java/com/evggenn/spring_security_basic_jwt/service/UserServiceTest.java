package com.evggenn.spring_security_basic_jwt.service;

import com.evggenn.spring_security_basic_jwt.dto.UserMapper;
import com.evggenn.spring_security_basic_jwt.dto.UserRequest;
import com.evggenn.spring_security_basic_jwt.dto.UserResponse;
import com.evggenn.spring_security_basic_jwt.exceptions.UserAlreadyExistsException;
import com.evggenn.spring_security_basic_jwt.model.Role;
import com.evggenn.spring_security_basic_jwt.model.User;
import com.evggenn.spring_security_basic_jwt.repository.UserRepo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private BCryptPasswordEncoder encoder;

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
    void testGetAllUsersEmptyList() {
        // Arrange
        when(userRepo.findAll()).thenReturn(Collections.emptyList());

        // Act
        List<User> result = userService.getAllUsers();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(userRepo).findAll();
    }

    @Test
    void testRegister() {

        // Arrange
        Role userRole = new Role(1, "USER");
        Role adminRole = new Role(2, "ADMIN");
        Set<Role> roles = new HashSet<>(List.of(userRole, adminRole));
        Set<String> requestRoles = new HashSet<>(List.of("ADMIN", "USER"));
        Set<String> responseRoles = new HashSet<>(List.of("ADMIN", "USER"));

        UserRequest userRequest = new UserRequest("wasya", "wasya", requestRoles);
        User unsavedUser = new User(null, "wasya", "wasya", roles);

        User savedUser = new User(1, "wasya", "$2a$10$NOfQvBiuUtQeASm/AfIC8.o/7GseVaNrGrIAFUU407xoyvo8Lhbhe", roles);
        when(userMapper.toUser(userRequest)).thenReturn(unsavedUser);
        when(encoder.encode("wasya")).thenReturn("$2a$10$NOfQvBiuUtQeASm/AfIC8.o/7GseVaNrGrIAFUU407xoyvo8Lhbhe");

        when(userRepo.save(unsavedUser)).thenReturn(savedUser);
        when(userMapper.toUserResponse(savedUser)).thenReturn(new UserResponse(savedUser.getId(), savedUser.getName(), responseRoles));

        // Act
        UserResponse response = userService.register(userRequest);

        // Assert
        assertNotNull(response);
        assertEquals(savedUser.getName(), response.name());
        assertEquals(savedUser.getId(), response.id());
        assertEquals(responseRoles, response.roles());
    }

    @Test
    public void testRegisterUserAlreadyExistsException() {
        // Arrange
        UserRequest userRequest = Mockito.mock(UserRequest.class);

        User user = new User();

        when(userMapper.toUser(userRequest)).thenReturn(user);

        // Act
        when(userRepo.save(user)).thenThrow(DataIntegrityViolationException.class);

        // Assert
        assertThrows(UserAlreadyExistsException.class, () -> userService.register(userRequest));
    }


}