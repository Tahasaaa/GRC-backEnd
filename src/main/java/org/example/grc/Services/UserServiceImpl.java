package org.example.grc.Services;

import jakarta.transaction.Transactional;
import org.example.grc.DTO.UserProfileDTO;
import org.example.grc.Entities.Role;
import org.example.grc.Entities.UserEntity;
import org.example.grc.Entities.UserStatus;
import org.example.grc.Repository.RoleRepository;
import org.example.grc.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserServiceInter {

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    private EmailService emailService;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public UserEntity addUser(UserEntity user) {
        return userRepository.save(user);
    }

    @Override
    public UserEntity userAddWR(UserEntity user) {
        // Assign default role safely
        Role role = roleRepository.findById(1L)
                .orElseThrow(() -> new RuntimeException("Role with ID 1 not found"));

        // Clear roles to avoid duplicates
        user.getRoles().clear();
        user.getRoles().add(role);

        return userRepository.save(user);
    }

    public ResponseEntity<UserEntity> login(String email, String password) {
        UserEntity user = userRepository.findByEmail(email);
        if (user != null && passwordEncoder.matches(password, user.getPassword())) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @Override
    public UserEntity addUsercryp(UserEntity user) {
        Role role = roleRepository.findById(1L)
                .orElseThrow(() -> new RuntimeException("Role with ID 1 not found"));

        // Clear roles to avoid duplicates
        user.getRoles().clear();
        user.getRoles().add(role);

        // Encrypt password
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setConfPassword(passwordEncoder.encode(user.getConfPassword()));

        return userRepository.save(user);
    }

    @Override
    public void delete(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public List<UserEntity> addUsers(List<UserEntity> users) {
        return userRepository.saveAll(users);
    }

    @Override
    public String addUserWTCP(UserEntity userEntity) {
        if (userEntity.getPassword().equals(userEntity.getConfPassword())) {
            userRepository.save(userEntity);
            return "Le client a été ajouté avec succès";
        } else {
            return "Mot de passe invalide. Il doit contenir au moins 8 caractères, dont un chiffre et un caractère spécial.";
        }
    }

    @Override
    public String addUserWTUN(UserEntity userEntity) {
        if (userRepository.existsByEmail(userEntity.getEmail())) {
            return "Email already exist";
        } else {
            userRepository.save(userEntity);
            return "User added successfully";
        }
    }

    @Override
    public UserEntity updateUserPost(UserEntity userEntity, Long id) {
        return userRepository.findById(id).map(user -> {
            user.setFirstName(userEntity.getFirstName());
            user.setLastName(userEntity.getLastName());
            user.setPhone(userEntity.getPhone());
            user.setPassword(userEntity.getPassword());
            user.setConfPassword(userEntity.getConfPassword());
            return userRepository.save(user);
        }).orElse(null);
    }

    @Override
    public List<UserEntity> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public UserEntity getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public UserProfileDTO getUserProfile(String email) {
        return userRepository.findByUsername(email).map(user -> {
            UserProfileDTO dto = new UserProfileDTO();
            dto.setId((long) user.getId()); // cast int to Long
            dto.setFirstName(user.getFirstName());
            dto.setLastName(user.getLastName());
            dto.setEmail(user.getEmail());
            dto.setAddress(user.getAddress());
            dto.setPhone(user.getPhone());
            dto.setGender(user.getGender());
            dto.setStatus(user.getStatus());
            dto.setAge(user.getAge());
            dto.setUsername(user.getUsername());
            dto.setCodePostal(String.valueOf(user.getCodePostal()));
            return dto;
        }).orElse(null);
    }

    @Override
    public UserProfileDTO updateUserProfile(String email, UserProfileDTO updatedProfile) {
        return Optional.ofNullable(userRepository.findByEmail(email)).map(user -> {
            user.setFirstName(updatedProfile.getFirstName());
            user.setLastName(updatedProfile.getLastName());
            user.setAddress(updatedProfile.getAddress());
            user.setPhone(updatedProfile.getPhone());
            user.setGender(updatedProfile.getGender());
            user.setStatus(updatedProfile.getStatus());
            user.setAge(updatedProfile.getAge());
            user.setUsername(updatedProfile.getUsername());
            user.setCodePostal(Integer.parseInt(updatedProfile.getCodePostal())); // convert String to int
            return userRepository.save(user);
        }).map(updatedUser -> {
            UserProfileDTO dto = new UserProfileDTO();
            dto.setId((long) updatedUser.getId()); // cast int to Long
            dto.setFirstName(updatedUser.getFirstName());
            dto.setLastName(updatedUser.getLastName());
            dto.setUsername(updatedUser.getUsername());
            dto.setEmail(updatedUser.getEmail());
            dto.setAddress(updatedUser.getAddress());
            dto.setPhone(updatedUser.getPhone());
            dto.setGender(updatedUser.getGender());
            dto.setStatus(updatedUser.getStatus());
            dto.setAge(updatedUser.getAge());
            dto.setCodePostal(String.valueOf(updatedUser.getCodePostal()));
            return dto;
        }).orElse(null);
    }


    @Override
    @Transactional
    public UserEntity updateUserStatus(Long id, UserStatus userStatus) {
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User with id " + id + " not found"));

        user.setUserStatut(userStatus);
        userRepository.save(user);

        // ✅ Email dynamique selon le statut
        if (userStatus == UserStatus.APPROVED) {
            String html = """
            <html>
              <body>
                <p>Bonjour <b>%s</b>,</p>
                <p>Votre compte a été 
                   <span style='color:green;font-weight:bold'>approuvé</span> avec succès 🎉.</p>
                <p>
                  👉 <a href="http://localhost:4200/reclamations"
                       style="background:#4CAF50;color:white;padding:10px 15px;
                              text-decoration:none;border-radius:6px;">
                       Accéder à mes réclamations
                  </a>
                </p>
                <br>
                Merci pour votre confiance.<br>
                <b>L’équipe Support GRC</b>
              </body>
            </html>
            """.formatted(user.getFirstName());

            emailService.sendHtmlEmail(user.getEmail(), "✅ Votre compte a été approuvé", html);

        } else if (userStatus == UserStatus.REJECTED) {
            String html = """
            <html>
              <body>
                <p>Bonjour <b>%s</b>,</p>
                <p>Nous sommes désolés de vous informer que 
                   votre compte a été <span style='color:red;font-weight:bold'>rejeté</span> ❌.</p>
                <p>Pour plus d’informations, veuillez contacter notre support.</p>
                <br>
                Merci de votre compréhension.<br>
                <b>L’équipe Support GRC</b>
              </body>
            </html>
            """.formatted(user.getFirstName());

            emailService.sendHtmlEmail(user.getEmail(), "❌ Votre compte a été rejeté", html);
        }

        return user;
    }

}
