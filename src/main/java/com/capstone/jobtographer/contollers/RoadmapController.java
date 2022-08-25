package com.capstone.jobtographer.contollers;

import com.capstone.jobtographer.models.*;
import com.capstone.jobtographer.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


import java.util.ArrayList;

import java.util.List;


@Controller
public class RoadmapController {
    @Autowired
    private RoadmapRepository roadmapsDao;
    @Autowired
    public UserCertsRepository userCertsDao;

    @Autowired
    private UserRepository usersDao;

    @Autowired
    private CertificationRepository certsDao;
    @Autowired
    private RoadmapsCertsRepository roadmapsCertsDao;


    @GetMapping("/create/roadmaps")
    public String createRoadmap(Model model) {
        model.addAttribute("roadmap", new Roadmap());
        return "roadmaps/create_roadmaps";
    }

    @PostMapping("/create/roadmaps")
    public String createRoadmap(Model model, @ModelAttribute Roadmap roadmap, @RequestParam(name = "title") String title, @RequestParam(name = "certs") String certsArr) {
        UserWithRoles userRole = (UserWithRoles) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        AppUser user = usersDao.findById(userRole.getId());
        roadmap.setUser(user);
        roadmap.setCareer(title);
        roadmapsDao.save(roadmap);
        long id = roadmap.getId();
        String[] certs = certsArr.split(",");

        List<Certification> certificationList = new ArrayList<>();
        for (String cert : certs) {
            System.out.println("Look !!! A CERT " + cert);
            Certification tableCert = certsDao.findCertificationByCertificationName(cert);
            if (tableCert == null) {
                Certification certification = new Certification();
                certification.setCertificationName(cert);
                certsDao.save(certification);
                certificationList.add(certification);
                RoadmapCert rc = new RoadmapCert();
                rc.setRoadmap_id(roadmap);
                rc.setCert_id(certification);
                roadmapsCertsDao.save(rc);

            } else {

                RoadmapCert rc = new RoadmapCert();
                rc.setRoadmap_id(roadmap);
                rc.setCert_id(tableCert);
                roadmapsCertsDao.save(rc);
            }
        }
        model.addAttribute("certs", certificationList);


//       List<RoadmapCert> rmc = roadmapsCertsDao.findAllByRoadmap_id(roadmap.getId());
//        roadmap.setRoadmapCerts(rmc);


        return "redirect:/create/roadmaps/" + id;
    }

    @GetMapping("/create/roadmaps/{id}")
    public String createNewRoadmap(@PathVariable(name = "id") long id, Model model) {
        Roadmap rd = roadmapsDao.getById(id);
        model.addAttribute("roadmap", rd);


        return "/roadmaps/create_roadmaps";

    }

    @GetMapping("/roadmaps")
    public String roadmaps(Model model) {
        UserWithRoles userRole = (UserWithRoles) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        AppUser user = usersDao.findById(userRole.getId());
        model.addAttribute("roadmaps", roadmapsDao.findByUser(user));
        List<Roadmap> rmList = roadmapsDao.findByUser(user);
        for (Roadmap rm : rmList) {
            int have = 0;
            int need = 0;
            List<RoadmapCert> rcList = roadmapsCertsDao.findAllByRoadmap_id(rm.getId());
            for (RoadmapCert rc : rcList) {
                need += 1;
                List<UserCert> uc = userCertsDao.findAllByUser_id(user.getId());
                for (UserCert c : uc) {
                    if (c.getCert_id().getId().equals(rc.getCert_id().getId())) {
                        have += 1;
                    }
                }
            }

            double math = (double) have/need * 100;
            int progress =(int) math;

           rm.setProgress(progress);
           roadmapsDao.save(rm);

            System.out.println(need + " !!!!!");
            System.out.println("i have " + have);
            System.out.format("i am %d percent complete %n",progress);
            model.addAttribute("progress", progress);
        }

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
    public String test() {
        return "<h1>Test Page</h1>";
    }

    @PostMapping("/test")
    public String tester(@RequestParam(name = "company") String company, @RequestParam(name = "title") String title, @RequestParam(name = "outlook") String outlook, @RequestParam(name = "wages") String wages, @RequestParam(name = "certs") String certs) {
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
