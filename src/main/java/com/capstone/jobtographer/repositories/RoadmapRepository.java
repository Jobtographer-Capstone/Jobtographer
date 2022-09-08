package com.capstone.jobtographer.repositories;

import com.capstone.jobtographer.models.AppUser;
import com.capstone.jobtographer.models.Roadmap;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoadmapRepository extends JpaRepository<Roadmap,Long> {
    List<Roadmap> findByUser(AppUser user);
    Roadmap findTopByUserOrderByIdDesc(AppUser user);
}