package org.example.grc.DTO;

import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class BankCardDTO {
    private String maskedCardNumber;
    private String cardHolderName;
    private String expirationDate;
    private String imagePath;

}

