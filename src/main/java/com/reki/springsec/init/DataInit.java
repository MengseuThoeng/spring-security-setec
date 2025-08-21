package com.reki.springsec.init;


import com.reki.springsec.domain.Role;
import com.reki.springsec.domain.User;
import com.reki.springsec.feature.role.RoleRepository;
import com.reki.springsec.feature.user.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DataInit {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @PostConstruct
    public void init() {


        if (roleRepository.count() == 0 && userRepository.count() == 0) {
            // Create roles
            Role userR = Role.builder()
                    .name("USER")
                    .build();
            Role adminR = Role.builder()
                    .name("ADMIN")
                    .build();

            // Save roles first
            roleRepository.saveAll(List.of(userR, adminR));

            // Now create users with the saved roles
            User user = User.builder()
                    .username("user")
                    .password(passwordEncoder.encode("123"))
                    .roles(List.of(userR))
                    .build();

            User admin = User.builder()
                    .username("admin")
                    .password(passwordEncoder.encode("admin"))
                    .roles(List.of(adminR))
                    .build();

            // Save users after roles are persisted
            userRepository.saveAll(List.of(user, admin));
        }
    }
}
