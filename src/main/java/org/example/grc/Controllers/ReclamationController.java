package org.example.grc.Controllers;

import org.example.grc.DTO.ReclamationDTO;
import org.example.grc.Entities.Reclamation;
import org.example.grc.Services.EmailService;
import org.example.grc.Services.ReclamationServiceInter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reclamations")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ReclamationController {

    @Autowired
    private ReclamationServiceInter reclamationServiceInter;

    @Autowired
    private EmailService emailService;

    @PostMapping("/add")
    public ResponseEntity<ReclamationDTO> addReclamation(@RequestBody ReclamationDTO dto) {
        try {
            Reclamation saved = reclamationServiceInter.addReclamation(dto);
            return ResponseEntity.status(201).body(reclamationServiceInter.convertToDTO(saved));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/user/{userId}")
    public ResponseEntity<ReclamationDTO> createReclamation(@PathVariable Long userId, @RequestBody ReclamationDTO dto) {
        try {
            Reclamation saved = reclamationServiceInter.createReclamation(dto, userId);
            return ResponseEntity.status(201).body(reclamationServiceInter.convertToDTO(saved));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/allP")
    public ResponseEntity<List<ReclamationDTO>> getAllReclamations() {
        List<ReclamationDTO> dtos = reclamationServiceInter.getAllReclamations()
                .stream().map(reclamationServiceInter::convertToDTO).toList();
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/getById/{reclamId}")
    public ResponseEntity<ReclamationDTO> getReclamationById(@PathVariable Long reclamId) {
        Reclamation reclamation = reclamationServiceInter.getReclamationById(reclamId);
        if (reclamation != null) return ResponseEntity.ok(reclamationServiceInter.convertToDTO(reclamation));
        else return ResponseEntity.notFound().build();
    }

    @GetMapping("/getByUserId/{userId}")
    public ResponseEntity<List<ReclamationDTO>> getReclamationByUserId(@PathVariable Long userId) {
        List<ReclamationDTO> dtos = reclamationServiceInter.getReclamationByUserId(userId)
                .stream().map(reclamationServiceInter::convertToDTO).toList();
        return ResponseEntity.ok(dtos);
    }

    @PostMapping("/addMultiple/{userId}")
    public ResponseEntity<?> addMultipleReclamations(@PathVariable Long userId, @RequestBody List<ReclamationDTO> dtos) {
        try {
            for (ReclamationDTO dto : dtos) {
                reclamationServiceInter.createReclamation(dto, userId);
            }
            return ResponseEntity.ok("✅ Réclamations ajoutées avec succès pour l'utilisateur " + userId);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erreur lors de l'ajout : " + e.getMessage());
        }
    }

    @GetMapping("/getByEtat/{etat}")
    public List<ReclamationDTO> getByEtat(@PathVariable String etat) {
        return reclamationServiceInter.getByEtat(etat);
    }

    //==================== ADMIN ACTIONS ====================//

    @PostMapping("/updateByReference/{reference}")
    public ResponseEntity<ReclamationDTO> updateByReference(
            @PathVariable String reference,
            @RequestBody ReclamationDTO dto
    ) {
        ReclamationDTO updated = reclamationServiceInter.updateReclamationByReference(reference, dto);

        if (updated.getUser() != null && updated.getUser().getEmail() != null) {
            String subject = "🔄 Mise à jour de votre réclamation – Réf : " + reference;

            String body = String.format(
                    "Bonjour %s %s,\n\n" +
                            "Nous vous informons que votre réclamation référencée sous le numéro **%s** a été mise à jour.\n\n" +
                            "📝 **Détails de la mise à jour** :\n" +
                            "- Nouveau statut : %s\n\n" +
                            "Vous pouvez consulter votre espace personnel pour plus d’informations.\n\n" +
                            "Cordialement,\n" +
                            "L'équipe Support GRC",
                    updated.getUser().getFirstName(),
                    updated.getUser().getLastName(),
                    reference,
                    dto.getEtat()
            );

            emailService.sendHtmlEmail(updated.getUser().getEmail(), subject, body);
        }

        return ResponseEntity.ok(updated);
    }


    @PostMapping("/reject/{reference}")
    public ResponseEntity<ReclamationDTO> rejectReclamation(
            @PathVariable String reference,
            @RequestBody(required = false) String motifRejet
    ) {
        String finalComment = (motifRejet == null || motifRejet.isBlank())
                ? "Votre réclamation a été rejetée suite à une analyse approfondie."
                : motifRejet;

        Reclamation updated = reclamationServiceInter.rejectReclamation(reference, finalComment);

        if (updated != null && updated.getUser() != null && updated.getUser().getEmail() != null) {
            String subject = "❌ Réclamation rejetée – Réf : " + updated.getRecRef();

            String body = String.format(
                    "Bonjour %s %s,\n\n" +
                            "Nous vous informons que votre réclamation référencée sous le numéro **%s** a été examinée, mais n’a pas pu être validée.\n\n" +
                            "🚫 **Statut** : %s\n" +
                            "🗒️ **Motif du rejet** : %s\n\n" +
                            "Si vous avez des questions ou souhaitez apporter des précisions supplémentaires, n'hésitez pas à nous recontacter.\n\n" +
                            "Merci de votre compréhension.\n" +
                            "Cordialement,\n" +
                            "L'équipe Support GRC",
                    updated.getUser().getFirstName(),
                    updated.getUser().getLastName(),
                    updated.getRecRef(),
                    updated.getEtat(),
                    finalComment
            );

            emailService.sendHtmlEmail(updated.getUser().getEmail(), subject, body);
        }

        return updated != null
                ? ResponseEntity.ok(reclamationServiceInter.convertToDTO(updated))
                : ResponseEntity.notFound().build();
    }


    @DeleteMapping("/delete/{reference}")
    public ResponseEntity<Void> deleteReclamation(@PathVariable String reference) {
        try {
            reclamationServiceInter.deleteReclamation(reference);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

     /*@PostMapping("/accept/{reference}")
    public ResponseEntity<ReclamationDTO> acceptReclamation(@PathVariable Long reference, @RequestBody(required = false) String commentaire) {
        String finalComment = commentaire == null ? "Acceptée par l'administrateur." : commentaire;
        Reclamation updated = reclamationServiceInter.acceptReclamation(reference, finalComment);

        if (updated != null && updated.getUser() != null && updated.getUser().getEmail() != null) {
            String subject = "Réclamation acceptée : " + updated.getRecRef();
            String body = "Bonjour " + updated.getUser().getFirstName() + ",\n\n"
                    + "Votre réclamation a été acceptée.\n"
                    + "État : " + updated.getEtat() + "\n"
                    + "Commentaire : " + finalComment + "\n\n"
                    + "Merci pour votre confiance.\nSupport GRC";

            emailService.sendSimpleEmail(updated.getUser().getEmail(), subject, body);
        }

        return updated != null ? ResponseEntity.ok(reclamationServiceInter.convertToDTO(updated)) : ResponseEntity.notFound().build();
    }*/

    //==================== END ADMIN ====================//
}
