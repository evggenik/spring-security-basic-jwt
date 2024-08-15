package com.evggenn.spring_security_basic_jwt.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.Set;

public record UserRequest(int id,
          @NotBlank(message = "Username cannot be empty")
          @Size(min = 3, message = "Username must be at least 3 characters long")
          String name,
          @NotBlank(message = "Password cannot be blank")
          @Size(min = 8, message = "Password must be at least 8 characters long")
          @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[@#$%^&+=]).*$", message = "Password must contain at least one digit, one letter, and one special character")
          String password,
          @NotEmpty(message = "Roles cannot be empty")
          @Size(min = 1, message = "At least one role is required")
          Set<@Pattern(regexp = "^[A-Z]+$", message = "Role names must be in uppercase") String> roles) {
}
