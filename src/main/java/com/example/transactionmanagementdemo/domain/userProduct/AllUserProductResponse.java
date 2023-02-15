package com.example.transactionmanagementdemo.domain.userProduct;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AllUserProductResponse {
    private String message;
    private List<UserProduct> userProducts;
}
