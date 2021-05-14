package com.sp.fc.site.study.controller.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StudySignUpForm {

    private Long schoolId;
    private Long teacherId;
    private String name;
    private String email;
    private String password;
    private String rePassword;
    private String grade;

}
