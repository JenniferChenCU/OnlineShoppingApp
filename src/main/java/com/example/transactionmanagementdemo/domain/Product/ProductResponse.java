package com.example.transactionmanagementdemo.domain.Product;

import com.example.transactionmanagementdemo.domain.Product.Product;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductResponse {
    private String message;
    private Product product;
}
