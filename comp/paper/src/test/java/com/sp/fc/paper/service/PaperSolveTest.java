package com.sp.fc.paper.service;


import com.sp.fc.paper.domain.Paper;
import com.sp.fc.paper.domain.PaperTemplate;
import com.sp.fc.paper.domain.Problem;
import com.sp.fc.paper.repository.PaperAnswerRepository;
import com.sp.fc.paper.repository.PaperRepository;
import com.sp.fc.paper.service.helper.WithPaperTemplateTest;
import com.sp.fc.user.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 시나리오
 *
 * 1) 선생님1, 학생1
 * 2) 시험지 템플릿을 만들고
 * 3) 시험지를 출제함.
 *
 *
 */
@DisplayName("학습자가 시험지를 푸는 것에 대해서 테스트 한다. ")
@DataJpaTest
public class PaperSolveTest extends WithPaperTemplateTest {

    @Autowired
    private PaperRepository paperRepository;
    @Autowired
    private PaperAnswerRepository paperAnswerRepository;

    private PaperService paperService;

    private PaperTemplate paperTemplate;
    private Problem problem1;
    private Problem problem2;

    private User study1;
    private Paper paper;

    @BeforeEach
    void before(){
        paperRepository.deleteAll();
        preparePaperTemplate();

        this.paperService = new PaperService(userRepository, paperTemplateService, paperRepository, paperAnswerRepository);
        this.study1 = this.userTestHelper.createStudent(school, teacher, "study1", "중1");
        this.paperTemplate = this.paperTemplateTestHelper.createPaperTemplate(teacher, "시험지1");
        this.problem1 = this.paperTemplateTestHelper.addProblem(paperTemplate.getPaperTemplateId(),
                problem(paperTemplate.getPaperTemplateId(), "문제1", "답1"));
        this.problem2 = this.paperTemplateTestHelper.addProblem(paperTemplate.getPaperTemplateId(),
                problem(paperTemplate.getPaperTemplateId(), "문제2", "답2"));
        this.paper = paperService.publishPaper(paperTemplate.getPaperTemplateId(), List.of(study1.getUserId())).get(0);
    }

    @DisplayName("1. 시험지를 모두 풀어서 100점을 맞는다.")
    @Test
    void 시험지를_모두_풀어서_100점을_맞는다() {

        paperService.answer(paper.getPaperId(), problem1.getProblemId(), 1, "답1");
        Paper ingPaper = paperService.findPaper(this.paper.getPaperId()).get();
        assertEquals(2, ingPaper.getTotal());
        assertEquals(1, ingPaper.getAnswered());
        assertEquals(0, ingPaper.getCorrect());
        assertEquals(Paper.PaperState.START, ingPaper.getState());
        assertNotNull(ingPaper.getStartTime());
        assertNull(ingPaper.getEndTime());

        paperService.answer(paper.getPaperId(), problem2.getProblemId(), 2, "답2");
        ingPaper = paperService.findPaper(this.paper.getPaperId()).get();
        assertEquals(2, ingPaper.getTotal());
        assertEquals(2, ingPaper.getAnswered());
        assertEquals(0, ingPaper.getCorrect());
        assertEquals(Paper.PaperState.START, ingPaper.getState());
        assertNotNull(ingPaper.getStartTime());
        assertNull(ingPaper.getEndTime());

        paperService.paperDone(paper.getPaperId());

        Paper resultPaper = paperService.findPaper(this.paper.getPaperId()).get();
        assertEquals(2, resultPaper.getTotal());
        assertEquals(2, resultPaper.getAnswered());
        assertEquals(2, resultPaper.getCorrect());
        assertEquals(Paper.PaperState.END, resultPaper.getState());
        assertNotNull(resultPaper.getStartTime());
        assertNotNull(resultPaper.getEndTime());

        PaperTemplate result = paperTemplateService.findById(paperTemplate.getPaperTemplateId()).get();
        assertEquals(1, result.getPublishedCount());
        assertEquals(1, result.getCompleteCount());
    }

