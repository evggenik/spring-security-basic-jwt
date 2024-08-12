package com.evggenn.spring_security_basic_jwt.repository;

import com.evggenn.spring_security_basic_jwt.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepo extends JpaRepository<Role, Integer> {

    Role findByName(String name);

}
