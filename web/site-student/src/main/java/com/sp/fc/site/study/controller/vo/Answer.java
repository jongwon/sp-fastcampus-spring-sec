package com.sp.fc.site.study.controller.vo;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Answer {

//    private Long paperTemplateId;
    private Long paperId;
    private Long problemId;
    private Integer indexNum;
    private String answer;
}
