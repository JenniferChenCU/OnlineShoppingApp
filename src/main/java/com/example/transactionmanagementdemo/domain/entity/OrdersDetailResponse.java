package com.example.transactionmanagementdemo.domain.entity;

import com.example.transactionmanagementdemo.domain.orderProduct.OrderProduct;
import com.example.transactionmanagementdemo.domain.orders.Orders;
import com.example.transactionmanagementdemo.domain.product.Product;
import com.example.transactionmanagementdemo.domain.userProduct.UserProduct;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrdersDetailResponse {
    private String message;
    private Orders orders;
    private List<Product> products;
    private List<UserProduct> userProducts;
    private List<OrderProduct> orderProducts;
}
