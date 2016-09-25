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
 * A Lzone.
 */
@Entity
@Table(name = "lzone")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Lzone implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "datecreation", nullable = false)
    private ZonedDateTime datecreation;

    @Column(name = "datemodif")
    private ZonedDateTime datemodif;

    @NotNull
    @Column(name = "type", nullable = false)
    private String type;

    @OneToOne
    @JoinColumn(unique = true)
    private Lcarte lcarte;

    @OneToMany(mappedBy = "lzone")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Lroute> lroutes = new HashSet<>();

    @OneToOne
    @JoinColumn(unique = true)
    private Lregion lregion;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "lzone_larrondissement",
               joinColumns = @JoinColumn(name="lzones_id", referencedColumnName="ID"),
               inverseJoinColumns = @JoinColumn(name="larrondissements_id", referencedColumnName="ID"))
    private Set<Larrondissement> larrondissements = new HashSet<>();

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "lzone_lville",
               joinColumns = @JoinColumn(name="lzones_id", referencedColumnName="ID"),
               inverseJoinColumns = @JoinColumn(name="lvilles_id", referencedColumnName="ID"))
    private Set<Lville> lvilles = new HashSet<>();

    @OneToOne(mappedBy = "lzone")
    @JsonIgnore
    private Luser luser;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getDatecreation() {
        return datecreation;
    }

    public Lzone datecreation(ZonedDateTime datecreation) {
        this.datecreation = datecreation;
        return this;
    }

    public void setDatecreation(ZonedDateTime datecreation) {
        this.datecreation = datecreation;
    }

    public ZonedDateTime getDatemodif() {
        return datemodif;
    }

    public Lzone datemodif(ZonedDateTime datemodif) {
        this.datemodif = datemodif;
        return this;
    }

    public void setDatemodif(ZonedDateTime datemodif) {
        this.datemodif = datemodif;
    }

    public String getType() {
        return type;
    }

    public Lzone type(String type) {
        this.type = type;
        return this;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Lcarte getLcarte() {
        return lcarte;
    }

    public Lzone lcarte(Lcarte lcarte) {
        this.lcarte = lcarte;
        return this;
    }

    public void setLcarte(Lcarte lcarte) {
        this.lcarte = lcarte;
    }

    public Set<Lroute> getLroutes() {
        return lroutes;
    }

    public Lzone lroutes(Set<Lroute> lroutes) {
        this.lroutes = lroutes;
        return this;
    }

    public Lzone addLroute(Lroute lroute) {
        lroutes.add(lroute);
        lroute.setLzone(this);
        return this;
    }

    public Lzone removeLroute(Lroute lroute) {
        lroutes.remove(lroute);
        lroute.setLzone(null);
        return this;
    }

    public void setLroutes(Set<Lroute> lroutes) {
        this.lroutes = lroutes;
    }

    public Lregion getLregion() {
        return lregion;
    }

    public Lzone lregion(Lregion lregion) {
        this.lregion = lregion;
        return this;
    }

    public void setLregion(Lregion lregion) {
        this.lregion = lregion;
    }

    public Set<Larrondissement> getLarrondissements() {
        return larrondissements;
    }

    public Lzone larrondissements(Set<Larrondissement> larrondissements) {
        this.larrondissements = larrondissements;
        return this;
    }

    public Lzone addLarrondissement(Larrondissement larrondissement) {
        larrondissements.add(larrondissement);
        larrondissement.getLzones().add(this);
        return this;
    }

    public Lzone removeLarrondissement(Larrondissement larrondissement) {
        larrondissements.remove(larrondissement);
        larrondissement.getLzones().remove(this);
        return this;
    }

    public void setLarrondissements(Set<Larrondissement> larrondissements) {
        this.larrondissements = larrondissements;
    }

    public Set<Lville> getLvilles() {
        return lvilles;
    }

    public Lzone lvilles(Set<Lville> lvilles) {
        this.lvilles = lvilles;
        return this;
    }

    public Lzone addLville(Lville lville) {
        lvilles.add(lville);
        lville.getLzones().add(this);
        return this;
    }

    public Lzone removeLville(Lville lville) {
        lvilles.remove(lville);
        lville.getLzones().remove(this);
        return this;
    }

    public void setLvilles(Set<Lville> lvilles) {
        this.lvilles = lvilles;
    }

    public Luser getLuser() {
        return luser;
    }

    public Lzone luser(Luser luser) {
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
        Lzone lzone = (Lzone) o;
        if(lzone.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, lzone.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Lzone{" +
            "id=" + id +
            ", datecreation='" + datecreation + "'" +
            ", datemodif='" + datemodif + "'" +
            ", type='" + type + "'" +
            '}';
    }
}
