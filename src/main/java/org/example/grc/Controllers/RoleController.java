package org.example.grc.Controllers;

import org.example.grc.Entities.UserEntity;
import org.example.grc.Services.UserServiceInter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserServiceInter userServiceInter;

    @PostMapping("/add")
    public UserEntity addUser(@RequestBody UserEntity user) {
        return userServiceInter.userAdd(user);
    }

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
}
