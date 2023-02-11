package com.example.transactionmanagementdemo.exception;

public class AuthorSaveFailedException extends RuntimeException{


    public AuthorSaveFailedException(String message){
        super(String.format(message));

    }
}
