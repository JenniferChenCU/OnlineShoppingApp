package com.example.transactionmanagementdemo.controller;

import com.example.transactionmanagementdemo.domain.User.User;
import com.example.transactionmanagementdemo.domain.User.UserRequest;
import com.example.transactionmanagementdemo.domain.User.UserResponse;
import com.example.transactionmanagementdemo.exception.InvalidCredentialsException;
import com.example.transactionmanagementdemo.exception.UserSaveFailedException;
import com.example.transactionmanagementdemo.service.LoginService;
import com.example.transactionmanagementdemo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("login")
public class LoginController {
    private final LoginService loginService;
    private final UserService userService;

    @Autowired
    public LoginController(LoginService loginService, UserService userService) {
        this.loginService = loginService;
        this.userService = userService;
    }

    @PostMapping("")
    public void loginValidation(@RequestBody UserRequest user) throws InvalidCredentialsException {
        try {
            Optional<User> possibleUser= loginService.validateLogin(user.getEmail(), user.getPassword());
            if (!possibleUser.isPresent()){
                throw new InvalidCredentialsException("Incorrect credentials, please try again.");
            }
//            UserResponse.builder()
//                    .message("Login successfully!")
//                    .user(possibleUser.get())
//                    .build();
        } catch (InvalidCredentialsException e){
            System.out.println(e);
        }
    }
}
