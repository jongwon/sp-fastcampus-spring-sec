package com.sp.fc.site.teacher.controller;

import com.sp.fc.paper.domain.Paper;
import com.sp.fc.paper.domain.PaperTemplate;
import com.sp.fc.paper.domain.Problem;
import com.sp.fc.paper.service.PaperService;
import com.sp.fc.paper.service.PaperTemplateService;
import com.sp.fc.site.teacher.controller.vo.ProblemInput;
import com.sp.fc.user.domain.School;
import com.sp.fc.user.domain.User;
import com.sp.fc.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping(value="/teacher")
@RequiredArgsConstructor
public class TeacherController {

    private final UserService userService;
    private final PaperTemplateService paperTemplateService;
    private final PaperService paperService;

    @RequestMapping({"", "/"})
    public String  index(@AuthenticationPrincipal User user, Model model){

        // 학생수와 문제지 수
        model.addAttribute("studentCount", 1);
        model.addAttribute("paperTemplateCount", 1);
        return "/teacher/index";
    }

    @GetMapping("/signup")
    public String signUp(){
        return "/teacher/signup";
    }

    private User user(){
        return User.builder()
                .userId(1L)
                .name("홍길동")
                .email("hong@test.com")
                .grade("3")
                .enabled(true)
                .school(School.builder().schoolId(1L).name("테스트 학교").city("서울").build())
                .build();
    }

    private PaperTemplate paperTemplate(){
        return PaperTemplate.builder()
                .paperTemplateId(1L)
                .name("테스트 시험지")
                .creator(user())
                .userId(1L)
                .publishedCount(1)
                .build();
    }

    private List<Paper> paperList(){
        return List.of(Paper.builder()
                .name("테스트 시험지")
                .paperTemplateId(1L)
                .state(Paper.PaperState.START)
                .total(2)
                .paperId(1L)
                .studyUserId(1L)
                .user(user())
                .build());
    }

    @GetMapping("/study/list")
    public String studyList(@AuthenticationPrincipal User user, Model model){
        model.addAttribute("menu", "study");
        model.addAttribute("studyList", List.of(user()));
        return "/teacher/study/list.html";
    }

    /**
     * 시험을 본 학생과 보지 않은 학생 리스트와 결과... 리스트
     *
     * @param paperTemplateId
     * @param user
     * @param model
     * @return
     */
    @GetMapping("/study/results")
    public String studyResults(
            @RequestParam Long paperTemplateId,
            @AuthenticationPrincipal User user,
            Model model
    ){
        model.addAttribute("menu", "paper");

        model.addAttribute("template", paperTemplate());
        model.addAttribute("papers", paperList());
        return "/teacher/study/results.html";
    }

    @GetMapping("/paperTemplate/list")
    public String paperTemplateList(
            @RequestParam(value="pageNum", defaultValue = "1") Integer pageNum,
            @RequestParam(value="size", defaultValue = "10") Integer size,
            @AuthenticationPrincipal User user,
            Model model
    ){
        model.addAttribute("menu", "paper");
        model.addAttribute("page", new PageImpl(List.of(paperTemplate())));
        return "/teacher/paperTemplate/list.html";
    }

    @GetMapping("/paperTemplate/create")
    public String editPaperTemplateName(@AuthenticationPrincipal User user, Model model){

        return "/teacher/paperTemplate/create.html";
    }

    @PostMapping(value="/paperTemplate/create", consumes = {"application/x-www-form-urlencoded;charset=UTF-8", MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    public String createAndEditTemplate(@RequestParam String paperName, @AuthenticationPrincipal User user, Model model){
        model.addAttribute("template", paperTemplate());
        return "/teacher/paperTemplate/edit.html";
    }

    @GetMapping("/paperTemplate/edit")
    public String editPaperTemplate(
            @RequestParam Long paperTemplateId,
            @AuthenticationPrincipal User user,
            Model model
    ){
        model.addAttribute("template", paperTemplate());
        return "/teacher/paperTemplate/edit.html";
    }

    @PostMapping(value="/paperTemplate/problem/add", consumes = {"application/x-www-form-urlencoded;charset=UTF-8", MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    public String addProblemToPaperTemplate(
            ProblemInput problemInput,
            @AuthenticationPrincipal User user, Model model){
        model.addAttribute("template", paperTemplate());
        return "/teacher/paperTemplate/edit.html";
    }

    /**
     * 시험지 배포
     * @return
     */
    @GetMapping("/paperTemplate/publish")
    public String publishPaper(
            @RequestParam Long paperTemplateId,
            @AuthenticationPrincipal User user, Model model
    ){

        return "redirect:/teacher/paperTemplate/list.html";
    }
}
