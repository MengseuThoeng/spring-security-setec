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
            // Create and save roles first
            Role userRole = Role.builder()
                    .name("USER")
                    .build();
            Role adminRole = Role.builder()
                    .name("ADMIN")
                    .build();

            // Save roles and get persisted entities with IDs
            List<Role> savedRoles = roleRepository.saveAll(List.of(userRole, adminRole));
            Role savedUserRole = savedRoles.stream()
                    .filter(role -> "USER".equals(role.getName()))
                    .findFirst()
                    .orElse(userRole);
            Role savedAdminRole = savedRoles.stream()
                    .filter(role -> "ADMIN".equals(role.getName()))
                    .findFirst()
                    .orElse(adminRole);

            // Now create users with the persisted roles
            User user = User.builder()
                    .username("user")
                    .password(passwordEncoder.encode("123"))
                    .roles(List.of(savedUserRole))
                    .build();

            User admin = User.builder()
                    .username("admin")
                    .password(passwordEncoder.encode("admin"))
                    .roles(List.of(savedAdminRole))
                    .build();

            // Save users
            userRepository.saveAll(List.of(user, admin));
        }
    }
}
