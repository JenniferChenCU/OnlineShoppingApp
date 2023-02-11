package com.example.transactionmanagementdemo.domain.User;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.*;

@Getter
@Setter
@Builder
public class UserRequest {

    @NotNull(message = "Username is required")
    private String username;

    @NotNull(message = "email is required")
    @Pattern(regexp="^.+@[^\\.].*\\.[a-z]{2,}$", message="Email address must be valid")
    private String email;

    @NotNull(message = "password is required")
    @Size(min = 5, message = "Password must be at least 5 in length")
    private String password;

//    @Size(min = 13, max = 13, message = "ISBN must be exactly 13 characters")
//    @Pattern(regexp="^(0|[1-9][0-9]*)$", message = "ISBN must be a number")
//    private String isbn;
//
//    @NotNull(message = "Title is required")
//    @Size(min = 3, max = 72, message = "Title must be between 3 and 72 characters")
//    private String title;
//
//    private String author;
//
//    @NotNull(message = "Price is required")
//    @Min(value = 1, message = "Price must be at least 1")
//    @Max(value = 999, message = "Price must be at most 999")
//    private double price;
}
