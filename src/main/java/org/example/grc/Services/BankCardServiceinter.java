package org.example.grc.Services;

import org.example.grc.DTO.BankCardDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface BankCardServiceinter {
    BankCardDTO addCard(BankCardDTO dto, MultipartFile image, String email);
    List<BankCardDTO> getCardsByUser(String email);
}
