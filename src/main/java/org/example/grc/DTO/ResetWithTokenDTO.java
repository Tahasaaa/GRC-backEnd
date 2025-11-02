package org.example.grc.DTO;

import lombok.Data;

@Data
public class ResetWithTokenDTO {
    private String token;
    private String newPassword;
    private String confirmPassword;
}
