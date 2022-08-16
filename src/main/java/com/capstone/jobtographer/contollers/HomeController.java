package com.capstone.jobtographer.contollers;

import com.capstone.jobtographer.models.AppUser;
import com.capstone.jobtographer.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@Controller
public class HomeController {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserRepository usersdao;


    @GetMapping("/")

    public String homePage() {
        return "index";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "/login";
    }

    @GetMapping("/profile")
    @ResponseBody
    public String profilePage() {
        return "<h1>Profile Page</h1>";
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

}
