package com.carsharing.service.impl;

import com.carsharing.model.Role;
import com.carsharing.model.User;
import com.carsharing.repository.RoleRepository;
import com.carsharing.repository.UserRepository;
import com.carsharing.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public void save(User user) {
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));

        userRepository.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.getUserByUsername(username);
        if (user != null) {
            GrantedAuthority authority = new SimpleGrantedAuthority(user.getRole().getName());
            List<GrantedAuthority> authorities = Collections.singletonList(authority);
            return buildUserForAuthentication(user, authorities);
        }
        else throw new UsernameNotFoundException(username);
    }

    private UserDetails buildUserForAuthentication(User user, List<GrantedAuthority> authorities) {
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), user.isEnabled(), true, true, true, authorities);
    }
}
