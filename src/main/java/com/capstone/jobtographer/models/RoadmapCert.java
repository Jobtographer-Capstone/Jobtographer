package com.capstone.jobtographer.models;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Table(name = "roadmap_certs")
public class RoadmapCert {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    private String expectedDate;


    @ManyToOne
    @JoinColumn(name = "roadmap_id")
    private Roadmap roadmap_id;

    @ManyToOne
    @JoinColumn(name = "cert_id")
    private Certification cert_id;


    public RoadmapCert() {
    }

    public RoadmapCert(String expectedDate, Roadmap roadmap_id, Certification cert_id) {
        this.expectedDate = expectedDate;
        this.roadmap_id = roadmap_id;
        this.cert_id = cert_id;
    }



    public RoadmapCert(Roadmap roadmap_id, Certification cert_id) {
        this.roadmap_id = roadmap_id;
        this.cert_id = cert_id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getExpectedDate() {
        return expectedDate;
    }

    public void setExpectedDate(String expectedDate) {
        this.expectedDate = expectedDate;
    }

    public Roadmap getRoadmap_id() {
        return roadmap_id;
    }

    public void setRoadmap_id(Roadmap roadmap_id) {
        this.roadmap_id = roadmap_id;
    }

    public Certification getCert_id() {
        return cert_id;
    }

    public void setCert_id(Certification cert_id) {
        this.cert_id = cert_id;
    }
}
