package com.reki.springsec.controller;

import com.reki.springsec.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class IndexController {

    private final UserService userService;

    @GetMapping("/")
    public String index(){
        return "redirect:/home";
    }

    @GetMapping("/login")
    public String login(){
        return "login";
    }

    @GetMapping("/default")
    public String defaultPage(Authentication authentication){
        if ( authentication.getAuthorities().stream().anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN")) ) {
            return "redirect:/admin";
        }
        return "redirect:/home";
    }

//    @PreAuthorize("permitAll()")
    @GetMapping("/home")
    public String home(){
        return "home";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin")
    public String admin(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        return "admin";
    }

}
