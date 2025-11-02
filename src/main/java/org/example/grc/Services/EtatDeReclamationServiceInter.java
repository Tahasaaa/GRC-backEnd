package org.example.grc.Services;

import org.example.grc.Entities.EtatDeReclamation;

public interface EtatDeReclamationServiceInter {
    EtatDeReclamation updateEtat(Long reclamationId, EtatDeReclamation nouvelEtat);
}
