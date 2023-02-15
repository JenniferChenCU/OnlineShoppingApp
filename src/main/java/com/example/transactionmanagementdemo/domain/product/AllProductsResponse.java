package com.example.transactionmanagementdemo.domain.product;

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
