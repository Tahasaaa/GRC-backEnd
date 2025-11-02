package org.example.grc.Controllers;

import org.example.grc.Entities.EtatDeReclamation;
import org.example.grc.Services.EtatDeReclamationServiceInter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/etat-reclamations")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class EtatDeReclamationController {

    @Autowired
    private EtatDeReclamationServiceInter etatService;

    @PutMapping("/update/{reclamationId}")
    public EtatDeReclamation updateEtat(@PathVariable Long reclamationId, @RequestBody EtatDeReclamation etat) {
        return etatService.updateEtat(reclamationId, etat);
    }
}