package com.mycompany.myapp.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A Delegue.
 */
@Entity
@Table(name = "delegue")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Delegue implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "code")
    private String code;

    @NotNull
    @Column(name = "mastersim", nullable = false)
    private Integer mastersim;

    @Column(name = "numid")
    private Integer numid;

    @NotNull
    @Column(name = "issuedate", nullable = false)
    private LocalDate issuedate;

    @NotNull
    @Column(name = "dateofbirth", nullable = false)
    private LocalDate dateofbirth;

    @NotNull
    @Column(name = "quatier", nullable = false)
    private String quatier;

    @NotNull
    @Column(name = "sex", nullable = false)
    private String sex;

    @Lob
    @Column(name = "pic")
    private byte[] pic;

    @Column(name = "pic_content_type")
    private String picContentType;

    @OneToOne
    @JoinColumn(unique = true)
    private Lprofil lprofil;

    @ManyToOne
    private Luser luser;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public Delegue code(String code) {
        this.code = code;
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getMastersim() {
        return mastersim;
    }

    public Delegue mastersim(Integer mastersim) {
        this.mastersim = mastersim;
        return this;
    }

    public void setMastersim(Integer mastersim) {
        this.mastersim = mastersim;
    }

    public Integer getNumid() {
        return numid;
    }

    public Delegue numid(Integer numid) {
        this.numid = numid;
        return this;
    }

    public void setNumid(Integer numid) {
        this.numid = numid;
    }

    public LocalDate getIssuedate() {
        return issuedate;
    }

    public Delegue issuedate(LocalDate issuedate) {
        this.issuedate = issuedate;
        return this;
    }

    public void setIssuedate(LocalDate issuedate) {
        this.issuedate = issuedate;
    }

    public LocalDate getDateofbirth() {
        return dateofbirth;
    }

    public Delegue dateofbirth(LocalDate dateofbirth) {
        this.dateofbirth = dateofbirth;
        return this;
    }

    public void setDateofbirth(LocalDate dateofbirth) {
        this.dateofbirth = dateofbirth;
    }

    public String getQuatier() {
        return quatier;
    }

    public Delegue quatier(String quatier) {
        this.quatier = quatier;
        return this;
    }

    public void setQuatier(String quatier) {
        this.quatier = quatier;
    }

    public String getSex() {
        return sex;
    }

    public Delegue sex(String sex) {
        this.sex = sex;
        return this;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public byte[] getPic() {
        return pic;
    }

    public Delegue pic(byte[] pic) {
        this.pic = pic;
        return this;
    }

    public void setPic(byte[] pic) {
        this.pic = pic;
    }

    public String getPicContentType() {
        return picContentType;
    }

    public Delegue picContentType(String picContentType) {
        this.picContentType = picContentType;
        return this;
    }

    public void setPicContentType(String picContentType) {
        this.picContentType = picContentType;
    }

    public Lprofil getLprofil() {
        return lprofil;
    }

    public Delegue lprofil(Lprofil lprofil) {
        this.lprofil = lprofil;
        return this;
    }

    public void setLprofil(Lprofil lprofil) {
        this.lprofil = lprofil;
    }

    public Luser getLuser() {
        return luser;
    }

    public Delegue luser(Luser luser) {
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
        Delegue delegue = (Delegue) o;
        if(delegue.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, delegue.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Delegue{" +
            "id=" + id +
            ", code='" + code + "'" +
            ", mastersim='" + mastersim + "'" +
            ", numid='" + numid + "'" +
            ", issuedate='" + issuedate + "'" +
            ", dateofbirth='" + dateofbirth + "'" +
            ", quatier='" + quatier + "'" +
            ", sex='" + sex + "'" +
            ", pic='" + pic + "'" +
            ", picContentType='" + picContentType + "'" +
            '}';
    }
}
