package com.sp.fc.site.teacher.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value="/teacher")
public class TeacherController {


    @GetMapping({"", "/"})
    public String index(){

        return "/teacher/index";
    }

    @GetMapping("/signup")
    public String signUp(){

        return "/teacher/signup";
    }

}
