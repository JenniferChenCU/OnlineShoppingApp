package com.example.transactionmanagementdemo.controller;

import com.example.transactionmanagementdemo.domain.User.User;
import com.example.transactionmanagementdemo.domain.User.UserRequest;
import com.example.transactionmanagementdemo.domain.User.UserResponse;
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
    public UserResponse loginValidation(
            @RequestBody UserRequest user
    ){
        System.out.println("=== debug ===");
        System.out.println(user.getEmail());
        System.out.println(user.getPassword());

        Optional<User> possibleUser= loginService.validateLogin(user.getEmail(), user.getPassword());
        if (possibleUser.isPresent()) {
            return UserResponse.builder()
                    .message("Login successfully!")
                    .user((User) possibleUser.get())
                    .build();
        }else{
            return UserResponse.builder().message("Login fail!").build();
        }
    }
}
