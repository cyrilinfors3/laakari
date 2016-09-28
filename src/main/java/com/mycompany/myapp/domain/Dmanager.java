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
 * A Dmanager.
 */
@Entity
@Table(name = "dmanager")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Dmanager implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "tel", nullable = false)
    private Integer tel;

    @NotNull
    @Column(name = "agentcode", nullable = false)
    private String agentcode;

    @OneToMany(mappedBy = "dmanager")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Lprofil> lprofils = new HashSet<>();

    @ManyToOne
    private Luser luser;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getTel() {
        return tel;
    }

    public Dmanager tel(Integer tel) {
        this.tel = tel;
        return this;
    }

    public void setTel(Integer tel) {
        this.tel = tel;
    }

    public String getAgentcode() {
        return agentcode;
    }

    public Dmanager agentcode(String agentcode) {
        this.agentcode = agentcode;
        return this;
    }

    public void setAgentcode(String agentcode) {
        this.agentcode = agentcode;
    }

    public Set<Lprofil> getLprofils() {
        return lprofils;
    }

    public Dmanager lprofils(Set<Lprofil> lprofils) {
        this.lprofils = lprofils;
        return this;
    }

    public Dmanager addLprofil(Lprofil lprofil) {
        lprofils.add(lprofil);
        lprofil.setDmanager(this);
        return this;
    }

    public Dmanager removeLprofil(Lprofil lprofil) {
        lprofils.remove(lprofil);
        lprofil.setDmanager(null);
        return this;
    }

    public void setLprofils(Set<Lprofil> lprofils) {
        this.lprofils = lprofils;
    }

    public Luser getLuser() {
        return luser;
    }

    public Dmanager luser(Luser luser) {
        this.luser = luser;
        return this;
    }

    public void setLuser(Luser luser) {
        this.luser = luser;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Dmanager dmanager = (Dmanager) o;
        if(dmanager.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, dmanager.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Dmanager{" +
            "id=" + id +
            ", tel='" + tel + "'" +
            ", agentcode='" + agentcode + "'" +
            '}';
    }
}
