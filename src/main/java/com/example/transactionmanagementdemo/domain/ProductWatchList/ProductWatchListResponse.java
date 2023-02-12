package com.example.transactionmanagementdemo.domain.ProductWatchList;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductWatchListResponse {
    private String message;
    private ProductWatchList productWatchList;
}
