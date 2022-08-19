package com.capstone.jobtographer.models;

import javax.persistence.*;

@Entity
@Table(name = "user_certs")
public class UserCert {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    private long user_id;

    @Column
    private long cert_id;

    public UserCert() {

    }

    public UserCert(long user_id, long cert_id) {
        this.user_id = user_id;
        this.cert_id = cert_id;
    }

    public UserCert(long id, long user_id, long cert_id) {
        this.id = id;
        this.user_id = user_id;
        this.cert_id = cert_id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getUser_id() {
        return user_id;
    }

    public void setUser_id(long user_id) {
        this.user_id = user_id;
    }

    public long getCert_id() {
        return cert_id;
    }

    public void setCert_id(long cert_id) {
        this.cert_id = cert_id;
    }
}
