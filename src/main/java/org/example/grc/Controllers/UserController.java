package org.example.grc.Controllers;

import org.example.grc.DTO.UserProfileDTO;
import org.example.grc.Entities.UserEntity;
import org.example.grc.Entities.UserStatus;
import org.example.grc.Services.UserServiceInter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class UserController {

    @Autowired
    private UserServiceInter userServiceInter;

    /*@PostMapping("/add")
    public UserEntity addUser(@RequestBody UserEntity user) {
        return userServiceInter.addUser(user);
    }*/

    @PostMapping("/addWR")
    public UserEntity addUserWR(@RequestBody UserEntity user) {
        return userServiceInter.userAddWR(user);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteUser(@PathVariable Long id) {
        userServiceInter.delete(id);
    }

    @PostMapping("/addUsers")
    public List<UserEntity> addUsers(@RequestBody List<UserEntity> users) {
        return userServiceInter.addUsers(users);
    }

    @PostMapping("/addUserWTCP")
    public String addUserWTCP(@RequestBody UserEntity userEntity) {
        return userServiceInter.addUserWTCP(userEntity);
    }

    //add with username
    @PostMapping("/addUserWTUN")
    public String addUserWTUN(@RequestBody UserEntity userEntity) {
        return userServiceInter.addUserWTUN(userEntity);
    }

    @PostMapping("/updateUsers/{id}")
    public UserEntity updateUserPost(@RequestBody UserEntity userEntity, @PathVariable Long id) {
        return userServiceInter.updateUserPost(userEntity, id);
    }

    @GetMapping("/getAllUsers")
    public List<UserEntity> getAllUsers() {
        return userServiceInter.getAllUsers();
    }

    @GetMapping("/getUserById/{id}")
    public UserEntity getUserById(@PathVariable Long id) {
        return userServiceInter.getUserById(id);
    }

    //cryptage de mdp
    @PostMapping("/addC")
    public UserEntity addUsercryp(@RequestBody UserEntity user) {
        return userServiceInter.addUsercryp(user);
    }

    @PostMapping("/login")
    public ResponseEntity<UserEntity> login(@RequestParam String username, @RequestParam String password) {
        return userServiceInter.login(username,password);
    }

    @GetMapping("/profile/{email}")
    public ResponseEntity<UserProfileDTO> getProfile(@PathVariable String email) {
        UserProfileDTO profile = userServiceInter.getUserProfile(email);
        return profile != null ? ResponseEntity.ok(profile) : ResponseEntity.notFound().build();
    }

    @PutMapping("/profile/{email}")
    public ResponseEntity<UserProfileDTO> updateProfile(@PathVariable String email,
                                                        @RequestBody UserProfileDTO dto) {
        UserProfileDTO updated = userServiceInter.updateUserProfile(email, dto);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }

    // Admin approuve un utilisateur
    @PutMapping("/approve/{id}")
    public ResponseEntity<UserEntity> approveUser(@PathVariable Long id) {
        UserEntity updated = userServiceInter.updateUserStatus(id, UserStatus.APPROVED);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }

    @PutMapping("/reject/{id}")
    public ResponseEntity<UserEntity> rejectUser(@PathVariable Long id) {
        UserEntity updated = userServiceInter.updateUserStatus(id, UserStatus.REJECTED);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }


}
