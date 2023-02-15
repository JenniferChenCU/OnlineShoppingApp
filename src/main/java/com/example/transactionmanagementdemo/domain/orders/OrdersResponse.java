package com.example.transactionmanagementdemo.domain.orders;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrdersResponse {
    private String message;
    private Orders orders;
}
