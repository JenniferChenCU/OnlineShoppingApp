package com.example.transactionmanagementdemo.domain.User;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponse {
    private String message;
    private User user;
}
