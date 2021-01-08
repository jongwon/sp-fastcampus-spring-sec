package com.sp.fc.web.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/teacher")
public class TeacherController {

    @PreAuthorize("hasAnyAuthority('ROLE_TEACHER')")
    @GetMapping("/main")
    public String main(){
        return "TeacherMain";
    }


}
