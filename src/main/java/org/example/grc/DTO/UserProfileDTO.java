package org.example.grc.DTO;

import lombok.Data;

@Data
public class UserProfileDTO {
    private Long  id;
    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private String address;
    private String phone;
    private String gender;
    private int age;
    private String status;
    private String codePostal;
}
