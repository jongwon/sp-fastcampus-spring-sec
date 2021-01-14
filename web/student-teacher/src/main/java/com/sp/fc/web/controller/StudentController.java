package com.sp.fc.web.controller;


import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/student")
public class StudentController {

    @PreAuthorize("hasAnyAuthority('ROLE_STUDENT')")
    @GetMapping("/main")
    public String main(){
        return "StudentMain";
    }

}
