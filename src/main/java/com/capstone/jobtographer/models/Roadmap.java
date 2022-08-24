package com.capstone.jobtographer.models;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "roadmaps")
public class Roadmap {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column( length = 100)
    private String industry;

    @Column(nullable = false, length = 100)
    private String career;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private AppUser user;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "roadmap_id")
    private List<RoadmapCert> roadmapCerts;


    public Roadmap(){}

    public Roadmap(String industry, String career){
        this.industry = industry;
        this.career = career;
    }

    public Roadmap(long id, String industry, String career) {
        this.id = id;
        this.industry = industry;
        this.career = career;
    }

    public Roadmap(String industry, String career, AppUser user){
        this.industry = industry;
        this.career = career;
        this.user = user;
    }
    public Roadmap(long id, String industry, String career, AppUser user) {
        this.id = id;
        this.industry = industry;
        this.career = career;
        this.user = user;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public String getCareer() {
        return career;
    }

    public void setCareer(String career) {
        this.career = career;
    }

    public AppUser getUser() {
        return user;
    }

    public void setUser(AppUser user) {
        this.user = user;
    }

    public List<RoadmapCert> getRoadmapCerts() {
        return roadmapCerts;
    }

    public void setRoadmapCerts(List<RoadmapCert> roadmapCerts) {
        this.roadmapCerts = roadmapCerts;
    }
}
