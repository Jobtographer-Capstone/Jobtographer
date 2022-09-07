package com.capstone.jobtographer.contollers;

import com.capstone.jobtographer.models.*;
import com.capstone.jobtographer.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


import java.sql.Date;
import java.time.YearMonth;
import java.util.ArrayList;

import java.util.Collection;
import java.util.Collections;
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

    @Value("${USER_ID}")
    private String USER_ID;

    @Value("${CAREER_API_KEY}")
    private String CAREER_API_KEY;


    @GetMapping("/create/roadmaps")
    public String createRoadmap(Model model) {
        model.addAttribute("roadmap", new Roadmap());
        model.addAttribute("USER_ID", USER_ID);
        model.addAttribute("CAREER_API_KEY", CAREER_API_KEY);
        return "roadmaps/create_roadmaps";
    }

    @PostMapping("/create/roadmaps")
    public String createRoadmap(Model model, @ModelAttribute Roadmap roadmap, @RequestParam(name = "title") String title, @RequestParam(name = "certs") String certsArr) {
        UserWithRoles userRole = (UserWithRoles) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        AppUser user = usersDao.findById(userRole.getId());
        roadmap.setUser(user);
//        System.out.println(certsArr);
//        System.out.println("------------------");
        roadmap.setCareer(title);
        roadmapsDao.save(roadmap);
        long id = roadmap.getId();
        String[] certs = certsArr.split(",");
//        System.out.println(certsArr);

        List<Certification> certificationList = new ArrayList<>();
        for (String cert : certs) {
//            System.out.println("Look !!! A CERT " + cert);
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


        return "redirect:/create/roadmaps/" + id;
    }

    @GetMapping("/create/roadmaps/{id}")
    public String createNewRoadmap(@PathVariable(name = "id") long id, Model model) {
        Roadmap rd = roadmapsDao.getById(id);
        model.addAttribute("roadmap", rd);
        List<RoadmapCert> timeline = roadmapsCertsDao.findAllByRoadmap_idOrderByExpectedDateAsc(id);
//        List<YearMonth> rcDates = new ArrayList<>();
//        for(RoadmapCert rc : timeline){
//            if(rc.getExpectedDate() != null) {
//                YearMonth rcDate = YearMonth.parse(rc.getExpectedDate());
//                rcDates.add(rcDate);
//            }
//        }
//      Collections.sort();
//        System.out.println(rcDates);
//        Collections.sort(timeline)
        for(RoadmapCert cert : timeline){
            System.out.println(cert.getExpectedDate());
        }
//        System.out.println(timeline.toString());
        model.addAttribute("timeline", timeline);



        return "roadmaps/create_roadmaps";

    }

    @PostMapping("/create/roadmaps/{id}")
    public String addDate(@PathVariable(name = "id") long id, @RequestParam(name = "cert-id") long certId, @RequestParam(name = "expected")Date date) {

        RoadmapCert cert = roadmapsCertsDao.getById(certId);
//        YearMonth cDate = YearMonth.parse(date);
        cert.setExpectedDate(date);
//        System.out.println(cert.getExpectedDate());
        roadmapsCertsDao.save(cert);
//        System.out.println("Did it save ?");

        return "redirect:/create/roadmaps/{id}";
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

            double math = (double) have / need * 100;
            int progress = (int) math;

            rm.setProgress(progress);
            roadmapsDao.save(rm);
            model.addAttribute("progress", progress);

//            System.out.println(need + " !!!!!");
//            System.out.println("i have " + have);
//            System.out.format("i am %d percent complete %n", progress);
            model.addAttribute("progress", progress);
        }

        return "roadmaps/roadmaps";
    }

    @GetMapping("/roadmaps/{id}")
    public String RoadmapShow(Model model, @PathVariable long id) {
        UserWithRoles user = (UserWithRoles) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        AppUser userLoggedIn = usersDao.findByUsername(user.getUsername());
        model.addAttribute("user",userLoggedIn);
        if (userLoggedIn.getRoadmaps().contains(roadmapsDao.getById(id))) {

            model.addAttribute("roadmap", roadmapsDao.getById(id));
            List<RoadmapCert> timeline = roadmapsCertsDao.findAllByRoadmap_idOrderByExpectedDateAsc(id);
            model.addAttribute("timeline", timeline);
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
}
