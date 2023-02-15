package com.example.transactionmanagementdemo.domain.orders;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AllOrdersResponse {
    private String message;
    private List<Orders> orders;
}
