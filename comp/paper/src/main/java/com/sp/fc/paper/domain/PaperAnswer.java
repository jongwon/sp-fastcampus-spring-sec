package com.sp.fc.paper.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name="sp_paper_answer")
public class PaperAnswer {

    @JsonIgnore
    @ManyToOne
    @JoinColumn(foreignKey = @ForeignKey(name = "paperId"))
    Paper paper;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Embeddable
    public static class PaperAnswerId implements Serializable {
        private Long paperId;
        private Integer num; // 1-base
    }

    @EmbeddedId
    private PaperAnswerId id;

    private Long problemId;
    private String answer;
    private boolean correct;

    private LocalDateTime answered; // updatable

    public Integer num(){
        return id.getNum();
    }

}
