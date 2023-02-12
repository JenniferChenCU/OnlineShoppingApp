package com.example.transactionmanagementdemo.controller;

import com.example.transactionmanagementdemo.domain.User.User;
import com.example.transactionmanagementdemo.domain.User.UserResponse;
import com.example.transactionmanagementdemo.exception.UserSaveFailedException;
import com.example.transactionmanagementdemo.service.UserService;
import com.example.transactionmanagementdemo.domain.User.UserRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("user")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/new")
    public UserResponse createNewUser(
            @Valid @RequestBody UserRequest user,
            BindingResult bindingResult
    ){
        // perform validation check
        if (bindingResult.hasErrors()) {
            List<FieldError> errors = bindingResult.getFieldErrors();
            errors.forEach(error -> System.out.println(
                    "ValidationError in " + error.getObjectName() + ": " + error.getDefaultMessage()));
            return UserResponse.builder()
                    .message("Validation Error")
                    .build();
        }

        // unique username & email check
        if (userService.getUserByUsername(user.getUsername())!=null) {
            return UserResponse.builder().message("Username already exist!").build();
        }
        if (userService.getUserByEmail(user.getEmail())!=null) {
            return UserResponse.builder().message("Email already exist!").build();
        }

        // validation passed, create new user
        User newUser = User.builder()
                .username(user.getUsername())
                .email(user.getEmail())
                .password(user.getPassword())
                .build();

        userService.addUser(newUser);

        return UserResponse.builder()
                .message("New user created")
                .user(newUser)
                .build();
    }

    @GetMapping("/success")
    public List<User> getAllUsersSuccess(){
        return userService.getAllUsersSuccess();
    }

    @GetMapping("/id/{id}")
    public UserResponse getUserById(@PathVariable int id){
        User user = userService.getUserById(id);
        return UserResponse.builder()
                .message("Returning user with id: " + id)
                .user(user)
                .build();
    }

    @PutMapping("/success")
    public UserResponse saveUserSuccess(@RequestBody User user){
        userService.saveUserSuccess(user);
        return UserResponse.builder()
                .message("User saved, committing...")
                .user(user)
                .build();
    }

    @PutMapping("/failed")
    public ResponseEntity saveUserFailed(@RequestBody User user) throws UserSaveFailedException {
        userService.saveUserFailed(user);
        return  ResponseEntity.ok().build();
    }

    @DeleteMapping("/id/{id}")
    public ResponseEntity<String> deleteUSerById(@PathVariable int id){
        userService.deleteUser(id);
        return new ResponseEntity<>("User deleted.", HttpStatus.OK);
    }

}
