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
 * A Dealer.
 */
@Entity
@Table(name = "dealer")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Dealer implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "shopcode", nullable = false)
    private String shopcode;

    @NotNull
    @Column(name = "sigle", nullable = false)
    private String sigle;

    @NotNull
    @Column(name = "mastersim", nullable = false)
    private Integer mastersim;

    @Lob
    @Column(name = "logo")
    private byte[] logo;

    @Column(name = "logo_content_type")
    private String logoContentType;

    @OneToMany(mappedBy = "dealer")
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

    public String getShopcode() {
        return shopcode;
    }

    public Dealer shopcode(String shopcode) {
        this.shopcode = shopcode;
        return this;
    }

    public void setShopcode(String shopcode) {
        this.shopcode = shopcode;
    }

    public String getSigle() {
        return sigle;
    }

    public Dealer sigle(String sigle) {
        this.sigle = sigle;
        return this;
    }

    public void setSigle(String sigle) {
        this.sigle = sigle;
    }

    public Integer getMastersim() {
        return mastersim;
    }

    public Dealer mastersim(Integer mastersim) {
        this.mastersim = mastersim;
        return this;
    }

    public void setMastersim(Integer mastersim) {
        this.mastersim = mastersim;
    }

    public byte[] getLogo() {
        return logo;
    }

    public Dealer logo(byte[] logo) {
        this.logo = logo;
        return this;
    }

    public void setLogo(byte[] logo) {
        this.logo = logo;
    }

    public String getLogoContentType() {
        return logoContentType;
    }

    public Dealer logoContentType(String logoContentType) {
        this.logoContentType = logoContentType;
        return this;
    }

    public void setLogoContentType(String logoContentType) {
        this.logoContentType = logoContentType;
    }

    public Set<Lprofil> getLprofils() {
        return lprofils;
    }

    public Dealer lprofils(Set<Lprofil> lprofils) {
        this.lprofils = lprofils;
        return this;
    }

    public Dealer addLprofil(Lprofil lprofil) {
        lprofils.add(lprofil);
        lprofil.setDealer(this);
        return this;
    }

    public Dealer removeLprofil(Lprofil lprofil) {
        lprofils.remove(lprofil);
        lprofil.setDealer(null);
        return this;
    }

    public void setLprofils(Set<Lprofil> lprofils) {
        this.lprofils = lprofils;
    }

    public Luser getLuser() {
        return luser;
    }

    public Dealer luser(Luser luser) {
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
        Dealer dealer = (Dealer) o;
        if(dealer.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, dealer.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Dealer{" +
            "id=" + id +
            ", shopcode='" + shopcode + "'" +
            ", sigle='" + sigle + "'" +
            ", mastersim='" + mastersim + "'" +
            ", logo='" + logo + "'" +
            ", logoContentType='" + logoContentType + "'" +
            '}';
    }
}
