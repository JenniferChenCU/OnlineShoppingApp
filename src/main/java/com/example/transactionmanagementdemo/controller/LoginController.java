package com.example.transactionmanagementdemo.controller;

import com.example.transactionmanagementdemo.domain.Login.LoginRequest;
import com.example.transactionmanagementdemo.domain.Login.LoginResponse;
import com.example.transactionmanagementdemo.security.AuthUserDetail;
import com.example.transactionmanagementdemo.security.JwtProvider;
import com.example.transactionmanagementdemo.service.LoginService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("")
public class LoginController {
    private final LoginService loginService;
    private AuthenticationManager authenticationManager;
    private JwtProvider jwtProvider;

    @Autowired
    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }
    @Autowired
    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }
    @Autowired
    public void setJwtProvider(JwtProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
    }

//    @PostMapping("")
//    public UserResponse loginValidation(@RequestBody UserRequest user) throws InvalidCredentialsException {
//
//        Optional<User> possibleUser;
//        try {
//            possibleUser= loginService.validateLogin(user.getEmail(), user.getPassword());
//            if (!possibleUser.isPresent()){
//                throw new InvalidCredentialsException("Incorrect credentials, please try again.");
//            }
//        }catch(InvalidCredentialsException e){
//            e.printStackTrace();
//            return UserResponse.builder()
//                    .message("Login fail!")
//                    .build();
//        }
//        return UserResponse.builder()
//                .message("Login successfully!")
//                .user(possibleUser.get())
//                .build();
//
//    }

    //User trying to log in with username and password
    @PostMapping("auth/login")
    public LoginResponse login(@RequestBody LoginRequest request){

        Authentication authentication;

        //Try to authenticate the user using the username and password
        try{
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );
        } catch (AuthenticationException e){
            throw new BadCredentialsException("Provided credential is invalid.");
        }

        //Successfully authenticated user will be stored in the authUserDetail object
        AuthUserDetail authUserDetail = (AuthUserDetail) authentication.getPrincipal(); //getPrincipal() returns the user object

        //A token wil be created using the username/email/userId and permission
        String token = jwtProvider.createToken(authUserDetail);

        //Returns the token as a response to the frontend/postman
        return LoginResponse.builder()
                .message("Welcome " + authUserDetail.getUsername())
                .token(token)
                .build();

    }
}
