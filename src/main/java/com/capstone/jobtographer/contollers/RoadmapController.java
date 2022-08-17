package com.capstone.jobtographer.contollers;

import com.capstone.jobtographer.models.AppUser;
import com.capstone.jobtographer.models.Roadmap;
import com.capstone.jobtographer.models.UserWithRoles;
import com.capstone.jobtographer.repositories.RoadmapRepository;
import com.capstone.jobtographer.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.method.P;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
    @GetMapping("/roadmaps/{id}")
    public String RoadmapShow(Model model, @PathVariable long id){
        model.addAttribute("roadmap",roadmapsDao.getById(id));
        return "roadmaps/roadmaps_show";
    }

    @GetMapping("/delete/roadmaps/{id}")
    public String deleteRoadmaps(@PathVariable long id){
        roadmapsDao.deleteById(id);
        return "redirect:/roadmaps";
    }
    @GetMapping("/update/roadmaps/{id}")
    public String updateRoadmaps(Model model, @PathVariable long id){
        model.addAttribute("roadmap", roadmapsDao.getById(id));

        return "roadmaps/update_roadmaps";
    }

    @PostMapping("/update/roadmaps/{id}")
    public String updateRoadmaps(@ModelAttribute Roadmap roadmap){
        roadmapsDao.save(roadmap);

        return "redirect:/roadmaps";
    }
}
