package com.capstone.jobtographer.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


// Setting security privileges
@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

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
                        "/create/roadmaps",
                        "/roadmaps")
                .authenticated()
                .and()
                .authorizeRequests()
//
                .antMatchers("/")
                .permitAll()
                .and()
                .build();
    }


}
