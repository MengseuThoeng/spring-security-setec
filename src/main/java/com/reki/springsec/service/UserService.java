package com.reki.springsec.service;

import com.reki.springsec.domain.User;
import com.reki.springsec.domain.Role;

import java.util.List;
import java.util.Optional;

public interface UserService {
    List<User> getAllUsers();
    Optional<User> getUserById(Long id);
    User createUser(User user, List<Long> roleIds);
    User updateUser(Long id, User user, List<Long> roleIds);
    void deleteUser(Long id, String currentUsername);
    List<Role> getAllRoles();
}
