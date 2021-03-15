package com.sp.fc.web.paper;

import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping(value="/paper")
@RestController
public class PaperController {

    private final PaperService paperService;

    public PaperController(PaperService paperService) {
        this.paperService = paperService;
    }

    @PostAuthorize("hasPermission(returnObject, 'READ')")
    @GetMapping("/{paperId}")
    public Paper getPaper(
            @AuthenticationPrincipal User user,
            @PathVariable Long paperId
    ){
        return paperService.getPaper(paperId).get();
    }

}