    @DisplayName("2. 문제를 한개 틀려서 50점을 맞는다.")
    @Test
    void 문제를_한개_틀려서_50점을_맞는다() {
        paperService.answer(paper.getPaperId(), problem1.getProblemId(), 1, "답1");
        paperService.answer(paper.getPaperId(), problem2.getProblemId(), 2, "오답");
        paperService.paperDone(paper.getPaperId());
        Paper resultPaper = paperService.findPaper(this.paper.getPaperId()).get();
        assertEquals(2, resultPaper.getTotal());
        assertEquals(2, resultPaper.getAnswered());
        assertEquals(1, resultPaper.getCorrect());
    }

    @DisplayName("3. 문제를 모두 틀려서 0점을 맞는다.")
    @Test
    void 문제를_모두_틀려서_0점을_맞는다() {
        paperService.answer(paper.getPaperId(), problem1.getProblemId(), 1, "오답");
        paperService.answer(paper.getPaperId(), problem2.getProblemId(), 2, "오답");
        paperService.paperDone(paper.getPaperId());
        Paper resultPaper = paperService.findPaper(this.paper.getPaperId()).get();
        assertEquals(2, resultPaper.getTotal());
        assertEquals(2, resultPaper.getAnswered());
        assertEquals(0, resultPaper.getCorrect());
    }

    @DisplayName("4. 1번 문제 풀고 중간에 제출해 버린다.")
    @Test
    void 첫번째_문제_풀고_중간에_제출해_버린다() {
        paperService.answer(paper.getPaperId(), problem1.getProblemId(), 1, "답1");
        paperService.paperDone(paper.getPaperId());
        Paper resultPaper = paperService.findPaper(this.paper.getPaperId()).get();
        assertEquals(2, resultPaper.getTotal());
        assertEquals(1, resultPaper.getAnswered());
        assertEquals(1, resultPaper.getCorrect());
    }

    @DisplayName("5. 2문제 풀고 중간에 제출해 버린다.")
    @Test
    void 두번째_문제_풀고_중간에_제출해_버린다() {
        paperService.answer(paper.getPaperId(), problem2.getProblemId(), 2, "답2");
        paperService.paperDone(paper.getPaperId());
        Paper resultPaper = paperService.findPaper(this.paper.getPaperId()).get();
        assertEquals(2, resultPaper.getTotal());
        assertEquals(1, resultPaper.getAnswered());
        assertEquals(1, resultPaper.getCorrect());
    }

    @DisplayName("6. 문제를 풀지 않고 그냥 제출해 버린다.")
    @Test
    void 문제를_풀지_않고_그냥_제출해_버린다() {
        paperService.paperDone(paper.getPaperId());
        Paper resultPaper = paperService.findPaper(this.paper.getPaperId()).get();
        assertEquals(2, resultPaper.getTotal());
        assertEquals(0, resultPaper.getAnswered());
        assertEquals(0, resultPaper.getCorrect());
    }

    @DisplayName("7. 상태에 따라 시험지가 조회된다.")
    @Test
    void 상태에_따라_시험지가_조회된다() {
        assertEquals(1, paperService.getPapersByUserState(study1.getUserId(), List.of(Paper.PaperState.READY, Paper.PaperState.START)).size());
        assertEquals(0, paperService.getPapersByUserState(study1.getUserId(), Paper.PaperState.END).size());

        paperService.answer(paper.getPaperId(), problem2.getProblemId(), 2, "답2");
        assertEquals(1, paperService.getPapersByUserState(study1.getUserId(), List.of(Paper.PaperState.READY, Paper.PaperState.START)).size());
        assertEquals(0, paperService.getPapersByUserState(study1.getUserId(), Paper.PaperState.END).size());

        paperService.paperDone(paper.getPaperId());

        assertEquals(0, paperService.getPapersByUserState(study1.getUserId(), List.of(Paper.PaperState.READY, Paper.PaperState.START)).size());
        assertEquals(1, paperService.getPapersByUserState(study1.getUserId(), Paper.PaperState.END).size());
    }

}
