package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Lprofil.
 */
@Entity
@Table(name = "lprofil")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Lprofil implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "login", nullable = false)
    private String login;

    @NotNull
    @Column(name = "pass", nullable = false)
    private String pass;

    @NotNull
    @Column(name = "tel", nullable = false)
    private Integer tel;

    @Lob
    @Column(name = "pic")
    private byte[] pic;

    @Column(name = "pic_content_type")
    private String picContentType;

    @ManyToOne
    private Dmanager dmanager;

    @ManyToOne
    private Dealer dealer;

    @ManyToOne
    private Sdealer sdealer;

    @OneToOne(mappedBy = "lprofil")
    @JsonIgnore
    private Delegue delegue;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public Lprofil login(String login) {
        this.login = login;
        return this;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPass() {
        return pass;
    }

    public Lprofil pass(String pass) {
        this.pass = pass;
        return this;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public Integer getTel() {
        return tel;
    }

    public Lprofil tel(Integer tel) {
        this.tel = tel;
        return this;
    }

    public void setTel(Integer tel) {
        this.tel = tel;
    }

    public byte[] getPic() {
        return pic;
    }

    public Lprofil pic(byte[] pic) {
        this.pic = pic;
        return this;
    }

    public void setPic(byte[] pic) {
        this.pic = pic;
    }

    public String getPicContentType() {
        return picContentType;
    }

    public Lprofil picContentType(String picContentType) {
        this.picContentType = picContentType;
        return this;
    }

    public void setPicContentType(String picContentType) {
        this.picContentType = picContentType;
    }

    public Dmanager getDmanager() {
        return dmanager;
    }

    public Lprofil dmanager(Dmanager dmanager) {
        this.dmanager = dmanager;
        return this;
    }

    public void setDmanager(Dmanager dmanager) {
        this.dmanager = dmanager;
    }

    public Dealer getDealer() {
        return dealer;
    }

    public Lprofil dealer(Dealer dealer) {
        this.dealer = dealer;
        return this;
    }

    public void setDealer(Dealer dealer) {
        this.dealer = dealer;
    }

    public Sdealer getSdealer() {
        return sdealer;
    }

    public Lprofil sdealer(Sdealer sdealer) {
        this.sdealer = sdealer;
        return this;
    }

    public void setSdealer(Sdealer sdealer) {
        this.sdealer = sdealer;
    }

    public Delegue getDelegue() {
        return delegue;
    }

    public Lprofil delegue(Delegue delegue) {
        this.delegue = delegue;
        return this;
    }

    public void setDelegue(Delegue delegue) {
        this.delegue = delegue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Lprofil lprofil = (Lprofil) o;
        if(lprofil.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, lprofil.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Lprofil{" +
            "id=" + id +
            ", login='" + login + "'" +
            ", pass='" + pass + "'" +
            ", tel='" + tel + "'" +
            ", pic='" + pic + "'" +
            ", picContentType='" + picContentType + "'" +
            '}';
    }
}
