package com.example.transactionmanagementdemo.exception;

public class OrderNotFoundException extends RuntimeException{

    public OrderNotFoundException(String message){
        super(String.format(message));
    }
}
