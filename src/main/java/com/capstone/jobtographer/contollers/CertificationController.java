package com.capstone.jobtographer.contollers;

import com.capstone.jobtographer.models.AppUser;
import com.capstone.jobtographer.models.Certification;
import com.capstone.jobtographer.models.UserCert;
import com.capstone.jobtographer.models.UserWithRoles;
import com.capstone.jobtographer.repositories.CertificationRepository;
import com.capstone.jobtographer.repositories.UserCertsRepository;
import com.capstone.jobtographer.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.util.List;


@Controller
public class CertificationController {
    @Autowired
    private CertificationRepository certsDao;
    @Autowired
    private UserCertsRepository userCertsDoa;
    @Autowired
    private UserRepository usersDoa;

    @GetMapping("/certifications")
    public String allCerts(Model model) {
        UserWithRoles loggedIn = (UserWithRoles) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        AppUser user = usersDoa.findById(loggedIn.getId());
        model.addAttribute("certs", userCertsDoa.findAllByUser_id(user.getId()));

        return "/certification/all_certs";
    }

    @GetMapping("/search/certification")
    public String addCert(Model model) {
        model.addAttribute("cert", new Certification());
        return "certs";
    }

    @PostMapping("/search/certification")
    public String addCert(@ModelAttribute Certification cert, @RequestParam(name = "expDate") Date expDate) {
        UserWithRoles loggedIn = (UserWithRoles) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        AppUser user = usersDoa.findById(loggedIn.getId());
        System.out.println("The user ID is " + user.getId());
        Certification tableCert = certsDao.findCertificationByCertificationName(cert.getCertificationName());
        System.out.println("this is a table cert" + tableCert.getCertificationName());

        if (tableCert == null) {
            certsDao.save(cert);
            userCertsDoa.save(new UserCert(user, cert, expDate));
        } else {

            userCertsDoa.save(new UserCert(user, tableCert, expDate));
        }





        return "redirect:/profile";
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
