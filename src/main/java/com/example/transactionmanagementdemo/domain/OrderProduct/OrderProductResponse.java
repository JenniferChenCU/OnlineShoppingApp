package com.example.transactionmanagementdemo.domain.OrderProduct;

import com.example.transactionmanagementdemo.domain.OrderProduct.OrderProduct;
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
