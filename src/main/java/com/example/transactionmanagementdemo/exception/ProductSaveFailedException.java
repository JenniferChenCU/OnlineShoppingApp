package com.example.transactionmanagementdemo.exception;

public class ProductSaveFailedException extends RuntimeException{

    public ProductSaveFailedException(String message){
        super(String.format(message));
    }
}
