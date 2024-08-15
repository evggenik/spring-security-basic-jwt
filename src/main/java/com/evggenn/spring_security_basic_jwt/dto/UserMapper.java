package com.evggenn.spring_security_basic_jwt.dto;

import com.evggenn.spring_security_basic_jwt.model.Role;
import com.evggenn.spring_security_basic_jwt.model.User;
import com.evggenn.spring_security_basic_jwt.repository.RoleRepo;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class UserMapper {

    private final RoleRepo roleRepo;

    public UserMapper(RoleRepo roleRepo) {
        this.roleRepo = roleRepo;
    }

    public UserResponse toUserResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getName(),
                mapRolesToStrings(user.getRoles()));
    }

    private Set<String> mapRolesToStrings(Set<Role> roles) {
        return roles.stream()
                .map(Role::getName)
                .collect(Collectors.toSet());
    }

    public User toUser(UserRequest request) {
        User user = new User();
        user.setName(request.name());
        user.setPassword(request.password());
        user.setRoles(mapStringsToRoles(request.roles()));
        return user;
    }

    private Set<Role> mapStringsToRoles(Set<String> roleNames) {
        Set<Role> roles = new HashSet<>();
        for (String name : roleNames) {
            Role role = roleRepo.findByName(name);
            if (role != null) {
                roles.add(role);
            } else {
                throw new IllegalArgumentException("Role " + name + " does not exist");
            }
        }
        return roles;
    }

}
