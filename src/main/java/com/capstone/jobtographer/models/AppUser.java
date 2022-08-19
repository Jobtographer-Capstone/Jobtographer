package com.capstone.jobtographer.models;


import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "users")
public class AppUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 50, unique = true)
    private String username;
    @Column(nullable = false, length = 100, unique = true)
    private String email;
    @Column(nullable = false, length = 100)
    private String password;

    @Column
    private String img;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    private List<Roadmap> roadmaps;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "user_certs",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "cert_id")})
    private List<Certification> certs;

    public AppUser() {

    }



    public AppUser(Long id, String username, String email, String password) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public AppUser( String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public AppUser( String username, String email, String password,List<Roadmap> roadmaps) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.roadmaps = roadmaps;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Roadmap> getRoadmaps() {
        return roadmaps;
    }

    public void setRoadmaps(List<Roadmap> roadmaps) {
        this.roadmaps = roadmaps;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public List<Certification> getCerts() {
        return certs;
    }

    public void setCerts(List<Certification> certs) {
        this.certs = certs;
    }
}