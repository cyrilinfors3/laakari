package com.mycompany.myapp.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A Lcall.
 */
@Entity
@Table(name = "lcall")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Lcall implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "emiteur", nullable = false)
    private String emiteur;

    @NotNull
    @Column(name = "recepteur", nullable = false)
    private String recepteur;

    @NotNull
    @Column(name = "calltime", nullable = false)
    private ZonedDateTime calltime;

    @ManyToOne
    @NotNull
    private Luser luser;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmiteur() {
        return emiteur;
    }

    public Lcall emiteur(String emiteur) {
        this.emiteur = emiteur;
        return this;
    }

    public void setEmiteur(String emiteur) {
        this.emiteur = emiteur;
    }

    public String getRecepteur() {
        return recepteur;
    }

    public Lcall recepteur(String recepteur) {
        this.recepteur = recepteur;
        return this;
    }

    public void setRecepteur(String recepteur) {
        this.recepteur = recepteur;
    }

    public ZonedDateTime getCalltime() {
        return calltime;
    }

    public Lcall calltime(ZonedDateTime calltime) {
        this.calltime = calltime;
        return this;
    }

    public void setCalltime(ZonedDateTime calltime) {
        this.calltime = calltime;
    }

    public Luser getLuser() {
        return luser;
    }

    public Lcall luser(Luser luser) {
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
        Lcall lcall = (Lcall) o;
        if(lcall.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, lcall.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Lcall{" +
            "id=" + id +
            ", emiteur='" + emiteur + "'" +
            ", recepteur='" + recepteur + "'" +
            ", calltime='" + calltime + "'" +
            '}';
    }
}
