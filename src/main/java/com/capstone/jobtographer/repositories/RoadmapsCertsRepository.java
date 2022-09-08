package com.capstone.jobtographer.repositories;

import com.capstone.jobtographer.models.Roadmap;
import com.capstone.jobtographer.models.RoadmapCert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RoadmapsCertsRepository extends JpaRepository<RoadmapCert,Long> {
    @Query("SELECT rc FROM RoadmapCert rc WHERE rc.roadmap_id.id= :id")
    List<RoadmapCert> findAllByRoadmap_id(long id);

    @Query("SELECT rc FROM RoadmapCert rc WHERE rc.roadmap_id.id= :id order by rc.expectedDate")
    List<RoadmapCert> findAllByRoadmap_idOrderByExpectedDateAsc(long id);
}