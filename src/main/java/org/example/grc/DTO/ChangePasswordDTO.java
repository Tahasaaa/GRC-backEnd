package org.example.grc.DTO;

import lombok.Data;

@Data
public class ChangePasswordDTO {
    private String email;
    private String oldPassword;
    private String newPassword;
    private String confirmPassword;
}
