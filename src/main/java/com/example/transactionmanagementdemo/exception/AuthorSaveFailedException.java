package com.example.transactionmanagementdemo.exception;

import com.example.transactionmanagementdemo.domain.entity.Author;

public class AuthorSaveFailedException extends RuntimeException{


    public AuthorSaveFailedException(String message){
        super(String.format(message));

    }
}
