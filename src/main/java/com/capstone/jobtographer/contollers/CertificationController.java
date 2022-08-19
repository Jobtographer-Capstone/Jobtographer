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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class CertificationController {
    @Autowired
    private CertificationRepository certsDao;
    @Autowired
    private UserCertsRepository userCertsDoa;
    @Autowired
    private UserRepository usersDoa;

    @GetMapping("/search/certification")
    public String addCert(Model model) {
        model.addAttribute("cert", new Certification());
        return "certs";
    }

    @PostMapping("/search/certification")
    public String addCert(@ModelAttribute Certification cert) {
        UserWithRoles loggedIn = (UserWithRoles) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        AppUser user = usersDoa.findByUsername(loggedIn.getUsername());
        certsDao.save(cert);
        userCertsDoa.save(new UserCert(user.getId(), cert.getId()));
//        userCert.setUser_id(user.getId());
//        userCert.setCert_id(cert.getId());
//        userCertsDoa.save(userCert);

        return "redirect:/profile";
    }

}
