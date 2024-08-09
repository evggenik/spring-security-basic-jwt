package com.evggenn.spring_security_basic_jwt.repository;

import com.evggenn.spring_security_basic_jwt.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends JpaRepository<User, Integer> {

    User findByName(String username);

}
