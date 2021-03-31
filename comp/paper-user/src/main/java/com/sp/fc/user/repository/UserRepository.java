package com.sp.fc.user.repository;

import com.sp.fc.user.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {


    @Modifying(clearAutomatically = true)
    @Query("update User set name=?2, updated=?3 where userId=?1")
    void updateUserName(Long userId, String userName, LocalDateTime update);

    Optional<User> findByEmail(String username);

    @Query("select a from User a, Authority b where a.userId=b.userId and b.authority=?1")
    List<User> findAllByAuthoritiesIn(String authority);

    @Query("select a from User a, Authority b where a.userId=b.userId and b.authority=?1")
    Page<User> findAllByAuthoritiesIn(String authority, Pageable pageable);

    @Query("select a from User a, Authority b where a.school.schoolId=?1 and a.userId=b.userId and b.authority=?2")
    List<User> findAllBySchool(Long schoolId, String authority);

    @Query("select a from User a, User b where a.teacher.userId=b.userId and b.userId=?1")
    List<User> findAllByTeacherUserId(Long userId);

    @Query("select count(a) from User a, User b where a.teacher.userId=b.userId and b.userId=?1")
    Long countByAllTeacherUserId(Long userId);

    @Query("select count(a) from User a, Authority b where a.userId=b.userId and b.authority=?1")
    long countAllByAuthoritiesIn(String authority);

    @Query("select count(a) from User a, Authority b where a.school.schoolId=?1 and a.userId=b.userId and b.authority=?2")
    long countAllByAuthoritiesIn(long schoolId, String authority);

}
