package com.sp.fc.paper.repository;

import com.sp.fc.paper.domain.Paper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaperRepository extends JpaRepository<Paper, Long> {

    List<Paper> findAllByPaperTemplateIdAndStudyUserIdIn(Long paperTemplateId, List<Long> studyIdlist);

    List<Paper> findAllByPaperTemplateId(Long paperTemplateId);
    long countByPaperTemplateId(Long paperTemplateId);

    List<Paper> findAllByStudyUserIdOrderByCreatedDesc(Long studyUserId);
    long countByStudyUserId(Long studyUserId);

    Page<Paper> findAllByStudyUserIdAndStateOrderByCreatedDesc(Long studyUserId, Paper.PaperState state, Pageable pageable);

    List<Paper> findAllByStudyUserIdAndStateOrderByCreatedDesc(Long studyUserId, Paper.PaperState state);
    long countByStudyUserIdAndState(Long studyUserId, Paper.PaperState state);

    List<Paper> findAllByStudyUserIdAndStateInOrderByCreatedDesc(Long studyUserId, List<Paper.PaperState> states);
    long countByStudyUserIdAndStateIn(Long studyUserId, List<Paper.PaperState> states);
}