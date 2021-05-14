package com.sp.fc.site.study.controller;

import com.sp.fc.site.study.controller.vo.StudySignUpForm;
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
public class StudySignupController {

    private final SchoolService schoolService;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/signup/study")
    public String signUp(Model model){
        model.addAttribute("site", "study");
        model.addAttribute("cityList", schoolService.cities());
        return "/study/signup";
    }

    @PostMapping(value = "/signUp/study", consumes = {"application/x-www-form-urlencoded;charset=UTF-8", MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    public String singup(StudySignUpForm form, Model model){
        final User study = User.builder()
                .name(form.getName())
                .email(form.getEmail())
                .password(passwordEncoder.encode(form.getPassword()))
                .grade(form.getGrade())
                .enabled(true)
                .build();
        schoolService.findSchool(form.getSchoolId()).ifPresent(school -> study.setSchool(school));
        userService.findUser(form.getTeacherId()).ifPresent(teacher->study.setTeacher(teacher));

        User saved = userService.save(study);
        userService.addAuthority(saved.getUserId(), Authority.ROLE_STUDENT);
        model.addAttribute("site", "study");
        return "loginForm.html";
    }


}
