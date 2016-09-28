package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Ltransactions.
 */
@Entity
@Table(name = "ltransactions")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Ltransactions implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "qte", nullable = false)
    private Integer qte;

    @NotNull
    @Column(name = "datet", nullable = false)
    private ZonedDateTime datet;

    @NotNull
    @Column(name = "vendeur", nullable = false)
    private String vendeur;

    @NotNull
    @Column(name = "acheteur", nullable = false)
    private String acheteur;

    @ManyToOne
    private Luser luser;

    @OneToMany(mappedBy = "ltransactions")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Lproduit> lproduits = new HashSet<>();

    @ManyToOne
    private Lfacture lfacture;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getQte() {
        return qte;
    }

    public Ltransactions qte(Integer qte) {
        this.qte = qte;
        return this;
    }

    public void setQte(Integer qte) {
        this.qte = qte;
    }

    public ZonedDateTime getDatet() {
        return datet;
    }

    public Ltransactions datet(ZonedDateTime datet) {
        this.datet = datet;
        return this;
    }

    public void setDatet(ZonedDateTime datet) {
        this.datet = datet;
    }

    public String getVendeur() {
        return vendeur;
    }

    public Ltransactions vendeur(String vendeur) {
        this.vendeur = vendeur;
        return this;
    }

    public void setVendeur(String vendeur) {
        this.vendeur = vendeur;
    }

    public String getAcheteur() {
        return acheteur;
    }

    public Ltransactions acheteur(String acheteur) {
        this.acheteur = acheteur;
        return this;
    }

    public void setAcheteur(String acheteur) {
        this.acheteur = acheteur;
    }

    public Luser getLuser() {
        return luser;
    }

    public Ltransactions luser(Luser luser) {
        this.luser = luser;
        return this;
    }

    public void setLuser(Luser luser) {
        this.luser = luser;
    }

    public Set<Lproduit> getLproduits() {
        return lproduits;
    }

    public Ltransactions lproduits(Set<Lproduit> lproduits) {
        this.lproduits = lproduits;
        return this;
    }

    public Ltransactions addLproduit(Lproduit lproduit) {
        lproduits.add(lproduit);
        lproduit.setLtransactions(this);
        return this;
    }

    public Ltransactions removeLproduit(Lproduit lproduit) {
        lproduits.remove(lproduit);
        lproduit.setLtransactions(null);
        return this;
    }

    public void setLproduits(Set<Lproduit> lproduits) {
        this.lproduits = lproduits;
    }

    public Lfacture getLfacture() {
        return lfacture;
    }

    public Ltransactions lfacture(Lfacture lfacture) {
        this.lfacture = lfacture;
        return this;
    }

    public void setLfacture(Lfacture lfacture) {
        this.lfacture = lfacture;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Ltransactions ltransactions = (Ltransactions) o;
        if(ltransactions.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, ltransactions.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Ltransactions{" +
            "id=" + id +
            ", qte='" + qte + "'" +
            ", datet='" + datet + "'" +
            ", vendeur='" + vendeur + "'" +
            ", acheteur='" + acheteur + "'" +
            '}';
    }
}
