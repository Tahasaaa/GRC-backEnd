package org.example.grc.DTO;

import jakarta.persistence.Lob;
import org.example.grc.Entities.UserEntity;

public class ReclamationDTO {

    private String reference;
    private String type;
    private String dateCreation;
    private String priorite;
    private String etat;
    private String description;
    private String titre;
    private String canal;
    @Lob
    private String commentaire;// FIXED typo here
    private String motifRejet;
    private UserEntity user;



    public ReclamationDTO() {}

    public ReclamationDTO(String reference, String type, String dateCreation,
                          String priorite, String etat, String description,
                          String titre, String canal, String commentaire, String motifRejet) {
        this.reference = reference;
        this.type = type;
        this.dateCreation = dateCreation;
        this.priorite = priorite;
        this.etat = etat;
        this.description = description;
        this.titre = titre;
        this.canal = canal;
        this.commentaire = commentaire;
        this.motifRejet = motifRejet;
    }

    public String getCommentaire() { return commentaire; }
    public void setCommentaire(String commentaire) { this.commentaire = commentaire; }


    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(String dateCreation) {
        this.dateCreation = dateCreation;
    }

    public String getPriorite() {
        return priorite;
    }

    public void setPriorite(String priorite) {
        this.priorite = priorite;
    }

    public String getEtat() {
        return etat;
    }

    public void setEtat(String etat) {
        this.etat = etat;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getCanal() {
        return canal;
    }

    public void setCanal(String canal) {
        this.canal = canal;
    }

    public String getMotifRejet() {
        return motifRejet;
    }

    public void setMotifRejet(String motifRejet) {
        this.motifRejet = motifRejet;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }
}
