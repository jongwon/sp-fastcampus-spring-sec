package com.sp.fc.web.controller;

import com.sp.fc.web.student.StudentManager;
import com.sp.fc.web.teacher.Teacher;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/teacher")
public class TeacherController {

    private final StudentManager studentManager;

    public TeacherController(StudentManager studentManager) {
        this.studentManager = studentManager;
    }

    @PreAuthorize("hasAnyAuthority('ROLE_TEACHER')")
    @GetMapping("/main")
    public String main(@AuthenticationPrincipal Teacher teacher, Model model){
        teacher.setStudentList(studentManager.getMyStudents(teacher.getId()));
        model.addAttribute("studentList", teacher.getStudentList());
        return "TeacherMain";
    }



}
