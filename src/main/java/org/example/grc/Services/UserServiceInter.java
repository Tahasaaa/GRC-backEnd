package org.example.grc.Services;

import org.example.grc.DTO.UserProfileDTO;
import org.example.grc.Entities.UserEntity;
import org.example.grc.Entities.UserStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface UserServiceInter {

    //UserEntity addUser(UserEntity user);

    UserEntity addUser(UserEntity user);

    UserEntity userAddWR(UserEntity user);

    void delete(Long id);

    List<UserEntity> addUsers(List<UserEntity> users);

    String addUserWTCP(UserEntity userEntity);

    String addUserWTUN(UserEntity userEntity);

    UserEntity updateUserPost(UserEntity userEntity, Long id);

    List<UserEntity> getAllUsers();

    UserEntity getUserById(Long id);


    UserEntity addUsercryp(UserEntity user);

    ResponseEntity<UserEntity> login(String username, String password);

    UserProfileDTO getUserProfile(String email);
    UserProfileDTO updateUserProfile(String email, UserProfileDTO updatedProfile);


    UserEntity updateUserStatus(Long id, UserStatus userStatus);
}
