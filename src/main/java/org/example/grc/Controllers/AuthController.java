package org.example.grc.Controllers;

import jakarta.validation.Valid;
import org.example.grc.DTO.*;
import org.example.grc.Entities.PasswordResetToken;
import org.example.grc.Repository.PasswordResetTokenRepository;
import org.example.grc.Services.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.example.grc.Entities.Role;
import org.example.grc.Entities.UserEntity;
import org.example.grc.Entities.RoleName;
import org.example.grc.Repository.RoleRepository;
import org.example.grc.Repository.UserRepository;
import org.example.grc.Security.JWTGenerator;
import org.springframework.transaction.annotation.Transactional;


import jakarta.annotation.PostConstruct;
import java.util.Date;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTGenerator jwtGenerator;
    private final RoleRepository roleRepository;

    @Autowired
    private PasswordResetTokenRepository tokenRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager,
                          UserRepository userRepository,
                          RoleRepository roleRepository,
                          PasswordEncoder passwordEncoder,
                          JWTGenerator jwtGenerator) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtGenerator = jwtGenerator;
    }

    @PostConstruct
    public void createDefaultAdminAccount() {
        try {
            String adminEmail = "admin@Stb.com";
            if (userRepository.existsByEmail(adminEmail)) {
                UserEntity oldAdmin = userRepository.findByEmail(adminEmail);
                userRepository.delete(oldAdmin);
            }
            Role oldAdminRole = roleRepository.findByRoleName(RoleName.ADMIN);
            if (oldAdminRole != null) {
                roleRepository.delete(oldAdminRole);
            }
            Role adminRole = new Role();
            adminRole.setRoleName(RoleName.ADMIN);
            adminRole.setEventManagement(true);
            adminRole.setUserManagement(true);
            adminRole = roleRepository.save(adminRole);

            UserEntity adminUser = new UserEntity();
            adminUser.setUsername("admin");
            adminUser.setEmail(adminEmail);
            adminUser.setPassword(passwordEncoder.encode("admin_admin"));
            adminUser.setConfPassword(passwordEncoder.encode("admin_admin"));
            adminUser.setFirstName("Admin");
            adminUser.setLastName("STB");
            adminUser.setCreationDate(new Date());
            adminUser.setRoles(Set.of(adminRole));

            userRepository.save(adminUser);

            System.out.println("✅ Admin recréé avec succès");
        } catch (Exception e) {
            System.err.println(" Erreur PostConstruct : " + e.getMessage());
            e.printStackTrace();
        }
    }


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO loginDto) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginDto.getEmail(),
                            loginDto.getPassword()
                    )
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String token = jwtGenerator.generateToken(authentication);

            Optional<UserEntity> userOptional = Optional.ofNullable(userRepository.findByEmail(userDetails.getUsername()));
            if (userOptional.isPresent()) {
                UserEntity user = userOptional.get();
                AuthResponseDTO authResponseDTO = new AuthResponseDTO(token, user);
                return new ResponseEntity<>(authResponseDTO, HttpStatus.OK);
            } else {
                return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
            }

        } catch (AuthenticationException e) {
            return new ResponseEntity<>("Invalid Email or password", HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterDTO registerDto, BindingResult result) {
        System.out.println(" Tentative d'inscription avec : " + registerDto.getUsername() + " | " + registerDto.getEmail());


        if (result.hasErrors()) {
            String errorMessage = result.getFieldError().getDefaultMessage();
            System.out.println(" Erreur de validation : " + errorMessage);
            return ResponseEntity.badRequest().body(errorMessage);
        }

        if (userRepository.existsByUsername(registerDto.getUsername())) {
            System.out.println(" Nom d'utilisateur déjà utilisé : " + registerDto.getUsername());
            return ResponseEntity.badRequest().body("Nom d'utilisateur déjà utilisé !");
        }

        if (userRepository.existsByEmail(registerDto.getEmail())) {
            System.out.println("Email déjà enregistré : " + registerDto.getEmail());
            return ResponseEntity.badRequest().body("Adresse e-mail déjà enregistrée !");
        }

        if (!registerDto.getPassword().equals(registerDto.getConfPassword())) {
            System.out.println("Les mots de passe ne correspondent pas !");
            return ResponseEntity.badRequest().body("Les mots de passe ne correspondent pas !");
        }

        Role defaultRole = roleRepository.findByRoleName(RoleName.USER);
        if (defaultRole == null) {
            System.out.println("Rôle USER non trouvé !");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Rôle USER introuvable !");
        }

        UserEntity user = new UserEntity();
        user.setFirstName(registerDto.getFirstName());
        user.setLastName(registerDto.getLastName());
        user.setUsername(registerDto.getUsername());
        user.setEmail(registerDto.getEmail());
        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));
        user.setAddress(registerDto.getAddress());
        user.setAge(registerDto.getAge());
        user.setGender(registerDto.getGender());
        user.setPhone(registerDto.getPhone());
        user.setCodePostal(registerDto.getCodePostal());
        user.setStatus(registerDto.getStatus());
        user.setCreationDate(new Date());
        user.setRoles(Set.of(defaultRole));

        userRepository.save(user);
        userRepository.flush();

        System.out.println("✅ Utilisateur enregistré avec succès : " + user.getUsername());
        return ResponseEntity.ok(user);
    }


    @Transactional
    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody ForgotPasswordDTO dto) {
        UserEntity user = userRepository.findByEmail(dto.getEmail());

        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Utilisateur non trouvé !");
        }

        // Supprimer l'ancien token si existant
        tokenRepository.deleteByUser(user);  // ⬅️ Ajout essentiel

        // Générer et sauvegarder le token
        PasswordResetToken resetToken = new PasswordResetToken(user);
        tokenRepository.save(resetToken);

        String resetUrl = "http://localhost:4200/reset-password?token=" + resetToken.getToken();

        String subject = "🔐 Réinitialisation de votre mot de passe";
        String message = "Bonjour " + user.getFirstName() + ",\n\n" +
                "Voici votre lien de réinitialisation (valable 15 minutes) :\n" +
                resetUrl + "\n\n" +
                "Si vous n'avez pas demandé cette action, ignorez ce message.";

        emailService.sendHtmlEmail(user.getEmail(), subject, message);

        return ResponseEntity.ok("Un lien de réinitialisation a été envoyé à votre adresse email.");
    }


    @PostMapping("/reset-password-with-token")
    public ResponseEntity<?> resetPasswordWithToken(@RequestBody ResetWithTokenDTO dto) {
        PasswordResetToken tokenEntity = tokenRepository.findByToken(dto.getToken());

        if (tokenEntity == null || tokenEntity.getExpirationDate().before(new Date())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Token invalide ou expiré !");
        }

        if (!dto.getNewPassword().equals(dto.getConfirmPassword())) {
            return ResponseEntity.badRequest().body("Les mots de passe ne correspondent pas !");
        }

        UserEntity user = tokenEntity.getUser();
        user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        user.setConfPassword(passwordEncoder.encode(dto.getConfirmPassword()));
        userRepository.save(user);

        tokenRepository.delete(tokenEntity);

        return ResponseEntity.ok("✅ Mot de passe réinitialisé avec succès !");
    }

    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordDTO dto) {
        UserEntity user = userRepository.findByEmail(dto.getEmail());

        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Utilisateur non trouvé !");
        }

        if (!passwordEncoder.matches(dto.getOldPassword(), user.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Ancien mot de passe incorrect !");
        }

        if (!dto.getNewPassword().equals(dto.getConfirmPassword())) {
            return ResponseEntity.badRequest().body("Les mots de passe ne correspondent pas !");
        }

        user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        user.setConfPassword(passwordEncoder.encode(dto.getConfirmPassword()));
        userRepository.save(user);

        return ResponseEntity.ok("🔐 Mot de passe modifié avec succès !");
    }



}