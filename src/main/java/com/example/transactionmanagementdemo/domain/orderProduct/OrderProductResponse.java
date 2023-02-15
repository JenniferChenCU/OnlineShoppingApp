package com.example.transactionmanagementdemo.domain.orderProduct;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderProductResponse {
    private String message;
    private OrderProduct orderProduct;
}
