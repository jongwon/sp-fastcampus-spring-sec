package com.sp.fc.site.teacher.controller;

import com.sp.fc.paper.domain.Paper;
import com.sp.fc.paper.domain.PaperTemplate;
import com.sp.fc.paper.domain.Problem;
import com.sp.fc.paper.service.PaperService;
import com.sp.fc.paper.service.PaperTemplateService;
import com.sp.fc.site.teacher.controller.vo.ProblemInput;
import com.sp.fc.user.domain.Authority;
import com.sp.fc.user.domain.School;
import com.sp.fc.user.domain.User;
import com.sp.fc.user.service.SchoolService;
import com.sp.fc.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    public String index(@AuthenticationPrincipal User user, Model model){

        // 학생수와 문제지 수
        model.addAttribute("studentCount", userService.findTeacherStudentCount(user.getUserId()));
        model.addAttribute("paperTemplateCount", paperTemplateService.countByUserId(user.getUserId()));

        return "/teacher/index";
    }

    @GetMapping("/study/list")
    public String studyList(@AuthenticationPrincipal User user, Model model){
        model.addAttribute("menu", "study");
        List<User> studyList = userService.findTeacherStudentList(user.getUserId());
        model.addAttribute("studyList", studyList);
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

        return paperTemplateService.findProblemTemplate(paperTemplateId).map(paperTemplate->{
            List<Paper> papers = paperService.getPapers(paperTemplateId);
            Map<Long, User> userMap = userService.getUsers(papers.stream().map(p->p.getStudyUserId()).collect(Collectors.toList()));
            papers.stream().forEach(paper -> paper.setUser(userMap.get(paper.getStudyUserId())));
            model.addAttribute("template", paperTemplateService.findById(paperTemplateId).get());
            model.addAttribute("papers", papers);
            return "/teacher/study/results.html";
        }).orElseThrow(()->new AccessDeniedException("시험지가 존재하지 않습니다."));

    }

    @GetMapping("/paperTemplate/list")
    public String paperTemplateList(
            @RequestParam(value="pageNum", defaultValue = "1") Integer pageNum,
            @RequestParam(value="size", defaultValue = "10") Integer size,
            @AuthenticationPrincipal User user,
            Model model
    ){
        model.addAttribute("menu", "paper");
        Page<PaperTemplate> templateList = paperTemplateService.findByTeacherId(user.getUserId(), pageNum, size)
                .map(template->{
                    return template;
                });
        model.addAttribute("page", templateList);
        return "/teacher/paperTemplate/list.html";
    }

    @GetMapping("/paperTemplate/create")
    public String editPaperTemplateName(@AuthenticationPrincipal User user, Model model){

        return "/teacher/paperTemplate/create.html";
    }

    @PostMapping(value="/paperTemplate/create", consumes = {"application/x-www-form-urlencoded;charset=UTF-8", MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    public String createAndEditTemplate(@RequestParam String paperName, @AuthenticationPrincipal User user, Model model){
        PaperTemplate paperTemplate = PaperTemplate.builder()
                .name(paperName)
                .userId(user.getUserId())
                .build();
        paperTemplate = paperTemplateService.save(paperTemplate);
        model.addAttribute("template", paperTemplate);
        return "/teacher/paperTemplate/edit.html";
    }

    @GetMapping("/paperTemplate/edit")
    public String editPaperTemplate(
            @RequestParam Long paperTemplateId,
            @AuthenticationPrincipal User user,
            Model model
    ){
        PaperTemplate paperTemplate = paperTemplateService.findProblemTemplate(paperTemplateId).orElseThrow(
                ()->new IllegalArgumentException(paperTemplateId + " 시험지 템플릿이 존재하지 않음"));
        model.addAttribute("template", paperTemplate);
        return "/teacher/paperTemplate/edit.html";
    }

    @PostMapping(value="/paperTemplate/problem/add", consumes = {"application/x-www-form-urlencoded;charset=UTF-8", MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    public String addProblemToPaperTemplate(
            ProblemInput problemInput,
            @AuthenticationPrincipal User user, Model model){
        Problem p = Problem.builder().paperTemplateId(problemInput.getPaperTemplateId())
                .content(problemInput.getContent()).answer(problemInput.getAnswer())
                .build();
        paperTemplateService.addProblem(problemInput.getPaperTemplateId(), p);
        PaperTemplate paperTemplate = paperTemplateService.findProblemTemplate(problemInput.getPaperTemplateId()).orElseThrow(
                ()->new IllegalArgumentException(problemInput.getPaperTemplateId() + " 시험지 템플릿이 존재하지 않음"));
        model.addAttribute("template", paperTemplate);
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
        List<User> studyList = userService.findTeacherStudentList(user.getUserId());
        paperService.publishPaper(paperTemplateId, studyList.stream().map(u->u.getUserId()).collect(Collectors.toList()));
        return "redirect:/teacher/paperTemplate/list";
    }
}
