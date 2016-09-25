package com.mycompany.myapp.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A Lvisit.
 */
@Entity
@Table(name = "lvisit")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Lvisit implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "date", nullable = false)
    private ZonedDateTime date;

    @NotNull
    @Column(name = "contentv", nullable = false)
    private String contentv;

    @Column(name = "durrationv")
    private String durrationv;

    @Column(name = "staff")
    private String staff;

    @ManyToOne
    @NotNull
    private Lcallbox lcallbox;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getDate() {
        return date;
    }

    public Lvisit date(ZonedDateTime date) {
        this.date = date;
        return this;
    }

    public void setDate(ZonedDateTime date) {
        this.date = date;
    }

    public String getContentv() {
        return contentv;
    }

    public Lvisit contentv(String contentv) {
        this.contentv = contentv;
        return this;
    }

    public void setContentv(String contentv) {
        this.contentv = contentv;
    }

    public String getDurrationv() {
        return durrationv;
    }

    public Lvisit durrationv(String durrationv) {
        this.durrationv = durrationv;
        return this;
    }

    public void setDurrationv(String durrationv) {
        this.durrationv = durrationv;
    }

    public String getStaff() {
        return staff;
    }

    public Lvisit staff(String staff) {
        this.staff = staff;
        return this;
    }

    public void setStaff(String staff) {
        this.staff = staff;
    }

    public Lcallbox getLcallbox() {
        return lcallbox;
    }

    public Lvisit lcallbox(Lcallbox lcallbox) {
        this.lcallbox = lcallbox;
        return this;
    }

    public void setLcallbox(Lcallbox lcallbox) {
        this.lcallbox = lcallbox;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Lvisit lvisit = (Lvisit) o;
        if(lvisit.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, lvisit.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Lvisit{" +
            "id=" + id +
            ", date='" + date + "'" +
            ", contentv='" + contentv + "'" +
            ", durrationv='" + durrationv + "'" +
            ", staff='" + staff + "'" +
            '}';
    }
}
