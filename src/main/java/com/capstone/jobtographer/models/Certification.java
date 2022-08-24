package com.capstone.jobtographer.models;


import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "certifications")

public class Certification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
@Lob
    @Column(nullable = false, unique = true)
    private String certificationName;

@OneToMany(mappedBy = "cert_id")
    private List<UserCert> userCerts;

@OneToMany(mappedBy = "cert_id")
    private List<RoadmapCert> roadmapCerts;

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

    public List<UserCert> getUserCerts() {
        return userCerts;
    }

    public void setUserCerts(List<UserCert> userCerts) {
        this.userCerts = userCerts;
    }

    public List<RoadmapCert> getRoadmapCerts() {
        return roadmapCerts;
    }

    public void setRoadmapCerts(List<RoadmapCert> roadmapCerts) {
        this.roadmapCerts = roadmapCerts;
    }
}
