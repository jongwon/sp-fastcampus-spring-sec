package com.sp.fc.site.teacher.controller.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TeacherSignUpForm {

    private Long schoolId;
    private String name;
    private String email;
    private String password;
    private String rePassword;

}
