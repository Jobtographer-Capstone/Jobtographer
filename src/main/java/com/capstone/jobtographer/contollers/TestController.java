package com.capstone.jobtographer.contollers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TestController {

    @GetMapping("/tester")
    public String testerHTML(){
        return "/CertTest";
    }


}
