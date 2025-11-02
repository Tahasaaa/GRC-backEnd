package org.example.grc.DTO;


import lombok.AllArgsConstructor;
import lombok.Data;
import org.example.grc.Entities.UserEntity;

@Data
@AllArgsConstructor
public class AuthResponseDTO {
    private String accessToken;
    private String tokenType = "Bearer";
    private UserEntity user;

    public AuthResponseDTO(String accessToken, UserEntity user) {
        this.accessToken = accessToken;
        this.user = user;
        this.tokenType = "Bearer";
    }
}
