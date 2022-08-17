package com.capstone.jobtographer.contollers;

import com.capstone.jobtographer.models.AppUser;
import com.capstone.jobtographer.models.Roadmap;
import com.capstone.jobtographer.models.UserWithRoles;
import com.capstone.jobtographer.repositories.RoadmapRepository;
import com.capstone.jobtographer.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.sql.SQLException;

@Controller
public class RoadmapController {
    @Autowired
    private RoadmapRepository roadmapsDao;

    @Autowired
    private UserRepository usersDao;

    @GetMapping("/create/roadmaps")
    public String createRoadmap(Model model) {
        model.addAttribute("roadmap", new Roadmap());
        return "roadmaps/create_roadmaps";
    }

    @PostMapping("/create/roadmaps")
    public String createRoadmap(@ModelAttribute Roadmap roadmap) {
        UserWithRoles userRole = (UserWithRoles) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        AppUser user = usersDao.findByUsername(userRole.getUsername());
        roadmap.setUser(user);
        try {
            roadmapsDao.save(roadmap);
        } catch (RuntimeException e) {
            return "redirect:/create/roadmaps";


        }
        System.out.println("complete!");
        return "redirect:/roadmaps";
    }

    @GetMapping("/roadmaps")
    public String roadmaps(Model model){
       UserWithRoles userRole =(UserWithRoles) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
       AppUser user = usersDao.findByUsername(userRole.getUsername());
        model.addAttribute("roadmaps",roadmapsDao.findByUser(user));
        return "roadmaps/roadmaps";
    }

    @GetMapping("/delete/roadmaps")
    public String deleteRoadmaps(){
        return "roadmaps/delete_roadmaps";
    }
    @GetMapping("/update/roadmaps")
    public String updateRoadmaps(){
        return "roadmaps/update_roadmaps";
    }
}
