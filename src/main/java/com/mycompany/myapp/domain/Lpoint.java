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
 * A Lpoint.
 */
@Entity
@Table(name = "lpoint")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Lpoint implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "lati", nullable = false)
    private Float lati;

    @NotNull
    @Column(name = "longi", nullable = false)
    private Float longi;

    @ManyToMany(mappedBy = "lpoints")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Lcarte> lcartes = new HashSet<>();

    @OneToMany(mappedBy = "lpoint")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Lcallbox> lcallboxes = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Float getLati() {
        return lati;
    }

    public Lpoint lati(Float lati) {
        this.lati = lati;
        return this;
    }

    public void setLati(Float lati) {
        this.lati = lati;
    }

    public Float getLongi() {
        return longi;
    }

    public Lpoint longi(Float longi) {
        this.longi = longi;
        return this;
    }

    public void setLongi(Float longi) {
        this.longi = longi;
    }

    public Set<Lcarte> getLcartes() {
        return lcartes;
    }

    public Lpoint lcartes(Set<Lcarte> lcartes) {
        this.lcartes = lcartes;
        return this;
    }

    public Lpoint addLcarte(Lcarte lcarte) {
        lcartes.add(lcarte);
        lcarte.getLpoints().add(this);
        return this;
    }

    public Lpoint removeLcarte(Lcarte lcarte) {
        lcartes.remove(lcarte);
        lcarte.getLpoints().remove(this);
        return this;
    }

    public void setLcartes(Set<Lcarte> lcartes) {
        this.lcartes = lcartes;
    }

    public Set<Lcallbox> getLcallboxes() {
        return lcallboxes;
    }

    public Lpoint lcallboxes(Set<Lcallbox> lcallboxes) {
        this.lcallboxes = lcallboxes;
        return this;
    }

    public Lpoint addLcallbox(Lcallbox lcallbox) {
        lcallboxes.add(lcallbox);
        lcallbox.setLpoint(this);
        return this;
    }

    public Lpoint removeLcallbox(Lcallbox lcallbox) {
        lcallboxes.remove(lcallbox);
        lcallbox.setLpoint(null);
        return this;
    }

    public void setLcallboxes(Set<Lcallbox> lcallboxes) {
        this.lcallboxes = lcallboxes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Lpoint lpoint = (Lpoint) o;
        if(lpoint.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, lpoint.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Lpoint{" +
            "id=" + id +
            ", lati='" + lati + "'" +
            ", longi='" + longi + "'" +
            '}';
    }
}
