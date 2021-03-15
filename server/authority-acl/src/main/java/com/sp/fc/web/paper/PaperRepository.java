package com.sp.fc.web.paper;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaperRepository extends JpaRepository<Paper, Long> {

//    @Cacheable(value="papers")
    Optional<Paper> findById(Long id);

}
