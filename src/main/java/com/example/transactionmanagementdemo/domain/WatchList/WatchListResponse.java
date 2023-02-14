package com.example.transactionmanagementdemo.domain.WatchList;

import com.example.transactionmanagementdemo.domain.Product.Product;
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
