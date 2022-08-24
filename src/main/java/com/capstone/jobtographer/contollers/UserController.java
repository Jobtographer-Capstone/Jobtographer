package com.capstone.jobtographer.contollers;

import com.capstone.jobtographer.models.AppUser;

import com.capstone.jobtographer.models.Roadmap;
import com.capstone.jobtographer.models.UserCert;
import com.capstone.jobtographer.models.UserWithRoles;
import com.capstone.jobtographer.repositories.CertificationRepository;
import com.capstone.jobtographer.repositories.RoadmapRepository;
import com.capstone.jobtographer.repositories.UserCertsRepository;
import com.capstone.jobtographer.repositories.UserRepository;
import com.capstone.jobtographer.services.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class UserController {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserRepository usersdao;
    @Autowired
    private RoadmapRepository roadmapsDao;
    @Autowired
    private UserCertsRepository userCertsDao;
    @Autowired
    private CertificationRepository certsDao;

    @Autowired
    private EmailService emailService;

    @GetMapping("/about")
    public String about() {return "about"; }

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
    public String profilePage(Model model) {
        UserWithRoles loggedIn = (UserWithRoles) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        AppUser user = usersdao.findById(loggedIn.getId());
        model.addAttribute("user", user);
        List<UserCert> userCerts = userCertsDao.findAllByUser_id(user.getId());
        model.addAttribute("certs", userCerts);

        Roadmap roadmap = roadmapsDao.findTopByUserOrderByIdDesc(user);
        model.addAttribute("roadmap", roadmap);


        return "/user/profile";

    }

    @PostMapping("/profile")
    public String profilePage(@RequestParam(name = "profileImage") String pI) {
        UserWithRoles loggedIn = (UserWithRoles) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        AppUser user = usersdao.findById(loggedIn.getId());
        usersdao.updateImg(pI, user.getUsername());

        return "redirect:/update/user";
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
        if (usersdao.findByEmail(email) == null) {
            AppUser user = new AppUser(username, email, passwordEncoder.encode(password));
            usersdao.save(user);
            String subject = "Account creation";
            String body = "Welcome " + user.getUsername() + ", You have created a new account ! your login username is : " + user.getUsername();
            emailService.prepareAndSend(user, subject, body);
        }

        return "redirect:/login";
    }

    @GetMapping("/delete/user")
    public String deleteAccount(HttpSession session) {
        UserWithRoles user = (UserWithRoles) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        usersdao.deleteById(user.getId());
        session.invalidate();
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
        return "redirect:/profile";
    }


}
