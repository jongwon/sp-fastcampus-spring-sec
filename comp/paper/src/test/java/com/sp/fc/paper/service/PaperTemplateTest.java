package com.sp.fc.paper.service;

import com.sp.fc.paper.domain.PaperTemplate;
import com.sp.fc.paper.domain.Problem;
import com.sp.fc.paper.service.helper.PaperTemplateTestHelper;
import com.sp.fc.paper.service.helper.WithPaperTemplateTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.assertEquals;


@DataJpaTest
public class PaperTemplateTest extends WithPaperTemplateTest {

    private PaperTemplate template;

    @BeforeEach
    void before(){
        preparePaperTemplate();
        template = paperTemplateTestHelper.createPaperTemplate(teacher, "paper1");
    }

    private Problem problem(String content, String answer){
        return Problem.builder().paperTemplateId(template.getPaperTemplateId())
                .content(content)
                .answer(answer)
                .build();
    }

    @DisplayName("1. 시험지를 한개 만든다.")
    @Test
    void test_1(){
        assertEquals(1, paperTemplateRepository.count());
        PaperTemplateTestHelper.assertPaperTemplate(
                paperTemplateRepository.findAll().get(0), teacher, "paper1");
    }


    @DisplayName("2. 문제를 추가한다.")
    @Test
    void test_2() {
        paperTemplateTestHelper.addProblem(template.getPaperTemplateId(), problem("문제1", "정답1"));
        paperTemplateTestHelper.addProblem(template.getPaperTemplateId(), problem("문제2", "정답2"));
        paperTemplateTestHelper.addProblem(template.getPaperTemplateId(), problem("문제3", "정답3"));

        assertEquals(3, paperTemplateRepository.findAll().get(0).getProblemList().size());
    }

    @DisplayName("3. 문제를 삭제한다.")
    @Test
    void test_3() {
        paperTemplateTestHelper.addProblem(template.getPaperTemplateId(), problem("문제1", "정답1"));
        paperTemplateTestHelper.addProblem(template.getPaperTemplateId(), problem("문제2", "정답2"));
        paperTemplateTestHelper.addProblem(template.getPaperTemplateId(), problem("문제3", "정답3"));
        PaperTemplate paperTemplate = paperTemplateRepository.findAll().get(0);
        paperTemplateService.removeProblem(template.getPaperTemplateId(), paperTemplate.getProblemList().get(1).getProblemId());

        assertEquals(2, paperTemplateRepository.findAll().get(0).getProblemList().size());
        paperTemplate = paperTemplateRepository.findAll().get(0);
        assertEquals(1, paperTemplate.getProblemList().get(0).getIndexNum());
        assertEquals(2, paperTemplate.getProblemList().get(1).getIndexNum());

        assertEquals("문제3", paperTemplate.getProblemList().get(1).getContent());
    }




}
