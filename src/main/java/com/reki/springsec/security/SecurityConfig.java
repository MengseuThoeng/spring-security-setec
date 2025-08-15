package com.reki.springsec.security;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public UserDetailsService userDetailsService() {
        var user = User.withUsername("user")
                .password("{noop}123456")
                .roles("USER").build();

        var admin = User.withUsername("admin")
                .password("{noop}123}")
                .roles("ADMIN").build();


        return new InMemoryUserDetailsManager(user, admin);
    }



    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(
                        auth -> auth
//                                .requestMatchers("/", "/home").permitAll()
//                                .requestMatchers("/admin").hasRole("ADMIN")
                                .anyRequest().permitAll()

                );

        http.formLogin(Customizer.withDefaults()).logout(Customizer.withDefaults());

        return http.build();
    }

}
