package com.sp.fc.paper.service;


import com.sp.fc.paper.domain.Paper;
import com.sp.fc.paper.domain.PaperTemplate;
import com.sp.fc.paper.repository.PaperAnswerRepository;
import com.sp.fc.paper.repository.PaperRepository;
import com.sp.fc.paper.service.PaperService;
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
 *  시나리오
 *
 *      1) 선생님1, 학습자1, 학습자2
 *      2) 시험지 템플릿을 만든다.
 *
 */
@DisplayName("템플릿을 가지고 학습자들에게 시험지를 낸다.")
@DataJpaTest
public class PaperTest extends WithPaperTemplateTest {

    @Autowired
    private PaperRepository paperRepository;
    @Autowired
    private PaperAnswerRepository paperAnswerRepository;

    private PaperService paperService;

    private PaperTemplate paperTemplate;
    private User study1;
    private User study2;

    @BeforeEach
    void before(){
        paperRepository.deleteAll();

        preparePaperTemplate();

        this.paperService = new PaperService(userRepository, paperTemplateService, paperRepository, paperAnswerRepository);

        this.study1 = this.userTestHelper.createStudent(school, teacher, "study1", "중1");
        this.study2 = this.userTestHelper.createStudent(school, teacher, "study2", "중1");

        this.paperTemplate = this.paperTemplateTestHelper.createPaperTemplate(teacher, "시험지1");
        this.paperTemplateTestHelper.addProblem(paperTemplate.getPaperTemplateId(),
                problem(paperTemplate.getPaperTemplateId(), "문제1", "답1"));
        this.paperTemplateTestHelper.addProblem(paperTemplate.getPaperTemplateId(),
                problem(paperTemplate.getPaperTemplateId(), "문제2", "답2"));
    }


    @DisplayName("1. 시험지 템플릿을 학습자1에게 시험지를 낸다.")
    @Test
    void 시험지_템플릿을_학습자1에게_시험지를_낸다() {
        paperService.publishPaper(paperTemplate.getPaperTemplateId(), List.of(study1.getUserId()));

        List<Paper> papers = paperService.getPapers(paperTemplate.getPaperTemplateId());
        assertEquals(1, papers.size());

        Paper paper = papers.get(0);
        assertNotNull(paper.getPaperId());
        assertNotNull(paper.getCreated());
        assertEquals(study1.getUserId(), paper.getStudyUserId());
        assertEquals(paperTemplate.getPaperTemplateId(), paper.getPaperTemplateId());
        assertEquals(paperTemplate.getName(), paper.getName());
        assertNull(paper.getStartTime());
        assertNull(paper.getEndTime());
        assertEquals(Paper.PaperState.READY, paper.getState());
        assertEquals(2, paper.getTotal());
        assertEquals(0, paper.getAnswered());
        assertEquals(0, paper.getCorrect());

        PaperTemplate result = paperTemplateService.findById(paperTemplate.getPaperTemplateId()).get();
        assertEquals(1, result.getPublishedCount());
        assertEquals(0, result.getCompleteCount());
    }

    @DisplayName("2. 시험지를 2명 이상의 user 를 검색해 낸다. (학습자1, 학습자2) ")
    @Test
    void 시험지를_2명_이상의_user를_검색해_낸다() {
        paperService.publishPaper(paperTemplate.getPaperTemplateId(), List.of(study1.getUserId(), study2.getUserId()));

        List<Paper> papers = paperService.getPapers(paperTemplate.getPaperTemplateId());
        assertEquals(2, papers.size());
    }

    @DisplayName("3. 시험지 삭제 기능")
    @Test
    void 시험지_삭제_기능() {
        paperService.publishPaper(paperTemplate.getPaperTemplateId(), List.of(study1.getUserId(), study2.getUserId()));

        paperService.removePaper(paperTemplate.getPaperTemplateId(), List.of(study1.getUserId()));
        List<Paper> papers = paperService.getPapers(paperTemplate.getPaperTemplateId());
        assertEquals(1, papers.size());

        paperService.removePaper(paperTemplate.getPaperTemplateId(), List.of(study2.getUserId()));
        papers = paperService.getPapers(paperTemplate.getPaperTemplateId());
        assertEquals(0, papers.size());

        paperService.removePaper(paperTemplate.getPaperTemplateId(), List.of(study2.getUserId()));
        papers = paperService.getPapers(paperTemplate.getPaperTemplateId());
        assertEquals(0, papers.size());
    }


}
