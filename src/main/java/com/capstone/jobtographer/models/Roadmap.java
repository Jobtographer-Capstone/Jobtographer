package com.capstone.jobtographer.models;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "roadmaps")
public class Roadmap {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, length = 100)
    private String industry;

    @Column(nullable = false, length = 100)
    private String career;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private AppUser user;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "roadmap_certs",
            joinColumns = {@JoinColumn(name = "roadmap_id")},
            inverseJoinColumns = {@JoinColumn(name = "cert_id")}
    )
    private List<RoadmapCert> certs;

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

    public Roadmap(String industry, String career, AppUser user, List<RoadmapCert> certs) {
        this.industry = industry;
        this.career = career;
        this.user = user;
        this.certs = certs;
    }

    public Roadmap(long id, String industry, String career, AppUser user, List<RoadmapCert> certs) {
        this.id = id;
        this.industry = industry;
        this.career = career;
        this.user = user;
        this.certs = certs;
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

    public List<RoadmapCert> getCerts() {
        return certs;
    }

    public void setCerts(List<RoadmapCert> certs) {
        this.certs = certs;
    }
}
