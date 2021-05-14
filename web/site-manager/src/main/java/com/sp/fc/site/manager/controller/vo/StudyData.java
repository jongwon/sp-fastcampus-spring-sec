package com.sp.fc.site.manager.controller.vo;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StudyData {

    private String schoolName;
    private Long userId;
    private String name;
    private String email;
    private String grade;

}
