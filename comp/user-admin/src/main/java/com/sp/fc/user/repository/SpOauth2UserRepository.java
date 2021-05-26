package com.sp.fc.user.repository;

import com.sp.fc.user.domain.SpOauth2User;
import org.springframework.data.jpa.repository.JpaRepository;


public interface SpOauth2UserRepository extends JpaRepository<SpOauth2User, String> {

}
