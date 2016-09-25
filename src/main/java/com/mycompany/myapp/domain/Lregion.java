package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Lregion.
 */
@Entity
@Table(name = "lregion")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Lregion implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "nom", nullable = false)
    private String nom;

    @Column(name = "capital")
    private String capital;

    @OneToOne(mappedBy = "lregion")
    @JsonIgnore
    private Lzone lzone;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public Lregion nom(String nom) {
        this.nom = nom;
        return this;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getCapital() {
        return capital;
    }

    public Lregion capital(String capital) {
        this.capital = capital;
        return this;
    }

    public void setCapital(String capital) {
        this.capital = capital;
    }

    public Lzone getLzone() {
        return lzone;
    }

    public Lregion lzone(Lzone lzone) {
        this.lzone = lzone;
        return this;
    }

    public void setLzone(Lzone lzone) {
        this.lzone = lzone;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Lregion lregion = (Lregion) o;
        if(lregion.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, lregion.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Lregion{" +
            "id=" + id +
            ", nom='" + nom + "'" +
            ", capital='" + capital + "'" +
            '}';
    }
}
