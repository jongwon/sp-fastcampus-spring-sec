package com.sp.fc.site.study.controller;

import com.sp.fc.paper.domain.Paper;
import com.sp.fc.paper.domain.PaperAnswer;
import com.sp.fc.paper.domain.PaperTemplate;
import com.sp.fc.paper.domain.Problem;
import com.sp.fc.paper.service.PaperService;
import com.sp.fc.paper.service.PaperTemplateService;
import com.sp.fc.site.study.controller.vo.Answer;
import com.sp.fc.site.study.controller.vo.StudySignUpForm;
import com.sp.fc.user.domain.Authority;
import com.sp.fc.user.domain.User;
import com.sp.fc.user.service.SchoolService;
import com.sp.fc.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping(value="/study")
@RequiredArgsConstructor
public class StudyController {

    private final PaperTemplateService paperTemplateService;

    private final PaperService paperService;

    @RequestMapping({"", "/"})
    public String  index(@AuthenticationPrincipal User user, Model model){

        // 학생수와 문제지 수
        model.addAttribute("paperCount", paperService.countPapersByUserIng(user.getUserId()));
        model.addAttribute("resultCount", paperService.countPapersByUserResult(user.getUserId()));

        return "/study/index.html";
    }


    // 시험지 리스트
    @GetMapping("/papers")
    public String paperList(@AuthenticationPrincipal User user, Model model){
        model.addAttribute("menu", "paper");
        model.addAttribute("papers", paperService.getPapersByUserIng(user.getUserId()));
        return "/study/paper/papers.html";
    }

    @GetMapping("/results")
    public String results(
            @RequestParam(value="pageNum", defaultValue = "1") Integer pageNum,
            @RequestParam(value="size", defaultValue = "10") Integer size,
            @AuthenticationPrincipal User user, Model model
    ){
        model.addAttribute("menu", "result");
        model.addAttribute("page",
                paperService.getPapersByUserResult(user.getUserId(), pageNum, size)
        );
        return "/study/paper/results.html";
    }


    // 시험 보기
    @GetMapping(value="/paper/apply")
    public String applyPaper(@RequestParam Long paperId, @AuthenticationPrincipal User user, Model model){
        model.addAttribute("menu", "paper");
        Paper paper = paperService.findPaper(paperId).get();
        if(paper.getState() == Paper.PaperState.END){
            return "redirect:/study/paper/result?paperId="+paperId;
        }

        // 시험지에서 풀이를 가져온다.
        Map<Integer, PaperAnswer> answerMap = paper.answerMap();

        // 안푼 문제를 번호와 함껜 내려준다.
        PaperTemplate template = paperTemplateService.findById(paper.getPaperTemplateId()).orElseThrow(() -> new IllegalArgumentException(paper.getPaperTemplateId() + " 시험지가 존재하지 않습니다."));
        Optional<Problem> notAnsweredProblem = template.getProblemList().stream().filter(problem -> !answerMap.containsKey(problem.getIndexNum())).findFirst();

        model.addAttribute("paperId", paperId);
        model.addAttribute("paperName", paper.getName());
        if(notAnsweredProblem.isPresent()){
            model.addAttribute("problem", notAnsweredProblem.get());
            model.addAttribute("alldone", false);
        }else{
            model.addAttribute("alldone", true);
        }
        return "/study/paper/apply.html";
    }

    /**
     * TODO : 다른 사람이 풀수도 있음. 아이디를 확인해야 함.
     *
     * @return
     */
    // 정답 제출
    @PostMapping(value="/paper/answer", consumes = {"application/x-www-form-urlencoded;charset=UTF-8", MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    public String answer(Answer answer, @AuthenticationPrincipal User user, Model model){
        paperService.answer(answer.getPaperId(), answer.getProblemId(), answer.getIndexNum(), answer.getAnswer());
        return "redirect:/study/paper/apply?paperId="+answer.getPaperId();
    }

    // 시험 완료
    @GetMapping("/paper/done")
    public String donePaper(Long paperId){
        paperService.paperDone(paperId);
        return "redirect:/study/paper/result?paperId="+paperId;
    }

    // 결과 시험지 리스트
    @GetMapping("/paper/result")
    public String paperResult(Long paperId, @AuthenticationPrincipal User user, Model model){
        model.addAttribute("menu", "result");
        Paper paper = paperService.findPaper(paperId).orElseThrow(()->new IllegalArgumentException("시험지가 존재하지 않습니다."));
        model.addAttribute("paper", paper);
        return "/study/paper/result.html";
    }
}
