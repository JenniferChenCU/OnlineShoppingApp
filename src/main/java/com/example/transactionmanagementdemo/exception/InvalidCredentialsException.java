package com.example.transactionmanagementdemo.exception;

public class InvalidCredentialsException extends RuntimeException{
//  message = "Incorrect credentials, please try again.";
    public InvalidCredentialsException(String message){
        super(String.format(message));
    }
}
