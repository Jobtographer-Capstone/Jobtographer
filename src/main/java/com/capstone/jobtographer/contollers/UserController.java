package com.capstone.jobtographer.contollers;

import com.capstone.jobtographer.models.AppUser;
import com.capstone.jobtographer.models.UserWithRoles;
import com.capstone.jobtographer.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@Controller
public class UserController {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserRepository usersdao;


    @GetMapping("/")

    public String homePage() {
        return "index";
    }

    @GetMapping("/login")
    public String loginPage(Model model) {
        model.addAttribute("user", SecurityContextHolder.getContext().getAuthentication().getPrincipal());

        return "/login";
    }

    @GetMapping("/profile")
    public String profilePage() {

        return "/user/profile";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }

    @GetMapping("/register")
    public String registerPage() {
        return "/register";
    }

    @PostMapping("/register")
    public String registerPage(String username, String email, String password) {

        AppUser user = new AppUser(username, email, passwordEncoder.encode(password));
        usersdao.save(user);


        return "redirect:/login";
    }

    @GetMapping("/delete/user")
    public String deleteAccount() {
        UserWithRoles user = (UserWithRoles) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        usersdao.deleteById(user.getId());
        return "redirect:/login";
    }

    @GetMapping("/update/user")
    public String updateAccount(Model model) {
        UserWithRoles loggedin = (UserWithRoles) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        model.addAttribute("user", usersdao.getById(loggedin.getId()));
        return "user/update_user";
    }

    @PostMapping("/update/user")
    public String updateAccount(@RequestParam(name = "password") String password, @ModelAttribute AppUser user) {
        user.setPassword(passwordEncoder.encode(password));
        usersdao.save(user);
        return "redirect:/";
    }

}
