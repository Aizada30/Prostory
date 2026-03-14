package com.prostory.repository;

import com.prostory.entity.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserInfoRepository extends JpaRepository<UserInfo, Long> {

    Optional<UserInfo> findByEmail(String email);

    boolean existsByEmail(String email);

    Optional<UserInfo> findByResetPasswordToken(String token);

}