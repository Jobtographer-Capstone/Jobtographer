package com.capstone.jobtographer.models;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "roadmaps")
public class Roadmap{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    private int progress;

    @Column(nullable = false, length = 100)
    private String career;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private AppUser user;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "roadmap_id")
    private List<RoadmapCert> roadmapCerts;


    public Roadmap() {
    }


    public Roadmap(long id, String career) {
        this.id = id;
        this.career = career;
    }

    public Roadmap(String career, AppUser user) {
        this.career = career;
        this.user = user;
    }

    public Roadmap(long progress, String career, AppUser user) {
        this.id = progress;
        this.career = career;
        this.user = user;
    }
    public Roadmap(long id, String career, AppUser user, int progress) {
        this.id = id;
        this.career = career;
        this.user = user;
        this.progress = progress;

    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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
