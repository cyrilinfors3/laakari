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
 * A Larrondissement.
 */
@Entity
@Table(name = "larrondissement")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Larrondissement implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "nom")
    private String nom;

    @NotNull
    @Column(name = "ville", nullable = false)
    private String ville;

    @ManyToMany(mappedBy = "larrondissements")
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

    public Larrondissement nom(String nom) {
        this.nom = nom;
        return this;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getVille() {
        return ville;
    }

    public Larrondissement ville(String ville) {
        this.ville = ville;
        return this;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public Set<Lzone> getLzones() {
        return lzones;
    }

    public Larrondissement lzones(Set<Lzone> lzones) {
        this.lzones = lzones;
        return this;
    }

    public Larrondissement addLzone(Lzone lzone) {
        lzones.add(lzone);
        lzone.getLarrondissements().add(this);
        return this;
    }

    public Larrondissement removeLzone(Lzone lzone) {
        lzones.remove(lzone);
        lzone.getLarrondissements().remove(this);
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
        Larrondissement larrondissement = (Larrondissement) o;
        if(larrondissement.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, larrondissement.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Larrondissement{" +
            "id=" + id +
            ", nom='" + nom + "'" +
            ", ville='" + ville + "'" +
            '}';
    }
}
