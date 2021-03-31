package com.sp.fc.user.service;

import com.sp.fc.user.domain.User;
import com.sp.fc.user.service.helper.UserTestHelper;
import com.sp.fc.user.service.helper.WithUserTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
public class StudentTest extends WithUserTest {

    User teacher;
    User student;

    @BeforeEach
    void before(){
        prepareUserServices();
        this.teacher = this.userTestHelper.createTeacher(school, "teacher1");
        this.student = this.userTestHelper.createStudent(school, teacher, "student1", "1");
    }

    @DisplayName("1. 학습자 등록 한다. ")
    @Test
    void 햑생_등록_테스트 () {
        List<User> studentList = userService.findStudentList();
        assertEquals(1, studentList.size());
        UserTestHelper.assertStudent(school, teacher, studentList.get(0), "student1", "1");
    }

    @DisplayName("2. 선생님으로 등록하면 선생님의 학습자가 조회된다 ")
    @Test
    void 선생님의_학생으로_조회된다 () {
        List<User> studentList = userService.findTeacherStudentList(teacher.getUserId());
        assertEquals(1, studentList.size());
        UserTestHelper.assertStudent(school, teacher, studentList.get(0), "student1", "1");
    }

    @DisplayName("3. 학교로 학습자가 조회된다.")
    @Test
    void 학교의_학생으로_조회된다 () {
        List<User> studentList = userService.findBySchoolStudentList(school.getSchoolId());
        assertEquals(1, studentList.size());
        UserTestHelper.assertStudent(school, teacher, studentList.get(0), "student1", "1");
    }
}
