package com.muhammet.criteriaexamples.repository.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "tbladres")
@Entity
public class Adres {
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)        
    private Long id;
    @Column(length = 64)
    private String ad;
    @Column(length = 2000)
    private String tanim;

    public Adres(String ad, String tanim) {
        this.ad = ad;
        this.tanim = tanim;
    }

    public Adres() {
    }

    @Override
    public String toString() {
        return "Adres{" + "id=" + id + ", ad=" + ad + ", tanim=" + tanim + '}';
    }

    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAd() {
        return ad;
    }

    public void setAd(String ad) {
        this.ad = ad;
    }

    public String getTanim() {
        return tanim;
    }

    public void setTanim(String tanim) {
        this.tanim = tanim;
    }
    
    
}
