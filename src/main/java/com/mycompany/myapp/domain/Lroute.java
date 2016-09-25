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
 * A Lroute.
 */
@Entity
@Table(name = "lroute")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Lroute implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "nom", nullable = false)
    private String nom;

    @Column(name = "quatier")
    private String quatier;

    @Column(name = "ville")
    private String ville;

    @OneToMany(mappedBy = "lroute")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Lcallbox> lcallboxes = new HashSet<>();

    @ManyToOne
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

    public Lroute nom(String nom) {
        this.nom = nom;
        return this;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getQuatier() {
        return quatier;
    }

    public Lroute quatier(String quatier) {
        this.quatier = quatier;
        return this;
    }

    public void setQuatier(String quatier) {
        this.quatier = quatier;
    }

    public String getVille() {
        return ville;
    }

    public Lroute ville(String ville) {
        this.ville = ville;
        return this;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public Set<Lcallbox> getLcallboxes() {
        return lcallboxes;
    }

    public Lroute lcallboxes(Set<Lcallbox> lcallboxes) {
        this.lcallboxes = lcallboxes;
        return this;
    }

    public Lroute addLcallbox(Lcallbox lcallbox) {
        lcallboxes.add(lcallbox);
        lcallbox.setLroute(this);
        return this;
    }

    public Lroute removeLcallbox(Lcallbox lcallbox) {
        lcallboxes.remove(lcallbox);
        lcallbox.setLroute(null);
        return this;
    }

    public void setLcallboxes(Set<Lcallbox> lcallboxes) {
        this.lcallboxes = lcallboxes;
    }

    public Lzone getLzone() {
        return lzone;
    }

    public Lroute lzone(Lzone lzone) {
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
        Lroute lroute = (Lroute) o;
        if(lroute.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, lroute.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Lroute{" +
            "id=" + id +
            ", nom='" + nom + "'" +
            ", quatier='" + quatier + "'" +
            ", ville='" + ville + "'" +
            '}';
    }
}
