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
 * A Lfacture.
 */
@Entity
@Table(name = "lfacture")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Lfacture implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "codebill")
    private String codebill;

    @NotNull
    @Column(name = "total", nullable = false)
    private Float total;

    @NotNull
    @Column(name = "fdate", nullable = false)
    private ZonedDateTime fdate;

    @OneToMany(mappedBy = "lfacture")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Ltransactions> ltransactions = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodebill() {
        return codebill;
    }

    public Lfacture codebill(String codebill) {
        this.codebill = codebill;
        return this;
    }

    public void setCodebill(String codebill) {
        this.codebill = codebill;
    }

    public Float getTotal() {
        return total;
    }

    public Lfacture total(Float total) {
        this.total = total;
        return this;
    }

    public void setTotal(Float total) {
        this.total = total;
    }

    public ZonedDateTime getFdate() {
        return fdate;
    }

    public Lfacture fdate(ZonedDateTime fdate) {
        this.fdate = fdate;
        return this;
    }

    public void setFdate(ZonedDateTime fdate) {
        this.fdate = fdate;
    }

    public Set<Ltransactions> getLtransactions() {
        return ltransactions;
    }

    public Lfacture ltransactions(Set<Ltransactions> ltransactions) {
        this.ltransactions = ltransactions;
        return this;
    }

    public Lfacture addLtransactions(Ltransactions ltransactions) {
        this.ltransactions.add(ltransactions);
        ltransactions.setLfacture(this);
        return this;
    }

    public Lfacture removeLtransactions(Ltransactions ltransactions) {
        this.ltransactions.remove(ltransactions);
        ltransactions.setLfacture(null);
        return this;
    }

    public void setLtransactions(Set<Ltransactions> ltransactions) {
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
        Lfacture lfacture = (Lfacture) o;
        if(lfacture.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, lfacture.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Lfacture{" +
            "id=" + id +
            ", codebill='" + codebill + "'" +
            ", total='" + total + "'" +
            ", fdate='" + fdate + "'" +
            '}';
    }
}
