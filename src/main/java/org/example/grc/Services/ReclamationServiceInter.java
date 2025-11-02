package org.example.grc.Services;

import org.example.grc.DTO.ReclamationDTO;
import org.example.grc.Entities.Reclamation;

import java.util.List;

public interface ReclamationServiceInter {


    Reclamation addReclamation(ReclamationDTO dto);

    Reclamation createReclamation(ReclamationDTO dto, Long userId);

    Reclamation updateReclamation(Long id, ReclamationDTO dto);

    ReclamationDTO convertToDTO(Reclamation reclamation);

    List<Reclamation> getAllReclamations();

    Reclamation getReclamationById(Long reclamId);

    List<Reclamation> getReclamationByUserId(Long userId);

    //Reclamation acceptReclamation(String reference, String commentaire);

    Reclamation rejectReclamation(String reference, String motifRejet);

    void deleteReclamation(Long reclamId);

    List<Reclamation> addMultipleReclamations(List<Reclamation> reclamations, Long userId);

    List<ReclamationDTO> getByEtat(String etat);

    ReclamationDTO updateReclamationByReference(String reference, ReclamationDTO dto);


    void deleteReclamation(String recRef);
}
