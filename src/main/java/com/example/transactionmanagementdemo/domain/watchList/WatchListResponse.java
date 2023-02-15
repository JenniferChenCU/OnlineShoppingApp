package com.example.transactionmanagementdemo.domain.watchList;

import com.example.transactionmanagementdemo.domain.product.Product;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WatchListResponse {
    private String message;
    private Set<Product> productSet;
}
