package com.capstone.jobtographer.repositories;

import com.capstone.jobtographer.models.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<AppUser, Long> {
AppUser findByUsername (String username);
}
