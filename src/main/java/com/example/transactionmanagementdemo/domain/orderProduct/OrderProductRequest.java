package com.example.transactionmanagementdemo.domain.orderProduct;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@Builder
public class OrderProductRequest {

    @NotNull(message = "OrderId is required")
    private Integer orderId;

    @NotNull(message = "ProductId is required")
    private Integer productId;

    @NotNull(message = "PurchasedQuantity is required")
    @Size(min = 1, message = "PurchasedQuantity must be at least 1")
    private Integer purchasedQuantity;

    @NotNull(message = "ExecutionRetailPrice is required")
    private float executionRetailPrice;

    @NotNull(message = "ExecutionWholesalePrice is required")
    private float executionWholesalePrice;

}
