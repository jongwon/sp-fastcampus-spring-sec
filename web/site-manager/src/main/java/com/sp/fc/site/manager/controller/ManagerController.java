package com.sp.fc.site.manager.controller;

import com.sp.fc.user.service.SchoolService;
import com.sp.fc.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value="/manager")
@RequiredArgsConstructor
public class ManagerController {

    private final SchoolService schoolService;
    private final UserService userService;

    @RequestMapping({"", "/"})
    public String index(Model model){

        model.addAttribute("schoolCount", schoolService.count());
        model.addAttribute("teacherCount", userService.countTeacher());
        model.addAttribute("studyCount", userService.countStudent());

        return "/manager/index";
    }

}
