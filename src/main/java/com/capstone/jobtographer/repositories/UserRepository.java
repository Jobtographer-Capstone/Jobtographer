package com.capstone.jobtographer.repositories;

import com.capstone.jobtographer.models.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;

public interface UserRepository extends JpaRepository<AppUser, Long> {
AppUser findByUsername (String username);
AppUser findById(long id);
AppUser findByEmail(String email);
@Modifying
    @Transactional
    @Query(value="UPDATE AppUser u SET u.img = :img WHERE u.username = :username")
    Integer updateImg(String img, String username);
}
