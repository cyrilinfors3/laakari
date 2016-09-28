package com.mycompany.myapp.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Lproduit.
 */
@Entity
@Table(name = "lproduit")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Lproduit implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "codep", nullable = false)
    private String codep;

    @NotNull
    @Column(name = "libelle", nullable = false)
    private String libelle;

    @NotNull
    @Column(name = "prix", nullable = false)
    private Float prix;

    @ManyToOne
    private Ltransactions ltransactions;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodep() {
        return codep;
    }

    public Lproduit codep(String codep) {
        this.codep = codep;
        return this;
    }

    public void setCodep(String codep) {
        this.codep = codep;
    }

    public String getLibelle() {
        return libelle;
    }

    public Lproduit libelle(String libelle) {
        this.libelle = libelle;
        return this;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public Float getPrix() {
        return prix;
    }

    public Lproduit prix(Float prix) {
        this.prix = prix;
        return this;
    }

    public void setPrix(Float prix) {
        this.prix = prix;
    }

    public Ltransactions getLtransactions() {
        return ltransactions;
    }

    public Lproduit ltransactions(Ltransactions ltransactions) {
        this.ltransactions = ltransactions;
        return this;
    }

    public void setLtransactions(Ltransactions ltransactions) {
        this.ltransactions = ltransactions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Lproduit lproduit = (Lproduit) o;
        if(lproduit.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, lproduit.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Lproduit{" +
            "id=" + id +
            ", codep='" + codep + "'" +
            ", libelle='" + libelle + "'" +
            ", prix='" + prix + "'" +
            '}';
    }
}
