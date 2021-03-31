package com.sp.fc.user.service.helper;

import com.sp.fc.user.domain.Authority;
import com.sp.fc.user.domain.School;
import com.sp.fc.user.domain.User;
import com.sp.fc.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@RequiredArgsConstructor
public class UserTestHelper {

    private final UserService userService;

    private final PasswordEncoder passwordEncoder; //  = NoOpPasswordEncoder.getInstance();


    public static User makeUser(School school, String name){
        return User.builder()
                .school(school)
                .name(name)
                .email(name+"@test.com")
                .enabled(true)
                .build();
    }

    public User createUser(School school, String name){
        User user = makeUser(school, name);
        user.setPassword(passwordEncoder.encode(name+"123"));
        return userService.save(user);
    }

    public User createUser(School school, String name, String ... authorities){
        User user = createUser(school, name);
        Stream.of(authorities).forEach(auth->userService.addAuthority(user.getUserId(), auth));
        return user;
    }

    public User createTeacher(School school, String name){
        User teacher = createUser(school, name);
        userService.addAuthority(teacher.getUserId(), Authority.ROLE_TEACHER);
        return teacher;
    }

    public static void assertTeacher(School school, User teacher, String name){
        assertUser(school, teacher, name, Authority.ROLE_TEACHER);
    }

    public User createStudent(School school, User teacher, String name, String grade){
        User student = User.builder()
                .school(school)
                .name(name)
                .password(passwordEncoder.encode(name+"123"))
                .email(name+"@test.com")
                .teacher(teacher)
                .grade(grade)
                .enabled(true)
                .build();
        student = userService.save(student);
        userService.addAuthority(student.getUserId(), Authority.ROLE_STUDENT);
        return student;
    }

    public static void assertStudent(School school, User teacher, User student, String name, String grade){
        assertUser(school, student, name, Authority.ROLE_STUDENT);
        assertEquals(teacher.getUserId(), student.getTeacher().getUserId());
        assertEquals(grade, student.getGrade());
    }


    public static void assertUser(School school, User user, String name){
        assertNotNull(user.getUserId());
        assertNotNull(user.getCreated());
        assertNotNull(user.getUpdated());
        assertTrue(user.isEnabled());
        assertEquals(school.getSchoolId(), user.getSchool().getSchoolId());
        assertEquals(name, user.getName());
        assertEquals(name+"@test.com", user.getEmail());
    }


    public static void assertUser(School school, User user, String name, String ... authorities){
        assertUser(school, user, name);
        assertTrue(user.getAuthorities().containsAll(
                Stream.of(authorities).map(auth->new Authority(user.getUserId(), auth)).collect(Collectors.toSet())
        ));
    }


}
