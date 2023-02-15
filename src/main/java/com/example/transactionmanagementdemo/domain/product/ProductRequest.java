package com.example.transactionmanagementdemo.domain.product;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@Builder
public class ProductRequest {

    @NotNull(message = "Id is required")
    private Integer id;

    private String name;

    private String description;

    private float retailPrice;

    private float wholesalePrice;

    private Integer stockQuantity;

}
