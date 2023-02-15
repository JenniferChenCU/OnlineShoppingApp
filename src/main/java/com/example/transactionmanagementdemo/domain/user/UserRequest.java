package com.example.transactionmanagementdemo.domain.user;

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

    @NotNull(message = "Email is required")
    @Pattern(regexp="^.+@[^\\.].*\\.[a-z]{2,}$", message="Email address must be valid")
    private String email;

    @NotNull(message = "Password is required")
    private String password;
}
