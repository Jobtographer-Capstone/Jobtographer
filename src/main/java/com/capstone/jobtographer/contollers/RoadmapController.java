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
import org.springframework.web.bind.annotation.*;


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
        roadmapsDao.save(roadmap);
        return "redirect:/roadmaps";
    }

    @GetMapping("/roadmaps")
    public String roadmaps(Model model) {
        UserWithRoles userRole = (UserWithRoles) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        AppUser user = usersDao.findByUsername(userRole.getUsername());
        model.addAttribute("roadmaps", roadmapsDao.findByUser(user));
        return "roadmaps/roadmaps";
    }

    @GetMapping("/roadmaps/{id}")
    public String RoadmapShow(Model model, @PathVariable long id) {
        UserWithRoles user = (UserWithRoles) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        AppUser userLoggedIn = usersDao.findByUsername(user.getUsername());
        if (userLoggedIn.getRoadmaps().contains(roadmapsDao.getById(id))) {
            model.addAttribute("roadmap", roadmapsDao.getById(id));
            return "roadmaps/roadmaps_show";
        } else {
            return "redirect:/roadmaps";
        }
    }

    @GetMapping("/delete/roadmaps/{id}")
    public String deleteRoadmaps(@PathVariable long id) {
        UserWithRoles loggedIn = (UserWithRoles) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        AppUser user = usersDao.findById(loggedIn.getId());
        if (roadmapsDao.getById(id).getUser().getId().equals(user.getId())) {
            roadmapsDao.deleteById(id);
        }
        return "redirect:/roadmaps";
    }

    @GetMapping("/update/roadmaps/{id}")
    public String updateRoadmaps(Model model, @PathVariable long id) {
        UserWithRoles loggedIn = (UserWithRoles) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        AppUser user = usersDao.findById(loggedIn.getId());
        if (user.getRoadmaps().contains(roadmapsDao.getById(id))) {
            model.addAttribute("roadmap", roadmapsDao.getById(id));
            return "roadmaps/update_roadmaps";
        } else {
            return "redirect:/roadmaps";
        }
    }

    @PostMapping("/update/roadmaps/{id}")
    public String updateRoadmaps(@ModelAttribute Roadmap roadmap) {
        roadmapsDao.save(roadmap);

        return "redirect:/roadmaps";
    }

    @GetMapping("/test")
    @ResponseBody
    public String test(){
        return "<h1>Test Page</h1>";
    }

    @PostMapping("/test")
    public String tester(@RequestParam(name = "company") String company, @RequestParam(name = "title") String title, @RequestParam(name = "outlook") String outlook, @RequestParam(name = "wages") String wages, @RequestParam(name = "certs") String certs){
        if (company != null) {
            System.out.println("This is the company: " + company);
            System.out.println("This is the title: " + title);
            System.out.println("This is the outlook: " + outlook);
            System.out.println("This is the wages: " + wages);
            System.out.println("This is the certs: " + certs);
        }
        return "redirect:/test";
    }
}
