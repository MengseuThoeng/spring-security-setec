package com.reki.springsec.controller;

import com.reki.springsec.domain.User;
import com.reki.springsec.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin/users")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class AdminUserController {
    
    private final UserService userService;

    @GetMapping
    public String listUsers(Model model) {
        List<User> users = userService.getAllUsers();
        model.addAttribute("users", users);
        return "admin-users";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("roles", userService.getAllRoles());
        return "admin-user-form";
    }

    @PostMapping("/create")
    public String createUser(@ModelAttribute @Valid User user, 
                           @RequestParam(value = "roleIds", required = false) List<Long> roleIds,
                           BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("roles", userService.getAllRoles());
            return "admin-user-form";
        }
        
        userService.createUser(user, roleIds);
        return "redirect:/admin/users";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Optional<User> user = userService.getUserById(id);
        if (user.isPresent()) {
            model.addAttribute("user", user.get());
            model.addAttribute("roles", userService.getAllRoles());
            return "admin-user-form";
        }
        return "redirect:/admin/users";
    }

    @PostMapping("/edit/{id}")
    public String updateUser(@PathVariable Long id, 
                            @ModelAttribute @Valid User user, 
                            @RequestParam(value = "roleIds", required = false) List<Long> roleIds,
                            BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("roles", userService.getAllRoles());
            return "admin-user-form";
        }
        
        userService.updateUser(id, user, roleIds);
        return "redirect:/admin/users";
    }

    @PostMapping("/delete/{id}")
    public String deleteUser(@PathVariable Long id, Authentication authentication, 
                            RedirectAttributes redirectAttributes) {
        try {
            String currentUsername = authentication.getName();
            userService.deleteUser(id, currentUsername);
            redirectAttributes.addFlashAttribute("successMessage", "User deleted successfully!");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/admin/users";
    }
}
