package com.capstone.jobtographer.models;


import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "certifications")

public class Certification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50, unique = true)
    private String certificationName;

@ManyToMany(mappedBy = "certs")
    private List<AppUser> users;

@ManyToMany(mappedBy = "certs")
    private List<Roadmap> roadmaps;

    public Certification() {
    }
    public Certification(String certificationName) {
        this.certificationName = certificationName;
    }


    public Certification(Long id, String certificationName) {
        this.id = id;
        this.certificationName = certificationName;

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCertificationName() {
        return certificationName;
    }

    public void setCertificationName(String certificationName) {
        this.certificationName = certificationName;
    }

    public List<AppUser> getUsers() { return users; }

    public void setUsers(List<AppUser> users) { this.users = users; }

    public List<Roadmap> getRoadmaps() {
        return roadmaps;
    }

    public void setRoadmaps(List<Roadmap> roadmaps) {
        this.roadmaps = roadmaps;
    }
}
