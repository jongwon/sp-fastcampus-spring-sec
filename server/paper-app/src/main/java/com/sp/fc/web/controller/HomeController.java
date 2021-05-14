package com.sp.fc.web.controller;


import com.sp.fc.user.domain.Authority;
import com.sp.fc.user.domain.School;
import com.sp.fc.user.domain.User;
import com.sp.fc.user.service.SchoolService;
import com.sp.fc.user.service.UserService;
import com.sp.fc.web.controller.vo.UserData;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final SchoolService schoolService;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    private RequestCache requestCache = new HttpSessionRequestCache();

    @GetMapping("/")
    public String home(Model model){
        return "index";
    }

    @ResponseBody
    @GetMapping(value="/schools")
    public List<School> getSchoolList(@RequestParam(value="city", required = true) String city){
        return schoolService.getSchoolList(city);
    }

    @ResponseBody
    @GetMapping(value="/teachers")
    public List<UserData> getTeacherList(@RequestParam(value="schoolId", required = true) Long schoolId){
        return userService.findBySchoolTeacherList(schoolId).stream()
                .map(user->new UserData(user.getUserId(), user.getName()))
                .collect(Collectors.toList());
    }

    @GetMapping("/login")
    public String login(
            @AuthenticationPrincipal User user,
            @RequestParam(value="site", required = false) String site,
            @RequestParam(value="error", defaultValue = "false") Boolean error,
            HttpServletRequest request,
            Model model
    ){
        if(user!=null && user.isEnabled()){
            if(user.getAuthorities().contains(Authority.ADMIN_AUTHORITY)){
                return "redirect:/manager";
            }else if(user.getAuthorities().contains(Authority.TEACHER_AUTHORITY)){
                return "redirect:/teacher";
            }else if(user.getAuthorities().contains(Authority.STUDENT_AUTHORITY)){
                return "redirect:/study";
            }
        }
        if(site == null) {
            SavedRequest savedRequest = requestCache.getRequest(request, null);
            if(savedRequest != null) {
                site = estimateSite(savedRequest.getRedirectUrl());
            }
        }
        model.addAttribute("error", error);
        model.addAttribute("site", site);

        return "loginForm";
    }

    private String estimateSite(String referer) {
        if(referer == null)
            return "study.html";
        try {
            URL url = new URL(referer);
            String path = url.getPath();
            if(path != null){
                if(path.startsWith("/teacher")) return "teacher";
                if(path.startsWith("/study")) return "study";
                if(path.startsWith("/manager")) return "manager";
            }
            String query = url.getQuery();
            if(query != null){
                if(path.startsWith("/site=teacher")) return "teacher";
                if(path.startsWith("/site=study")) return "study";
                if(path.startsWith("/site=manager")) return "manager";
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return "study.html";
    }

//    @PostMapping("/login")
//    public String loginPost(@RequestParam String site, Model model){
//        model.addAttribute("site", site);
//
//        return "redirect:/"+site;
//    }

    @GetMapping("/signup")
    public String signUp(
            @RequestParam String site,
            HttpServletRequest request
    ){
        if(site == null) {
            site = estimateSite(request.getParameter("referer"));
        }
        return "redirect:/"+site+"/signup";
    }

    @GetMapping("/access-denied")
    public String accessDenied(){
        return "/accessDenied";
    }

}
