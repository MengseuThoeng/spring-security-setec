package com.reki.springsec.service;

import com.reki.springsec.domain.User;
import com.reki.springsec.domain.Role;
import com.reki.springsec.feature.user.UserRepository;
import com.reki.springsec.feature.role.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public User createUser(User user, List<Long> roleIds) {
        // Encode the password before saving
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        
        // Set roles if provided
        if (roleIds != null && !roleIds.isEmpty()) {
            List<Role> roles = roleRepository.findAllById(roleIds);
            user.setRoles(roles);
        }
        
        return userRepository.save(user);
    }

    @Override
    public User updateUser(Long id, User user, List<Long> roleIds) {
        user.setId(id);
        
        // Get the existing user to preserve the current password if not changed
        Optional<User> existingUserOpt = userRepository.findById(id);
        if (existingUserOpt.isPresent()) {
            User existingUser = existingUserOpt.get();
            
            // Only encode password if it's provided and different from existing
            if (user.getPassword() != null && !user.getPassword().trim().isEmpty()) {
                user.setPassword(passwordEncoder.encode(user.getPassword()));
            } else {
                // Keep the existing password if no new password provided
                user.setPassword(existingUser.getPassword());
            }
        }
        
        // Set roles if provided
        if (roleIds != null && !roleIds.isEmpty()) {
            List<Role> roles = roleRepository.findAllById(roleIds);
            user.setRoles(roles);
        }
        
        return userRepository.save(user);
    }

    @Override
    public void deleteUser(Long id, String currentUsername) {
        // Get the user to be deleted
        Optional<User> userToDelete = userRepository.findById(id);
        
        if (userToDelete.isPresent()) {
            User user = userToDelete.get();
            
            // Prevent self-deletion
            if (user.getUsername().equals(currentUsername)) {
                throw new IllegalArgumentException("You cannot delete your own account while logged in");
            }
            
            userRepository.deleteById(id);
        } else {
            throw new IllegalArgumentException("User not found with id: " + id);
        }
    }

    @Override
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }
}
