package com.sp.fc.site.manager.controller;

import com.sp.fc.site.manager.controller.vo.TeacherData;
import com.sp.fc.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/manager/teacher")
@RequiredArgsConstructor
public class TeacherMngController {

    private final UserService userService;

    @GetMapping("/list")
    public String list(
            @RequestParam(value="pageNum", defaultValue = "1") Integer pageNum,
            @RequestParam(value="size", defaultValue = "10") Integer size,
            Model model
    ){
        model.addAttribute("menu", "teacher");
        Page<TeacherData> teacherList = userService.listTeachers(pageNum, size)
                .map(teacher->new TeacherData(teacher.getSchool().getName(),
                        teacher.getUserId(), teacher.getName(), teacher.getEmail(), 0L));
        teacherList.getContent().stream().forEach(data->{
            data.setStudentCount(userService.findTeacherStudentCount(data.getUserId()));
        });
        model.addAttribute("page", teacherList);
        return "manager/teacher/list.html";
    }



}
