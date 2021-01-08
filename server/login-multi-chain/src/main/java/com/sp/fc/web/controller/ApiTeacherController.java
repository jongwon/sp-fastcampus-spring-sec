package com.sp.fc.web.controller;

import com.sp.fc.web.student.Student;
import com.sp.fc.web.student.StudentManager;
import com.sp.fc.web.teacher.Teacher;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;


@Controller
@RequestMapping("/api/teacher")
public class ApiTeacherController {

    private final StudentManager studentManager;

    public ApiTeacherController(StudentManager studentManager) {
        this.studentManager = studentManager;
    }

    @ResponseBody
    @GetMapping("/students")
    public List<Student> main(@AuthenticationPrincipal Teacher teacher){
        return studentManager.getMyStudents(teacher.getId());
    }

}
