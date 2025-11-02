package org.example.grc.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.Date;
import java.util.Objects;

@Entity
public class Reclamation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rec_id;

    private String rec_title;
    private String rec_description;

    @Column(unique = true)
    private String recRef;
    private String recType;
    @Temporal(TemporalType.DATE)
    private Date rec_date;
    private String rec_priority;
    private String rec_channel;
    @Lob
    private String commentaire;
    private String motifRejet;


    @Temporal(TemporalType.TIMESTAMP)
    private Date rec_updatedAt; // Date de dernière modification

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private UserEntity user;



    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "etat_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "FK_RECLAMATION_ETAT"))
    private EtatDeReclamation etat;


    public EtatDeReclamation getEtat() {
        return etat;
    }

    public void setEtat(EtatDeReclamation etat) {
        this.etat = etat;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public Long getRec_id() {
        return rec_id;
    }

    public void setRec_id(Long rec_id) {
        this.rec_id = rec_id;
    }

    public String getRec_title() {
        return rec_title;
    }

    public void setRec_title(String rec_title) {
        this.rec_title = rec_title;
    }

    public String getRec_description() {
        return rec_description;
    }

    public void setRec_description(String rec_description) {
        this.rec_description = rec_description;
    }

    public String getRecRef() {
        return recRef;
    }

    public void setRecRef(String recRef) {
        this.recRef = recRef;
    }

    public String getRecType() {
        return recType;
    }

    public void setRecType(String recType) {
        this.recType = recType;
    }

    public Date getRec_date() {
        return rec_date;
    }

    public void setRec_date(Date rec_date) {
        this.rec_date = rec_date;
    }


    public String getRec_priority() {
        return rec_priority;
    }

    public void setRec_priority(String rec_priority) {
        this.rec_priority = rec_priority;
    }



    public String getRec_channel() {
        return rec_channel;
    }

    public void setRec_channel(String rec_channel) {
        this.rec_channel = rec_channel;
    }

    public Date getRec_updatedAt() {
        return rec_updatedAt;
    }

    public void setRec_updatedAt(Date rec_updatedAt) {
        this.rec_updatedAt = rec_updatedAt;
    }

    public void setUserEntity(UserEntity userEntity) {
        this.user = userEntity;
    }

    public String getCommentaire() {
        return commentaire;
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }

    public String getMotifRejet() {
        return motifRejet;
    }

    public void setMotifRejet(String motifRejet) {
        this.motifRejet = motifRejet;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Reclamation that = (Reclamation) o;
        return Objects.equals(rec_id, that.rec_id) && Objects.equals(rec_title, that.rec_title) && Objects.equals(rec_description, that.rec_description) && Objects.equals(recRef, that.recRef) && Objects.equals(recType, that.recType) && Objects.equals(rec_date, that.rec_date) && Objects.equals(rec_priority, that.rec_priority) && Objects.equals(rec_channel, that.rec_channel) && Objects.equals(commentaire, that.commentaire) && Objects.equals(motifRejet, that.motifRejet) && Objects.equals(rec_updatedAt, that.rec_updatedAt) && Objects.equals(user, that.user) && Objects.equals(etat, that.etat);
    }

    @Override
    public int hashCode() {
        return Objects.hash(rec_id, rec_title, rec_description, recRef, recType, rec_date, rec_priority, rec_channel, commentaire, motifRejet, rec_updatedAt, user, etat);
    }
}