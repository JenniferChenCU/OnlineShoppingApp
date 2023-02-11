package com.example.transactionmanagementdemo.domain.User;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AllUsersResponse {
    private String message;
    private List<User> user;
}
