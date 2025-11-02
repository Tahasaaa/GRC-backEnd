package org.example.grc.Services;

import org.example.grc.DTO.BankCardDTO;
import org.example.grc.Entities.BankCard;
import org.example.grc.Entities.UserEntity;
import org.example.grc.Repository.BankCardRepository;
import org.example.grc.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class BankCardService implements BankCardServiceinter {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BankCardRepository bankCardRepository;

    @Override
    public BankCardDTO addCard(BankCardDTO dto, MultipartFile image, String email) {
        UserEntity user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("Utilisateur non trouvé");
        }

        String cardNumber = dto.getCardHolderName();
        if (cardNumber == null || cardNumber.isEmpty()) {
            throw new IllegalArgumentException("Le numéro de carte est obligatoire");
        }

        String masked = maskCardNumber(cardNumber);

        // Enregistrer l’image si elle existe
        String imagePath = null;
        if (image != null && !image.isEmpty()) {
            imagePath = saveImage(image);
        }

        BankCard card = new BankCard();
        card.setMaskedCardNumber(masked);
        card.setCardHolderName(dto.getCardHolderName());
        card.setExpirationDate(dto.getExpirationDate());
        card.setImagePath(imagePath);
        card.setUser(user);

        bankCardRepository.save(card);

        return toResponseDTO(card);
    }

    @Override
    public List<BankCardDTO> getCardsByUser(String email) {
        return bankCardRepository.findByUser_Email(email)
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    private String maskCardNumber(String cardNumber) {
        if (cardNumber == null || cardNumber.length() < 4) return "**** **** ****";
        String last4 = cardNumber.substring(cardNumber.length() - 4);
        return "**** **** **** " + last4;
    }

    private String saveImage(MultipartFile file) {
        try {
            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            Path path = Paths.get("uploads/cards/" + fileName);
            Files.createDirectories(path.getParent());
            Files.write(path, file.getBytes());
            return "/uploads/cards/" + fileName; // accessible via config
        } catch (IOException e) {
            throw new RuntimeException("Échec de l'enregistrement de l'image.", e);
        }
    }

    private BankCardDTO toResponseDTO(BankCard card) {
        BankCardDTO dto = new BankCardDTO();
        dto.setMaskedCardNumber(card.getMaskedCardNumber());
        dto.setCardHolderName(card.getCardHolderName());
        dto.setExpirationDate(card.getExpirationDate());
        dto.setImagePath(card.getImagePath());
        return dto;
    }
}
