package org.example.grc.DTO;

import jakarta.validation.constraints.*;
import lombok.Data;
import org.example.grc.Entities.UserStatus;

@Data
public class RegisterDTO {

    private int id;

    @NotBlank(message = "Le prénom est requis.")
    private String firstName;

    @NotBlank(message = "Le nom est requis.")
    private String lastName;

    @NotBlank(message = "Le nom d'utilisateur est requis.")
    private String username;

    @NotBlank(message = "Le numéro de téléphone est requis.")
    @Pattern(regexp = "\\d{8}", message = "Le numéro de téléphone doit contenir exactement 8 chiffres.")
    private String phone;

    @NotBlank(message = "L'adresse e-mail est requise.")
    @Email(message = "L'adresse e-mail est invalide.")
    private String email;

    @NotBlank(message = "Le mot de passe est requis.")
    @Size(min = 8, message = "Le mot de passe doit contenir au moins 8 caractères.")
    private String password;

    @NotBlank(message = "La confirmation du mot de passe est requise.")
    private String confPassword;

    @NotBlank(message = "L'adresse est requise.")
    private String address;

    @NotBlank(message = "Le genre est requis.")
    private String gender;

    @Min(value = 1, message = "L'âge doit être supérieur à 0.")
    private int age;

    @NotBlank(message = "Le statut est requis.")
    private String status;

    @Min(value = 1000, message = "Le code postal est invalide.")
    private int codePostal;

    private UserStatus Userstatus = UserStatus.PENDING;
}
