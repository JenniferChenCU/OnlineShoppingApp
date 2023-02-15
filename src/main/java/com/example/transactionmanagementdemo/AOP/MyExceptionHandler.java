package com.example.transactionmanagementdemo.AOP;

import com.example.transactionmanagementdemo.domain.login.LoginResponse;
import com.example.transactionmanagementdemo.domain.orders.OrdersResponse;
import com.example.transactionmanagementdemo.exception.InvalidCredentialsException;
import com.example.transactionmanagementdemo.exception.NotEnoughInventoryException;
import com.example.transactionmanagementdemo.exception.OrderNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class MyExceptionHandler {

    @ExceptionHandler(value = {InvalidCredentialsException.class})
    public ResponseEntity<LoginResponse> handleInvalidCredentialsException(LoginResponse e){
        return new ResponseEntity(LoginResponse.builder().message(e.getMessage()).build(), HttpStatus.OK);
    }

    @ExceptionHandler(value = {NotEnoughInventoryException.class})
    public ResponseEntity<OrdersResponse> handleNotEnoughInventoryException(OrdersResponse e){
        return new ResponseEntity(OrdersResponse.builder().message(e.getMessage()).build(), HttpStatus.OK);
    }

    @ExceptionHandler(value = {OrderNotFoundException.class})
    public ResponseEntity<OrdersResponse> handleOrderNotFoundException(OrdersResponse e){
        return new ResponseEntity(OrdersResponse.builder().message(e.getMessage()).build(), HttpStatus.OK);
    }

}
