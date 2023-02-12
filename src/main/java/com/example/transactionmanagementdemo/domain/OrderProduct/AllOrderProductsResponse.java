package com.example.transactionmanagementdemo.domain.OrderProduct;

import com.example.transactionmanagementdemo.domain.User.User;
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
