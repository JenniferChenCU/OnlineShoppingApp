package com.example.transactionmanagementdemo.domain.user;

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
