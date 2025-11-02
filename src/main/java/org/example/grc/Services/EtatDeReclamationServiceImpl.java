package org.example.grc.Services;

import org.example.grc.Entities.EtatDeReclamation;
import org.example.grc.Entities.Reclamation;
import org.example.grc.Entities.RecEtat;
import org.example.grc.Repository.EtatDeReclamationRepository;
import org.example.grc.Repository.ReclamationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EtatDeReclamationServiceImpl implements EtatDeReclamationServiceInter {

    @Autowired
    private EtatDeReclamationRepository etatDeReclamationRepository;

    @Autowired
    private ReclamationRepository reclamationRepo;

    @Override
    public EtatDeReclamation updateEtat(Long reclamationId, EtatDeReclamation nouvelEtat) {
        Reclamation reclamation = reclamationRepo.findById(reclamationId)
                .orElseThrow(() -> new RuntimeException("Réclamation introuvable"));

        if (nouvelEtat.getRecEtat() == null) {
            throw new IllegalArgumentException("L'état de la réclamation ne peut pas être nul.");
        }

        RecEtat newEtat = nouvelEtat.getRecEtat();
        boolean isValid = false;
        for (RecEtat etatPossible : RecEtat.values()) {
            if (etatPossible == newEtat) {
                isValid = true;
                break;
            }
        }

        if (!isValid) {
            throw new IllegalArgumentException("État invalide : " + newEtat);
        }

        EtatDeReclamation etat = reclamation.getEtat();
        if (etat == null) {
            etat = new EtatDeReclamation();
        }

        etat.setRecEtat(newEtat);
        etat.setCommentaire(nouvelEtat.getCommentaire());
        etat.setReclamation(reclamation);

        EtatDeReclamation saved = etatDeReclamationRepository.save(etat);
        reclamation.setEtat(saved);
        reclamationRepo.save(reclamation);

        return saved;
    }
}
