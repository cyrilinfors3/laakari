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
 * A Lcarte.
 */
@Entity
@Table(name = "lcarte")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Lcarte implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "code")
    private String code;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @NotNull
    @JoinTable(name = "lcarte_lpoint",
               joinColumns = @JoinColumn(name="lcartes_id", referencedColumnName="ID"),
               inverseJoinColumns = @JoinColumn(name="lpoints_id", referencedColumnName="ID"))
    private Set<Lpoint> lpoints = new HashSet<>();

    @OneToOne(mappedBy = "lcarte")
    @JsonIgnore
    private Lzone lzone;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public Lcarte code(String code) {
        this.code = code;
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Set<Lpoint> getLpoints() {
        return lpoints;
    }

    public Lcarte lpoints(Set<Lpoint> lpoints) {
        this.lpoints = lpoints;
        return this;
    }

    public Lcarte addLpoint(Lpoint lpoint) {
        lpoints.add(lpoint);
        lpoint.getLcartes().add(this);
        return this;
    }

    public Lcarte removeLpoint(Lpoint lpoint) {
        lpoints.remove(lpoint);
        lpoint.getLcartes().remove(this);
        return this;
    }

    public void setLpoints(Set<Lpoint> lpoints) {
        this.lpoints = lpoints;
    }

    public Lzone getLzone() {
        return lzone;
    }

    public Lcarte lzone(Lzone lzone) {
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
        Lcarte lcarte = (Lcarte) o;
        if(lcarte.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, lcarte.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Lcarte{" +
            "id=" + id +
            ", code='" + code + "'" +
            '}';
    }
}
