package org.example.grc.Services;

import jakarta.transaction.Transactional;
import org.example.grc.DTO.ReclamationDTO;
import org.example.grc.Entities.EtatDeReclamation;
import org.example.grc.Entities.RecEtat;
import org.example.grc.Entities.Reclamation;
import org.example.grc.Entities.UserEntity;
import org.example.grc.Repository.ReclamationRepository;
import org.example.grc.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.Optional;
import java.util.stream.Collectors;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class ReclamationServiceImpl implements ReclamationServiceInter {

    @Autowired
    private ReclamationRepository reclamationRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    private String generateReference() {
        String prefix = "REC-";
        String datePart = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        int random = (int)(Math.random() * 9000) + 1000;
        return prefix + datePart + "-" + random;
    }



    @Override
    public Reclamation addReclamation(ReclamationDTO dto) {
        Reclamation reclamation = new Reclamation();
        reclamation.setRec_title(dto.getTitre());
        reclamation.setRec_description(dto.getDescription());
        reclamation.setRecRef(generateReference());
        reclamation.setRecType(dto.getType());
        reclamation.setRec_priority(dto.getPriorite());
        reclamation.setRec_channel(dto.getCanal());
        reclamation.setCommentaire(dto.getCommentaire()); 

        // parse date
        if (dto.getDateCreation() != null) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date date = sdf.parse(dto.getDateCreation());
                reclamation.setRec_date(date);
            } catch (ParseException e) {
                reclamation.setRec_date(new Date());
            }
        } else {
            reclamation.setRec_date(new Date());
        }
        reclamation.setRec_updatedAt(new Date());

        // Etat
        EtatDeReclamation etat = new EtatDeReclamation();
        etat.setRecEtat(RecEtat.EN_ATTENTE);
        etat.setCommentaire("Réclamation créée");
        etat.setReclamation(reclamation);
        reclamation.setEtat(etat);

        return reclamationRepository.save(reclamation);
    }

    @Override
    public Reclamation createReclamation(ReclamationDTO dto, Long userId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));

        Reclamation reclamation = new Reclamation();
        reclamation.setRec_title(dto.getTitre());
        reclamation.setRec_description(dto.getDescription());
        reclamation.setRecRef(generateReference());
        reclamation.setRecType(dto.getType());
        reclamation.setRec_priority(dto.getPriorite());
        reclamation.setRec_channel(dto.getCanal());
        reclamation.setCommentaire(dto.getCommentaire());

        if (dto.getDateCreation() != null) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date date = sdf.parse(dto.getDateCreation());
                reclamation.setRec_date(date);
            } catch (ParseException e) {
                reclamation.setRec_date(new Date());
            }
        } else {
            reclamation.setRec_date(new Date());
        }

        reclamation.setRec_updatedAt(new Date());
        reclamation.setUser(user);

        EtatDeReclamation etat = new EtatDeReclamation();
        etat.setRecEtat(RecEtat.EN_ATTENTE);
        etat.setCommentaire("Réclamation créée");
        etat.setReclamation(reclamation);
        reclamation.setEtat(etat);

        return reclamationRepository.save(reclamation);
    }

    @Override
    public Reclamation updateReclamation(Long reclamId, ReclamationDTO dto) {
        return reclamationRepository.findById(reclamId).map(existing -> {
            existing.setRec_title(dto.getTitre());
            existing.setRec_description(dto.getDescription());
            existing.setRecRef(dto.getReference());
            existing.setRecType(dto.getType());
            existing.setRec_priority(dto.getPriorite());
            existing.setRec_channel(dto.getCanal());
            existing.setCommentaire(dto.getCommentaire());
            existing.setRec_updatedAt(new Date());
            return reclamationRepository.save(existing);
        }).orElse(null);
    }

    public ReclamationDTO convertToDTO(Reclamation reclamation) {
        ReclamationDTO dto = new ReclamationDTO();

        dto.setReference(reclamation.getRecRef());
        dto.setTitre(reclamation.getRec_title());
        dto.setDescription(reclamation.getRec_description());
        dto.setType(reclamation.getRecType());
        dto.setPriorite(reclamation.getRec_priority());
        dto.setCanal(reclamation.getRec_channel());
        dto.setCommentaire(reclamation.getCommentaire());
        dto.setMotifRejet(reclamation.getMotifRejet());
        

        if (reclamation.getRec_date() != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            dto.setDateCreation(sdf.format(reclamation.getRec_date()));
        }

        if (reclamation.getEtat() != null && reclamation.getEtat().getRecEtat() != null) {
            dto.setEtat(reclamation.getEtat().getRecEtat().name());
        } else {
            dto.setEtat(null);
        }

        return dto;
    }


    @Override
    public List<Reclamation> getAllReclamations() {
        return reclamationRepository.findAll();
    }

    @Override
    public Reclamation getReclamationById(Long reclamId) {
        return reclamationRepository.findById(reclamId).orElse(null);
    }

    @Override
    public List<Reclamation> getReclamationByUserId(Long userId) {
        return reclamationRepository.findByUserId(userId);
    }

    private Reclamation updateEtatReclamation(Long reclamId, RecEtat etat, String commentaire) {
        return reclamationRepository.findById(reclamId).map(reclamation -> {
            EtatDeReclamation etatReclamation = reclamation.getEtat();
            if (etatReclamation == null) {
                etatReclamation = new EtatDeReclamation();
                etatReclamation.setReclamation(reclamation);
            }
            etatReclamation.setRecEtat(etat);
            etatReclamation.setCommentaire(commentaire);
            reclamation.setEtat(etatReclamation);
            reclamation.setRec_updatedAt(new Date());
            Reclamation saved = reclamationRepository.save(reclamation);

            // Envoi de l'email
            UserEntity user = reclamation.getUser();
            if (user != null && user.getEmail() != null) {
                String subject = "Mise à jour de votre réclamation " + reclamation.getRecRef();
                StringBuilder body = new StringBuilder();
                body.append("Bonjour ").append(user.getFirstName()).append(",\n\n");
                body.append("Votre réclamation intitulée \"").append(reclamation.getRec_title()).append("\" a été mise à jour.\n");
                body.append("Nouvel état : ").append(etat.toString()).append("\n");
                body.append("Commentaire : ").append(commentaire != null ? commentaire : "Aucun").append("\n\n");
                body.append("Merci de votre confiance.\n");
                body.append("Cordialement,\nL'équipe Support");

                emailService.sendHtmlEmail(user.getEmail(), subject, body.toString());
            }

            return saved;
        }).orElse(null);
    }

    @Override
    public void deleteReclamation(Long reclamId) {
        reclamationRepository.deleteById(reclamId);
    }

    @Override
    public List<Reclamation> addMultipleReclamations(List<Reclamation> reclamations, Long userId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));

        for (Reclamation reclamation : reclamations) {
            reclamation.setUser(user);
            reclamation.setRec_date(new Date());
            reclamation.setRec_updatedAt(new Date());

            if (reclamation.getEtat() == null) {
                EtatDeReclamation etat = new EtatDeReclamation();
                etat.setRecEtat(RecEtat.EN_ATTENTE);
                etat.setCommentaire("Réclamation créée");
                etat.setReclamation(reclamation);
                reclamation.setEtat(etat);
            }
            reclamationRepository.save(reclamation);
        }
        return reclamationRepository.findByUserId(userId);
    }

    private ReclamationDTO mapToDTO(Reclamation reclamation) {
        ReclamationDTO dto = new ReclamationDTO();
        dto.setReference(reclamation.getRecRef());
        dto.setType(reclamation.getRecType());
        dto.setDateCreation(reclamation.getRec_date().toString());
        dto.setPriorite(reclamation.getRec_priority());
        dto.setEtat(reclamation.getEtat().getRecEtat().toString());
        dto.setDescription(reclamation.getRec_description());
        dto.setTitre(reclamation.getRec_title());
        dto.setCanal(reclamation.getRec_channel());
        dto.setCommentaire(reclamation.getCommentaire());
        return dto;
    }



    @Override
    public List<ReclamationDTO> getByEtat(String etat) {
        RecEtat recEtatEnum;
        try {
            recEtatEnum = RecEtat.valueOf(etat.toUpperCase().replace('é', 'E').replace('è', 'E').replace('ê', 'E'));
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("État invalide : " + etat);
        }

        List<Reclamation> recs = reclamationRepository.findByEtat_RecEtat(recEtatEnum);
        return recs.stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    @Override
    public ReclamationDTO updateReclamationByReference(String reference, ReclamationDTO dto) {
        List<Reclamation> reclamations = reclamationRepository.findByRecRef(reference);

        if (reclamations.isEmpty()) {
            throw new RuntimeException("Réclamation avec la référence " + reference + " introuvable.");
        }

        Reclamation reclamation = reclamations.get(0);

        if (dto.getType() != null) reclamation.setRecType(dto.getType());
        if (dto.getPriorite() != null) reclamation.setRec_priority(dto.getPriorite());

        if (dto.getEtat() != null) {
            try {
                String etatFormatted = dto.getEtat()
                        .toUpperCase()
                        .replace("É", "E")
                        .replace("È", "E")
                        .replace("Ê", "E")
                        .replace("Â", "A")
                        .replace("Ô", "O")
                        .replace("Î", "I")
                        .replace("Ù", "U")
                        .replace("Û", "U")
                        .replace(" ", "_");
                System.out.println("Etat formatte pour enum: '" + etatFormatted + "'");

                // Add this check here:
                if (etatFormatted.equals("REJETEE")) {
                    throw new RuntimeException("Pour rejeter une réclamation, utilisez l'endpoint dédié /reject/{reference}.");
                }

                RecEtat recEtatEnum = RecEtat.valueOf(etatFormatted);
                EtatDeReclamation etat = reclamation.getEtat();
                if (etat == null) {
                    etat = new EtatDeReclamation();
                    etat.setReclamation(reclamation);
                }
                etat.setRecEtat(recEtatEnum);
                reclamation.setEtat(etat);

                // Remove this block or keep it? For REJETEE it's not needed here anymore.
                if (recEtatEnum == RecEtat.REJETEE) {
                    if (dto.getCommentaire() != null) {
                        reclamation.setCommentaire(dto.getCommentaire());
                    }
                    if (dto.getMotifRejet() != null) {
                        reclamation.setMotifRejet(dto.getMotifRejet());
                    }
                }

            } catch (IllegalArgumentException e) {
                throw new RuntimeException("État invalide : " + dto.getEtat());
            }
        }

        reclamation.setRec_updatedAt(new Date());

        Reclamation updated = reclamationRepository.save(reclamation);

        ReclamationDTO updatedDTO = new ReclamationDTO();
        updatedDTO.setReference(updated.getRecRef());
        updatedDTO.setType(updated.getRecType());
        updatedDTO.setPriorite(updated.getRec_priority());
        updatedDTO.setTitre(updated.getRec_title());
        updatedDTO.setDescription(updated.getRec_description());
        updatedDTO.setCanal(updated.getRec_channel());
        updatedDTO.setDateCreation(new SimpleDateFormat("yyyy-MM-dd").format(updated.getRec_date()));
        updatedDTO.setCommentaire(updated.getCommentaire());
        updatedDTO.setEtat(updated.getEtat() != null ? updated.getEtat().toString() : null);
        updatedDTO.setMotifRejet(updated.getMotifRejet());

        return updatedDTO;
    }


    @Override
    @Transactional
    public void deleteReclamation(String reference) {
        List<Reclamation> reclamations = reclamationRepository.findByRecRef(reference);

        if (reclamations == null || reclamations.isEmpty()) {
            throw new RuntimeException("Réclamation introuvable avec la référence : " + reference);
        }

        reclamationRepository.deleteAll(reclamations);
    }

    public Reclamation rejectReclamation(String reference, String motifRejet) {
        List<Reclamation> reclamations = reclamationRepository.findByRecRef(reference);
        if (reclamations.isEmpty()) {
            throw new RuntimeException("Réclamation avec la référence " + reference + " introuvable.");
        }
        Reclamation reclamation = reclamations.get(0);

        EtatDeReclamation etat = reclamation.getEtat();
        if (etat == null) {
            etat = new EtatDeReclamation();
            etat.setReclamation(reclamation);
        }

        etat.setRecEtat(RecEtat.REJETEE);
        reclamation.setEtat(etat);

        reclamation.setCommentaire(motifRejet != null ? motifRejet : "Rejetée par l'administrateur.");
        reclamation.setRec_updatedAt(new Date());

        return reclamationRepository.save(reclamation);
    }


}
