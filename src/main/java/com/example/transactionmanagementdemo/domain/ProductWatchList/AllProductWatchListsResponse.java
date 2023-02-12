package com.example.transactionmanagementdemo.domain.ProductWatchList;

import com.example.transactionmanagementdemo.domain.OrderProduct.OrderProduct;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AllProductWatchListsResponse {
    private String message;
    private List<ProductWatchList> productWatchList;
}
