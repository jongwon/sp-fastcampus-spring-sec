package com.sp.fc.web.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @RequestMapping("/greeting")
    public String greeting(){
        return "hello";
    }

}
