package com.capstone.jobtographer.models;

import javax.persistence.*;
import java.sql.Date;


@Entity
@Table(name = "user_certs")
public class UserCert {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(nullable = false)
    private AppUser user_id;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Certification cert_id;

    @Column
    private Date expDate;

    public UserCert() {

    }

    public UserCert(AppUser user_id, Certification cert_id, Date expDate) {
        this.user_id = user_id;
        this.cert_id = cert_id;
        this.expDate = expDate;
    }

    public UserCert(AppUser user_id, Certification cert_id) {
        this.user_id = user_id;
        this.cert_id = cert_id;
    }

    public UserCert(long id, AppUser user_id, Certification cert_id, Date expDate) {
        this.id = id;
        this.user_id = user_id;
        this.cert_id = cert_id;
        this.expDate = expDate;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public AppUser getUser_id() {
        return user_id;
    }

    public void setUser_id(AppUser user_id) {
        this.user_id = user_id;
    }

    public Certification getCert_id() {
        return cert_id;
    }

    public void setCert_id(Certification cert_id) {
        this.cert_id = cert_id;
    }

    public Date getExpDate() {
        return expDate;
    }

    public void setExpDate(Date expDate) {
        this.expDate = expDate;
    }
}
