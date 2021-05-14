package com.sp.fc.site.study.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value="/study")
public class StudyController {


    @GetMapping({"", "/"})
    public String index(){

        return "/study/index";
    }

    @GetMapping("/signup")
    public String signUp(){

        return "/study/signup";
    }

}
