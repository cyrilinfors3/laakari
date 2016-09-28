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
 * A Luser.
 */
@Entity
@Table(name = "luser")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Luser implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "identifiant", nullable = false)
    private String identifiant;

    @OneToMany(mappedBy = "luser")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Dmanager> dmanagers = new HashSet<>();

    @OneToMany(mappedBy = "luser")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Dealer> dealers = new HashSet<>();

    @OneToMany(mappedBy = "luser")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Sdealer> sdealers = new HashSet<>();

    @OneToMany(mappedBy = "luser")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Delegue> delegues = new HashSet<>();

    @OneToOne
    @JoinColumn(unique = true)
    private Lzone lzone;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIdentifiant() {
        return identifiant;
    }

    public Luser identifiant(String identifiant) {
        this.identifiant = identifiant;
        return this;
    }

    public void setIdentifiant(String identifiant) {
        this.identifiant = identifiant;
    }

    public Set<Dmanager> getDmanagers() {
        return dmanagers;
    }

    public Luser dmanagers(Set<Dmanager> dmanagers) {
        this.dmanagers = dmanagers;
        return this;
    }

    public Luser addDmanager(Dmanager dmanager) {
        dmanagers.add(dmanager);
        dmanager.setLuser(this);
        return this;
    }

    public Luser removeDmanager(Dmanager dmanager) {
        dmanagers.remove(dmanager);
        dmanager.setLuser(null);
        return this;
    }

    public void setDmanagers(Set<Dmanager> dmanagers) {
        this.dmanagers = dmanagers;
    }

    public Set<Dealer> getDealers() {
        return dealers;
    }

    public Luser dealers(Set<Dealer> dealers) {
        this.dealers = dealers;
        return this;
    }

    public Luser addDealer(Dealer dealer) {
        dealers.add(dealer);
        dealer.setLuser(this);
        return this;
    }

    public Luser removeDealer(Dealer dealer) {
        dealers.remove(dealer);
        dealer.setLuser(null);
        return this;
    }

    public void setDealers(Set<Dealer> dealers) {
        this.dealers = dealers;
    }

    public Set<Sdealer> getSdealers() {
        return sdealers;
    }

    public Luser sdealers(Set<Sdealer> sdealers) {
        this.sdealers = sdealers;
        return this;
    }

    public Luser addSdealer(Sdealer sdealer) {
        sdealers.add(sdealer);
        sdealer.setLuser(this);
        return this;
    }

    public Luser removeSdealer(Sdealer sdealer) {
        sdealers.remove(sdealer);
        sdealer.setLuser(null);
        return this;
    }

    public void setSdealers(Set<Sdealer> sdealers) {
        this.sdealers = sdealers;
    }

    public Set<Delegue> getDelegues() {
        return delegues;
    }

    public Luser delegues(Set<Delegue> delegues) {
        this.delegues = delegues;
        return this;
    }

    public Luser addDelegue(Delegue delegue) {
        delegues.add(delegue);
        delegue.setLuser(this);
        return this;
    }

    public Luser removeDelegue(Delegue delegue) {
        delegues.remove(delegue);
        delegue.setLuser(null);
        return this;
    }

    public void setDelegues(Set<Delegue> delegues) {
        this.delegues = delegues;
    }

    public Lzone getLzone() {
        return lzone;
    }

    public Luser lzone(Lzone lzone) {
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
        Luser luser = (Luser) o;
        if(luser.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, luser.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Luser{" +
            "id=" + id +
            ", identifiant='" + identifiant + "'" +
            '}';
    }
}
