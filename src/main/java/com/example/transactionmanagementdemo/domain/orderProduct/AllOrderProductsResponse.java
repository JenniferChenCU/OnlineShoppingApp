package com.example.transactionmanagementdemo.domain.orderProduct;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AllOrderProductsResponse {
    private String message;
    private List<OrderProduct> orderProduct;
}
