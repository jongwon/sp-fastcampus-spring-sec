package com.sp.fc.paper.repository;

import com.sp.fc.paper.domain.PaperAnswer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaperAnswerRepository extends JpaRepository<PaperAnswer, PaperAnswer.PaperAnswerId> {

}
