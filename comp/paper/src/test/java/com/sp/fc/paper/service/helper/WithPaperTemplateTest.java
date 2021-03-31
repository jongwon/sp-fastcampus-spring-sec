package com.sp.fc.paper.service.helper;

import com.sp.fc.paper.domain.Problem;
import com.sp.fc.paper.repository.PaperTemplateRepository;
import com.sp.fc.paper.repository.ProblemRepository;
import com.sp.fc.paper.service.PaperTemplateService;
import com.sp.fc.paper.service.ProblemService;
import com.sp.fc.user.domain.User;
import com.sp.fc.user.service.helper.WithUserTest;
import org.springframework.beans.factory.annotation.Autowired;

public class WithPaperTemplateTest extends WithUserTest {

    @Autowired
    protected PaperTemplateRepository paperTemplateRepository;
    @Autowired
    protected ProblemRepository problemRepository;

    protected PaperTemplateService paperTemplateService;
    protected PaperTemplateTestHelper paperTemplateTestHelper;
    protected ProblemService problemService;
    protected User teacher;

    protected void preparePaperTemplate(){
        this.problemRepository.deleteAll();
        this.paperTemplateRepository.deleteAll();
        prepareUserServices();

        this.problemService = new ProblemService(problemRepository);
        this.paperTemplateService = new PaperTemplateService(paperTemplateRepository, problemService);
        this.paperTemplateTestHelper = new PaperTemplateTestHelper(this.paperTemplateService);

        this.teacher = this.userTestHelper.createTeacher(school, "선생님1");
    }

    protected Problem problem(long ptId, String content, String answer){
        return Problem.builder().paperTemplateId(ptId)
                .content(content).answer(answer).build();
    }

}
