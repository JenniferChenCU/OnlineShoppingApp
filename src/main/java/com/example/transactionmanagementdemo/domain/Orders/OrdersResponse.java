package com.example.transactionmanagementdemo.domain.Orders;

import com.example.transactionmanagementdemo.domain.Orders.Orders;
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
