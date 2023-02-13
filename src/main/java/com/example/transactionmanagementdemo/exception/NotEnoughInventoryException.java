package com.example.transactionmanagementdemo.exception;

public class NotEnoughInventoryException extends RuntimeException{

    public NotEnoughInventoryException(String message){
        super(String.format(message));
    }
}
