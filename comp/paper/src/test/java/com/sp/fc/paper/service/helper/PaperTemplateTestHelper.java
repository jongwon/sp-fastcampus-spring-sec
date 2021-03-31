package com.sp.fc.paper.service.helper;

import com.sp.fc.paper.domain.PaperTemplate;
import com.sp.fc.paper.domain.Problem;
import com.sp.fc.paper.service.PaperTemplateService;
import com.sp.fc.user.domain.User;
import lombok.RequiredArgsConstructor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@RequiredArgsConstructor
public class PaperTemplateTestHelper {

    private final PaperTemplateService paperTemplateService;

    public PaperTemplate createPaperTemplate(User teacher, String paperName){
        PaperTemplate paperTemplate = PaperTemplate.builder()
                .name(paperName)
                .creator(teacher)
                .build();
        return paperTemplateService.save(paperTemplate);
    }

    public Problem addProblem(long paperTemplateId, Problem problem){
        return paperTemplateService.addProblem(paperTemplateId, problem);
    }

    public static void assertPaperTemplate(PaperTemplate pt, User user, String paperName) {
        assertNotNull(pt.getPaperTemplateId());
        assertNotNull(pt.getCreated());
        assertNotNull(pt.getUpdated());
        assertEquals(paperName, pt.getName());
        assertEquals(user.getUserId(), pt.getCreator().getUserId());
    }

}