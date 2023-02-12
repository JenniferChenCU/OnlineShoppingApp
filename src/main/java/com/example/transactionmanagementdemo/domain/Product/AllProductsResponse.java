package com.example.transactionmanagementdemo.domain.Product;

import com.example.transactionmanagementdemo.domain.User.User;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AllProductsResponse {
    private String message;
    private List<Product> product;
}
