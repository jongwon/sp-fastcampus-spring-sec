package com.sp.fc.user.service;

import com.sp.fc.user.domain.Authority;
import com.sp.fc.user.domain.User;
import com.sp.fc.user.service.helper.UserTestHelper;
import com.sp.fc.user.service.helper.WithUserTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

// 선생님을 등록한다.
// 선생님으로 등록한 학생 리스트를 조회한다.
// 선생님 리스트를 조회 한다.
// 학교로 선생님이 조회된다.
@DataJpaTest
public class TeacherTest extends WithUserTest {

    User teacher;

    @BeforeEach
    void before(){
        prepareUserServices();
        this.teacher = this.userTestHelper.createTeacher(school, "teacher1");
    }

    @DisplayName("1. 선생님을 등록한다. ")
    @Test
    void 선생님_등록_테스트 () {
        List<User> teacherList = userService.findTeacherList();
        assertEquals(1, teacherList.size());
        UserTestHelper.assertTeacher(school, teacherList.get(0), "teacher1");
    }

    @DisplayName("2. 선생님으로 등록한 학생 리스트를 조회한다.")
    @Test
    void 선생님으로_등록한_학생_리스트_조회 () {
        this.userTestHelper.createStudent(school, teacher, "study1", "1");
        this.userTestHelper.createStudent(school, teacher, "study2", "1");
        this.userTestHelper.createStudent(school, teacher, "study3", "1");
        assertEquals(3, userService.findTeacherStudentList(teacher.getUserId()).size());
    }

    @DisplayName("3. 선생님 리스트를 조회 한다.")
    @Test
    void 선생님_리스트_조회 () {
        this.userTestHelper.createUser(school, "teacher2", Authority.ROLE_TEACHER);
        this.userTestHelper.createUser(school, "teacher3", Authority.ROLE_TEACHER);
        this.userTestHelper.createUser(school, "teacher4", Authority.ROLE_TEACHER);
        assertEquals(4, userService.findTeacherList().size());
    }


    @DisplayName("4. 학교로 선생님이 조회된다.")
    @Test
    void 학교로_선생님_리스트_조회 () {
        List<User> teacherList = userService.findBySchoolTeacherList(school.getSchoolId());
        assertEquals(1, teacherList.size());
        UserTestHelper.assertTeacher(school, teacher, "teacher1");

        this.userTestHelper.createUser(school, "teacher2", Authority.ROLE_TEACHER);
        this.userTestHelper.createUser(school, "teacher3", Authority.ROLE_TEACHER);
        assertEquals(3, userService.findTeacherList().size());
    }
}
