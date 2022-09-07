package com.capstone.jobtographer.contollers;

import com.capstone.jobtographer.models.*;

import com.capstone.jobtographer.repositories.*;
import com.capstone.jobtographer.services.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
    private RoadmapsCertsRepository roadmapsCertsDao;

    @Value("${CAREER_API_KEY}")
    private String CAREER_API_KEY;

    @Value("${USER_ID}")
    private String USER_ID;

    @Value("${FILESTACK_API_KEY}")
    private String FILESTACK_API_KEY;

    @Autowired
    private EmailService emailService;

    @GetMapping("/about")
    public String about() {return "about"; }

    @GetMapping("/")
    public String homePage(Model model) {
        model.addAttribute("CAREER_API_KEY", CAREER_API_KEY);
        model.addAttribute("USER_ID", USER_ID);
        return "index";
    }

    @GetMapping("/login")

    public String loginPage(Model model) {
        model.addAttribute("user", SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        return "index";
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
        if(roadmap != null){
            int have = 0;
            int need = 0;
            List<RoadmapCert> rcList = roadmapsCertsDao.findAllByRoadmap_id(roadmap.getId());
            for (RoadmapCert rc : rcList) {
                need += 1;
                List<UserCert> uc = userCertsDao.findAllByUser_id(user.getId());
                for (UserCert c : uc) {
                    if (c.getCert_id().getId().equals(rc.getCert_id().getId())) {
                        have += 1;
                    }
                }
            }

            double math = (double) have / need * 100;
            int progress = (int) math;

            roadmap.setProgress(progress);
            roadmapsDao.save(roadmap);
        }



        return "user/profile";

    }

    @PostMapping("/profile")
    public String profilePage(@RequestParam(name = "profileImage") String pI, Model model) {
        UserWithRoles loggedIn = (UserWithRoles) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        AppUser user = usersdao.findById(loggedIn.getId());
        model.addAttribute("FILESTACK_API_KEY", FILESTACK_API_KEY);
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
        return "register";
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
        model.addAttribute("FILESTACK_API_KEY", FILESTACK_API_KEY);
        model.addAttribute("user", usersdao.getById(loggedin.getId()));
        model.addAttribute("FILESTACK_API_KEY", FILESTACK_API_KEY);
        return "user/update_user";
    }

    @PostMapping("/update/user")
    public String updateAccount(@RequestParam(name = "password") String password, @ModelAttribute AppUser user) {
        user.setPassword(passwordEncoder.encode(password));
        usersdao.save(user);
        return "redirect:/profile";
    }
    @GetMapping("/search")
    public String search(){

        return "search-jobs";
    }



}
