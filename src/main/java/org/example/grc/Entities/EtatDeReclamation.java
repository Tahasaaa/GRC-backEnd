package org.example.grc.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.Optional;

@Entity
public class EtatDeReclamation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "rec_etat", length = 30)
    private RecEtat recEtat;

    private String commentaire;

    @JsonIgnore
    @OneToOne(mappedBy = "etat")
    private Reclamation reclamation;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public RecEtat getRecEtat() {
        return recEtat;
    }

    public void setRecEtat(RecEtat recEtat) {
        this.recEtat = recEtat;
    }

    public String getCommentaire() {
        return commentaire;
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }

    public Reclamation getReclamation() {
        return reclamation;
    }

    public void setReclamation(Reclamation reclamation) {
        this.reclamation = reclamation;
    }

    @Override
    public String toString() {
        return recEtat != null ? recEtat.toString() : "";
    }


}