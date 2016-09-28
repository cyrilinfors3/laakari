package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import com.mycompany.myapp.domain.enumeration.Sex;

/**
 * A Lcallbox.
 */
@Entity
@Table(name = "lcallbox")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Lcallbox implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "nom")
    private String nom;

    @Column(name = "prenom")
    private String prenom;

    @Column(name = "dateofbirth")
    private LocalDate dateofbirth;

    @Column(name = "quatier")
    private String quatier;

    @Column(name = "mastersim")
    private String mastersim;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "sex", nullable = false)
    private Sex sex;

    @OneToMany(mappedBy = "lcallbox")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Lvisit> lvisits = new HashSet<>();

    @ManyToOne
    private Lpoint lpoint;

    @ManyToOne
    @NotNull
    private Lroute lroute;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public Lcallbox nom(String nom) {
        this.nom = nom;
        return this;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public Lcallbox prenom(String prenom) {
        this.prenom = prenom;
        return this;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public LocalDate getDateofbirth() {
        return dateofbirth;
    }

    public Lcallbox dateofbirth(LocalDate dateofbirth) {
        this.dateofbirth = dateofbirth;
        return this;
    }

    public void setDateofbirth(LocalDate dateofbirth) {
        this.dateofbirth = dateofbirth;
    }

    public String getQuatier() {
        return quatier;
    }

    public Lcallbox quatier(String quatier) {
        this.quatier = quatier;
        return this;
    }

    public void setQuatier(String quatier) {
        this.quatier = quatier;
    }

    public String getMastersim() {
        return mastersim;
    }

    public Lcallbox mastersim(String mastersim) {
        this.mastersim = mastersim;
        return this;
    }

    public void setMastersim(String mastersim) {
        this.mastersim = mastersim;
    }

    public Sex getSex() {
        return sex;
    }

    public Lcallbox sex(Sex sex) {
        this.sex = sex;
        return this;
    }

    public void setSex(Sex sex) {
        this.sex = sex;
    }

    public Set<Lvisit> getLvisits() {
        return lvisits;
    }

    public Lcallbox lvisits(Set<Lvisit> lvisits) {
        this.lvisits = lvisits;
        return this;
    }

    public Lcallbox addLvisit(Lvisit lvisit) {
        lvisits.add(lvisit);
        lvisit.setLcallbox(this);
        return this;
    }

    public Lcallbox removeLvisit(Lvisit lvisit) {
        lvisits.remove(lvisit);
        lvisit.setLcallbox(null);
        return this;
    }

    public void setLvisits(Set<Lvisit> lvisits) {
        this.lvisits = lvisits;
    }

    public Lpoint getLpoint() {
        return lpoint;
    }

    public Lcallbox lpoint(Lpoint lpoint) {
        this.lpoint = lpoint;
        return this;
    }

    public void setLpoint(Lpoint lpoint) {
        this.lpoint = lpoint;
    }

    public Lroute getLroute() {
        return lroute;
    }

    public Lcallbox lroute(Lroute lroute) {
        this.lroute = lroute;
        return this;
    }

    public void setLroute(Lroute lroute) {
        this.lroute = lroute;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Lcallbox lcallbox = (Lcallbox) o;
        if(lcallbox.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, lcallbox.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Lcallbox{" +
            "id=" + id +
            ", nom='" + nom + "'" +
            ", prenom='" + prenom + "'" +
            ", dateofbirth='" + dateofbirth + "'" +
            ", quatier='" + quatier + "'" +
            ", mastersim='" + mastersim + "'" +
            ", sex='" + sex + "'" +
            '}';
    }
}
