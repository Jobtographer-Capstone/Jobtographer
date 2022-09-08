package com.capstone.jobtographer.contollers;

import com.capstone.jobtographer.models.AppUser;
import com.capstone.jobtographer.models.Certification;
import com.capstone.jobtographer.models.UserCert;
import com.capstone.jobtographer.models.UserWithRoles;
import com.capstone.jobtographer.repositories.CertificationRepository;
import com.capstone.jobtographer.repositories.UserCertsRepository;
import com.capstone.jobtographer.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;

@Controller
public class CertificationController {
    @Autowired
    private CertificationRepository certsDao;
    @Autowired
    private UserCertsRepository userCertsDoa;
    @Autowired
    private UserRepository usersDoa;


    @Value("${USER_ID}")
    private String USER_ID;

    @Value("${CAREER_API_KEY}")
    private String CAREER_API_KEY;


    @GetMapping("/certifications")
    public String allCerts(Model model) {
        UserWithRoles loggedIn = (UserWithRoles) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        AppUser user = usersDoa.findById(loggedIn.getId());
        model.addAttribute("certs", userCertsDoa.findAllByUser_id(user.getId()));
        return "certification/all_certs";
    }

    @GetMapping("/search/certification")
    public String addCert(Model model) {
        model.addAttribute("CAREER_API_KEY",CAREER_API_KEY);
        model.addAttribute("USER_ID", USER_ID);
        model.addAttribute("cert", new Certification());
        return "certification/certs";
    }

    @PostMapping("/search/certification")
    public String addCert(@ModelAttribute Certification cert, @RequestParam(name = "expDate") Date expDate) {
        UserWithRoles loggedIn = (UserWithRoles) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        AppUser user = usersDoa.findById(loggedIn.getId());
        Certification tableCert = certsDao.findCertificationByCertificationName(cert.getCertificationName());
        if (tableCert == null) {
            certsDao.save(cert);
            userCertsDoa.save(new UserCert(user, cert, expDate));
        } else {
            userCertsDoa.save(new UserCert(user, tableCert, expDate));
        }
        return "redirect:/certifications";
    }

    @GetMapping("/delete/certification/{id}")
    public String deleteCert(@PathVariable(name = "id") long id) {
        UserWithRoles loggedIn = (UserWithRoles) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        AppUser user = usersDoa.findById(loggedIn.getId());
        if (userCertsDoa.getById(id).getUser_id().equals(user)) {
            userCertsDoa.delete(userCertsDoa.getById(id));
        }
        return "redirect:/certifications";
    }
}