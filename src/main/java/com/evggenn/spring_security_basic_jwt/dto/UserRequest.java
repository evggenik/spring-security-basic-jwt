package com.evggenn.spring_security_basic_jwt.dto;


import java.util.Set;

public record UserRequest(int id, String name, String password, Set<String> roles) {
}
