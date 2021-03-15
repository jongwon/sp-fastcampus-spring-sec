package com.sp.fc.web.paper;

import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PaperService {


    private final PaperRepository paperRepository;

    public PaperService(PaperRepository paperRepository) {
        this.paperRepository = paperRepository;
    }

    public void setPaper(Paper paper){
        paperRepository.save(paper);
    }

    public Optional<Paper> getPaper(Long paperId) {
        return paperRepository.findById(paperId);
    }


}
