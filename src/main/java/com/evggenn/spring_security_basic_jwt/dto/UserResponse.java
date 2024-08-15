package com.evggenn.spring_security_basic_jwt.dto;


import java.util.Set;

public record UserResponse(int id, String name, Set<String> roles) {
}
