package com.sp.fc.site.manager.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value="/manager")
public class ManagerController {


    @GetMapping({"", "/"})
    public String index(){

        return "/manager/index";
    }

}
