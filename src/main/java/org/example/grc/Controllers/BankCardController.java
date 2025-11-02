package org.example.grc.Controllers;

import org.example.grc.DTO.BankCardDTO;
import org.example.grc.Services.BankCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/cards")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class BankCardController {

    @Autowired
    private BankCardService cardService;

    @PostMapping("/add")
    public ResponseEntity<?> addCard(@ModelAttribute BankCardDTO dto,
                                     @RequestParam(value = "image", required = false) MultipartFile image,
                                     Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(401).body("User not authenticated");
        }
        String email = principal.getName();
        BankCardDTO savedCard = cardService.addCard(dto, image, email);
        return ResponseEntity.ok(savedCard);
    }

    @GetMapping("/my-cards")
    public ResponseEntity<?> getUserCards(Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(401).body("User not authenticated");
        }
        List<BankCardDTO> cards = cardService.getCardsByUser(principal.getName());
        return ResponseEntity.ok(cards);
    }
}
