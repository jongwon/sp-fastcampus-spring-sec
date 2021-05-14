package com.sp.fc.site.teacher.controller;

import com.sp.fc.site.teacher.controller.vo.TeacherSignUpForm;
import com.sp.fc.user.domain.Authority;
import com.sp.fc.user.domain.User;
import com.sp.fc.user.service.SchoolService;
import com.sp.fc.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;


@Controller
@RequiredArgsConstructor
public class TeacherSignupController {


    private final SchoolService schoolService;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/signup/teacher")
    public String signUp(Model model){
        model.addAttribute("site", "study");
        model.addAttribute("cityList", schoolService.cities());
        return "/teacher/signup";
    }


    @PostMapping(value = "/signUp/teacher", consumes = {"application/x-www-form-urlencoded;charset=UTF-8", MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    public String singup(TeacherSignUpForm form, Model model){
        final User teacher = User.builder()
                .name(form.getName())
                .email(form.getEmail())
                .password(passwordEncoder.encode(form.getPassword()))
                .enabled(true)
                .build();
        schoolService.findSchool(form.getSchoolId()).ifPresent(school -> teacher.setSchool(school));
        User saved = userService.save(teacher);
        userService.addAuthority(saved.getUserId(), Authority.ROLE_TEACHER);
        model.addAttribute("site", "teacher");
        return "loginForm.html";
    }


}
