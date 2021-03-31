package com.sp.fc.paper.repository;

import com.sp.fc.paper.domain.Problem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface ProblemRepository extends JpaRepository<Problem, Long> {

    List<Problem> findAllByPaperTemplateIdOrderByIndexNumAsc(Long paperTemplateId);

    @Modifying
    @Query("update Problem set content=?2, answer=?3, updated=?4 where problemId=?1")
    void updateProblem(long problemId, String content, String answer, LocalDateTime now);

}
