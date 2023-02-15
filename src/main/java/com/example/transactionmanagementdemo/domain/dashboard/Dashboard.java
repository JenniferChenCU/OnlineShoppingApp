package com.example.transactionmanagementdemo.domain.dashboard;

import com.example.transactionmanagementdemo.domain.orders.Orders;
import com.example.transactionmanagementdemo.domain.product.Product;

import lombok.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Dashboard {
    private List<Orders> orders;
    private List<Product> products;
}
