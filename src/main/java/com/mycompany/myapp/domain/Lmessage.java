package com.mycompany.myapp.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A Lmessage.
 */
@Entity
@Table(name = "lmessage")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Lmessage implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "sentdate", nullable = false)
    private LocalDate sentdate;

    @NotNull
    @Column(name = "msgcontent", nullable = false)
    private String msgcontent;

    @NotNull
    @Column(name = "senthours", nullable = false)
    private ZonedDateTime senthours;

    @NotNull
    @Column(name = "sender", nullable = false)
    private String sender;

    @NotNull
    @Column(name = "reciever", nullable = false)
    private String reciever;

    @ManyToOne
    private Luser luser;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getSentdate() {
        return sentdate;
    }

    public Lmessage sentdate(LocalDate sentdate) {
        this.sentdate = sentdate;
        return this;
    }

    public void setSentdate(LocalDate sentdate) {
        this.sentdate = sentdate;
    }

    public String getMsgcontent() {
        return msgcontent;
    }

    public Lmessage msgcontent(String msgcontent) {
        this.msgcontent = msgcontent;
        return this;
    }

    public void setMsgcontent(String msgcontent) {
        this.msgcontent = msgcontent;
    }

    public ZonedDateTime getSenthours() {
        return senthours;
    }

    public Lmessage senthours(ZonedDateTime senthours) {
        this.senthours = senthours;
        return this;
    }

    public void setSenthours(ZonedDateTime senthours) {
        this.senthours = senthours;
    }

    public String getSender() {
        return sender;
    }

    public Lmessage sender(String sender) {
        this.sender = sender;
        return this;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReciever() {
        return reciever;
    }

    public Lmessage reciever(String reciever) {
        this.reciever = reciever;
        return this;
    }

    public void setReciever(String reciever) {
        this.reciever = reciever;
    }

    public Luser getLuser() {
        return luser;
    }

    public Lmessage luser(Luser luser) {
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
        Lmessage lmessage = (Lmessage) o;
        if(lmessage.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, lmessage.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Lmessage{" +
            "id=" + id +
            ", sentdate='" + sentdate + "'" +
            ", msgcontent='" + msgcontent + "'" +
            ", senthours='" + senthours + "'" +
            ", sender='" + sender + "'" +
            ", reciever='" + reciever + "'" +
            '}';
    }
}
