package com.sp.fc.site.manager.controller;


import com.sp.fc.user.domain.School;
import com.sp.fc.user.service.SchoolService;
import com.sp.fc.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
@RequestMapping("/manager/school")
@RequiredArgsConstructor
public class SchoolMngController {

    private final SchoolService schoolService;

    private final UserService userService;

    @GetMapping("/list")
    public String list(
            @RequestParam(value="pageNum", defaultValue = "1") Integer pageNum,
            @RequestParam(value="size", defaultValue = "10") Integer size,
            Model model
    ){
        model.addAttribute("menu", "school");
        Page<School> schoolList = schoolService.list(pageNum, size);
        schoolList.getContent().stream().forEach(school->{
            school.setTeacherCount(userService.countTeacher(school.getSchoolId()));
            school.setStudyCount(userService.countStudent(school.getSchoolId()));
        });
        model.addAttribute("page", schoolList);
        return "manager/school/list.html";
    }

    @GetMapping("/edit")
    public String list(
            @RequestParam(value="schoolId", required = false) Long schoolId,
            Model model
    ){
        model.addAttribute("menu", "school");
        if(schoolId != null) {
            model.addAttribute("school", schoolService.findSchool(schoolId).get());
        }else{
            model.addAttribute("school", School.builder().build());
        }
        return "manager/school/edit.html";
    }

    @PostMapping(value = "/save", consumes = {"application/x-www-form-urlencoded;charset=UTF-8", MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    public String save(School school){
        schoolService.save(school);
        return "redirect:/manager/school/list";
    }

}
