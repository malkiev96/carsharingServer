package com.carsharing.service;

import com.carsharing.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;


public interface UserService extends UserDetailsService {

    void save(User user);
}
