package com.capstone.jobtographer.config;

import com.capstone.jobtographer.models.AppUser;
import com.capstone.jobtographer.repositories.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


// Setting security privileges
@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
private UserRepository usersdao;
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http

//                Login configuration
                .formLogin()
                .loginPage("/login")
//                Users home page
                .permitAll()
                .defaultSuccessUrl("/profile")
                .failureUrl("/error")
//                Anyone can go to the login page
//                .permitAll()
//                Pages that can be viewed without having to log in

                .and()
                .logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/")
                .and()
                .authorizeRequests()
                .antMatchers("/profile",
                        "/logout",
                        "/roadmaps",
                        "/create/roadmaps",
                        "/create/roadmaps/{id}",
                        "/roadmaps/{id}",
                        "/delete/roadmaps/{id}",
                        "/update/roadmaps/{id}",
                        "/update/user",
                        "/delete/user",
                        "/certifications",
                        "/search/certification",
                        "/delete/certification/{id}"
                )
                .authenticated()
                .and()
                .authorizeRequests()
//
                .antMatchers("/" , "/register" ,"/login")
                .permitAll()
                .and()
                .build();
    }


}
