package com.example.transactionmanagementdemo.domain.ProductWatchList;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@Builder
public class ProductWatchListRequest {

    @NotNull(message = "OrderId is required")
    private Integer orderId;

    @NotNull(message = "ProductId is required")
    private Integer productId;

}
