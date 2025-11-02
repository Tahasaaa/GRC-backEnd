package org.example.grc.Services;

import org.example.grc.DTO.ChatBotResponse;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class ChatBotService {

    public ChatBotResponse generateStructuredReclamation(String description, String type) {
        ChatBotResponse response = new ChatBotResponse();

        String refined = reformulate(description, type);
        response.setRefinedDescription(refined);

        double clarityScore = calculateClarityScore(description);
        response.setClarityScore(clarityScore);

        String estimatedDelay = estimateDelay(type);
        response.setEstimatedDelay(estimatedDelay);

        String nextStep = suggestNextStep(type);
        response.setNextStep(nextStep); // Il faudra ajouter ce champ dans ton DTO aussi

        return response;
    }


    private String reformulate(String desc, String type) {
        String cleaned = cleanAndEnhance(desc);

        switch (type.toLowerCase()) {
            case "carte bloquée":
                return "Nous avons détecté une anomalie liée à une carte bloquée. Voici la reformulation de votre réclamation : \"" + cleaned + "\". Nous allons l'examiner dans les plus brefs délais.";
            case "erreur de virement":
                return "Une irrégularité concernant un virement a été signalée. Détails reformulés : \"" + cleaned + "\". Votre demande sera traitée en priorité.";
            case "problème de connexion":
                return "Vous rencontrez des difficultés d'accès. Votre réclamation a été reformulée ainsi : \"" + cleaned + "\". Nous investiguons cette situation.";
            default:
                return "Réclamation liée à *" + type + "* : \"" + cleaned + "\". Merci pour votre vigilance.";
        }
    }


    private String cleanAndEnhance(String desc) {
        desc = desc.toLowerCase();

        // Remplacements dynamiques courants
        Map<String, String> replacements = Map.of(
                "probleme", "anomalie",
                "erreur", "irrégularité",
                "bloque", "inaccessible",
                "non recu", "non parvenu",
                "retard", "délai inhabituel",
                "ne fonctionne pas", "semble défaillant",
                "impossible", "difficile",
                "ne marche pas", "ne répond pas correctement"
        );

        for (Map.Entry<String, String> entry : replacements.entrySet()) {
            if (desc.contains(entry.getKey())) {
                desc = desc.replace(entry.getKey(), entry.getValue());
            }
        }

        // Majuscule en début + point final
        return capitalizeFirstLetter(desc.replaceAll("\\s+", " ").trim());
    }

    private String capitalizeFirstLetter(String text) {
        if (text == null || text.isEmpty()) return "";
        return text.substring(0, 1).toUpperCase() + text.substring(1);
    }


    private double calculateClarityScore(String text) {
        int length = text.length();
        boolean hasPunctuation = text.contains(".") || text.contains("?");
        double score = (length > 20 ? 0.7 : 0.4) + (hasPunctuation ? 0.3 : 0.1);
        return Math.min(1.0, score);
    }

    private String estimateDelay(String type) {
        switch (type.toLowerCase()) {
            case "carte bloquée": return "24 heures";
            case "erreur de virement": return "48 heures";
            default: return "72 heures";
        }
    }

    private String suggestNextStep(String type) {
        switch (type.toLowerCase()) {
            case "carte bloquée":
                return "Vous pouvez appeler le service client au 71 000 000 pour débloquer immédiatement votre carte.";
            case "erreur de virement":
                return "Veuillez vérifier votre relevé de compte ou contacter l’agence pour confirmation.";
            case "problème de connexion":
                return "Essayez de réinitialiser votre mot de passe. Si le problème persiste, contactez l’assistance.";
            default:
                return "Notre équipe va analyser la réclamation et vous recontactera si nécessaire.";
        }
    }

}