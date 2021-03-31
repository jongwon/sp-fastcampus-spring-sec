package com.sp.fc.user.service;

import com.sp.fc.user.domain.Authority;
import com.sp.fc.user.domain.User;
import com.sp.fc.user.repository.SchoolRepository;
import com.sp.fc.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final SchoolRepository schoolRepository;
    private final UserRepository userRepository;

    public User save(User user) throws DataIntegrityViolationException {
        if(user.getUserId() == null){
            user.setCreated(LocalDateTime.now());
        }
        user.setUpdated(LocalDateTime.now());
        return userRepository.save(user);
    }

    public Optional<User> findUser(Long userId) {
        return userRepository.findById(userId);
    }

    public Page<User> listUser(int pageNum, int size){
        return userRepository.findAll(PageRequest.of(pageNum-1, size));
    }

    public Map<Long, User> getUsers(List<Long> userIds){
        return StreamSupport.stream(userRepository.findAllById(userIds).spliterator(), false)
                .collect(Collectors.toMap(User::getUserId, Function.identity()));
    }

    public void addAuthority(Long userId, String authority){
        userRepository.findById(userId).ifPresent(user->{
            Authority newRole = new Authority(user.getUserId(), authority);
            if(user.getAuthorities() == null){
                HashSet<Authority> authorities = new HashSet<>();
                authorities.add(newRole);
                user.setAuthorities(authorities);
                save(user);
            }else if(!user.getAuthorities().contains(newRole)){
                HashSet<Authority> authorities = new HashSet<>();
                authorities.addAll(user.getAuthorities());
                authorities.add(newRole);
                user.setAuthorities(authorities);
                save(user);
            }
        });
    }

    public void removeAuthority(Long userId, String authority){
        userRepository.findById(userId).ifPresent(user->{
            if(user.getAuthorities()==null) return;
            Authority targetRole = new Authority(user.getUserId(), authority);
            if(user.getAuthorities().contains(targetRole)){
                user.setAuthorities(
                        user.getAuthorities().stream().filter(auth->!auth.equals(targetRole))
                                .collect(Collectors.toSet())
                );
                save(user);
            }
        });
    }

    public void updateUsername(Long userId, String userName) {
        userRepository.updateUserName(userId, userName, LocalDateTime.now());
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public List<User> findTeacherList() {
        return userRepository.findAllByAuthoritiesIn(Authority.ROLE_TEACHER);
    }

    public List<User> findStudentList() {
        return userRepository.findAllByAuthoritiesIn(Authority.ROLE_STUDENT);
    }

    public List<User> findTeacherStudentList(Long userId) {
        return userRepository.findAllByTeacherUserId(userId);
    }

    public Long findTeacherStudentCount(Long userId) {
        return userRepository.countByAllTeacherUserId(userId);
    }

    public List<User> findBySchoolStudentList(Long schoolId) {
        return userRepository.findAllBySchool(schoolId, Authority.ROLE_STUDENT);
    }

    public List<User> findBySchoolTeacherList(Long schoolId) {
        return userRepository.findAllBySchool(schoolId, Authority.ROLE_TEACHER);
    }

    public void updateUserSchoolTeacher(Long userId, Long schoolId, Long teacherId) {
        userRepository.findById(userId).ifPresent(user->{
            if(!user.getSchool().getSchoolId().equals(schoolId)) {
                schoolRepository.findById(schoolId).ifPresent(school -> user.setSchool(school));
            }
            if(!user.getTeacher().getUserId().equals(teacherId)){
                findUser(teacherId).ifPresent(teacher->user.setTeacher(teacher));
            }
            if(user.getSchool().getSchoolId() != user.getTeacher().getSchool().getSchoolId()){
                throw new IllegalArgumentException("해당 학교의 선생님이 아닙니다.");
            }
            save(user);
        });
    }

    public long countTeacher() {
        return userRepository.countAllByAuthoritiesIn(Authority.ROLE_TEACHER);
    }
    public long countTeacher(long schoolId) {
        return userRepository.countAllByAuthoritiesIn(schoolId, Authority.ROLE_TEACHER);
    }

    public long countStudent() {
        return userRepository.countAllByAuthoritiesIn(Authority.ROLE_STUDENT);
    }
    public long countStudent(long schoolId) {
        return userRepository.countAllByAuthoritiesIn(schoolId, Authority.ROLE_STUDENT);
    }

    public Page<User> listStudents(Integer pageNum, Integer size) {
        return userRepository.findAllByAuthoritiesIn(Authority.ROLE_STUDENT, PageRequest.of(pageNum-1, size));
    }

    public Page<User> listTeachers(Integer pageNum, Integer size) {
        return userRepository.findAllByAuthoritiesIn(Authority.ROLE_TEACHER, PageRequest.of(pageNum-1, size));
    }

}
