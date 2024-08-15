package com.evggenn.spring_security_basic_jwt.dto;

import com.evggenn.spring_security_basic_jwt.model.Role;
import com.evggenn.spring_security_basic_jwt.model.User;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class UserMapper {

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
        return roleNames.stream()
                .map((name->{
                    Role role = new Role(); //лучше использовать билдер?
                    role.setName(name);
                    return role;}
                ))
                .collect(Collectors.toSet());
    }

}
