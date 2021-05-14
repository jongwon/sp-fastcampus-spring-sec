package com.sp.fc.site.manager.controller;

import com.sp.fc.site.manager.controller.vo.StudyData;
import com.sp.fc.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/manager/study")
@RequiredArgsConstructor
public class StudyMngController {

    private final UserService userService;

    @GetMapping("/list")
    public String list(
            @RequestParam(value="pageNum", defaultValue = "1") Integer pageNum,
            @RequestParam(value="size", defaultValue = "10") Integer size,
            Model model
    ){
        model.addAttribute("menu", "study");
        Page<StudyData> studyList = userService.listStudents(pageNum, size)
                .map(s->new StudyData(s.getSchool().getName(), s.getUserId(), s.getName(), s.getEmail(), s.getGrade()));
        model.addAttribute("page", studyList);
        return "manager/study/list.html";
    }


}
