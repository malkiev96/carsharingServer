package com.carsharing.service;

import com.carsharing.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;


public interface UserService extends UserDetailsService {

    User getUserByUsername(String username);
    void save(User user);
}
