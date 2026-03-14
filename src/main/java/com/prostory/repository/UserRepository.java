package com.prostory.repository;

import com.prostory.entity.User;
import com.prostory.entity.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Query("select u from UserInfo u where u.email = ?1")
    Optional<UserInfo> findUserInfoByEmail(String email);

    @Query("select u from User u join u.userInfo ui where ui.email = ?1")
    Optional<User> findUserByEmail(String email);
}