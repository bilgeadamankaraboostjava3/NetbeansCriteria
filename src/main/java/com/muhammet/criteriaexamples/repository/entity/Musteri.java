package com.muhammet.criteriaexamples.repository.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * NamedQuery -> 
 * name = kullanabilmek için ad giriyorsunuz
 * query = tabloya yapılacak sorguyu giriyoruz. JPQL, HQL ister
 * Avantajı var mı ? sorgular cache e alınır. aynı sorgu değişiklik olmadan gelirse v.t ye gitmeden bellekten çekilir. 
 * Method-> Redis 
 * Tablo cache -> GraphQL,
 */
@NamedQueries({
    @NamedQuery(name = "Musteri.findAll",query = "SELECT m FROM Musteri m"),
    /**
     * Eğer sorgu içine bir paramet geçmek istiyorsanız (:) iki noktadan sonra parametrenin adını yazınız.
     */
    @NamedQuery(name = "Musteri.findById",query = "SELECT m FROM Musteri m WHERE m.id= :id"),
    @NamedQuery(name= "Musteri.findAllByLikeName", query = "SELECT m FROM Musteri m WHERE ad LIKE :ad")
})
@Table(name = "tblmusteri")
@Entity
public class Musteri {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)        
    private Long id;
    @Column(length = 50)
    private String ad;
    @Column(length = 50)
    private String soyad;
    @OneToOne(cascade = CascadeType.ALL)
    private Adres adres;
    
    
    @Override
    public String toString() {
        return "Musteri{" + "id=" + id + ", ad=" + ad + ", soyad=" + soyad + ", adres=" + adres + '}';
    }

    public Musteri(String ad, String soyad, Adres adres) {
        this.ad = ad;
        this.soyad = soyad;
        this.adres = adres;
    }

    public Musteri() {
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

    public String getSoyad() {
        return soyad;
    }

    public void setSoyad(String soyad) {
        this.soyad = soyad;
    }

    public Adres getAdres() {
        return adres;
    }

    public void setAdres(Adres adres) {
        this.adres = adres;
    }

       
    
}
