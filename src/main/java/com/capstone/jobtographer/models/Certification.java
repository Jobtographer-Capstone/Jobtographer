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

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "user_certs",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "cert_id")})

    private List<AppUser> users;

    public Certification() {
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
}
