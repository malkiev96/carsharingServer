package com.carsharing;

import com.carsharing.model.Role;
import com.carsharing.model.User;
import com.carsharing.repository.RoleRepository;
import com.carsharing.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.HashSet;

@Component
@AllArgsConstructor
public class UserInit implements CommandLineRunner {

    private final UserService userService;
    private final RoleRepository roleRepository;

    @Override
    public void run(String... args) throws Exception {

      /*  User user = User.builder()
                .email("mail@mail.ru")
                .enabled(true)
                .username("admin")
                .password("admin")
                .role(roleRepository.getOne(1))
                .build();

        userService.save(user);*/
    }
}
