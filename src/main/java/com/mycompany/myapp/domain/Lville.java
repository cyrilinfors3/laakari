package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Lville.
 */
@Entity
@Table(name = "lville")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Lville implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "nom")
    private String nom;

    @NotNull
    @Column(name = "region", nullable = false)
    private String region;

    @ManyToMany(mappedBy = "lvilles")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Lzone> lzones = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public Lville nom(String nom) {
        this.nom = nom;
        return this;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getRegion() {
        return region;
    }

    public Lville region(String region) {
        this.region = region;
        return this;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public Set<Lzone> getLzones() {
        return lzones;
    }

    public Lville lzones(Set<Lzone> lzones) {
        this.lzones = lzones;
        return this;
    }

    public Lville addLzone(Lzone lzone) {
        lzones.add(lzone);
        lzone.getLvilles().add(this);
        return this;
    }

    public Lville removeLzone(Lzone lzone) {
        lzones.remove(lzone);
        lzone.getLvilles().remove(this);
        return this;
    }

    public void setLzones(Set<Lzone> lzones) {
        this.lzones = lzones;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Lville lville = (Lville) o;
        if(lville.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, lville.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Lville{" +
            "id=" + id +
            ", nom='" + nom + "'" +
            ", region='" + region + "'" +
            '}';
    }
}
