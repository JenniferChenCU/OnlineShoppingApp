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

//    @Size(min = 13, max = 13, message = "ISBN must be exactly 13 characters")
//    @Pattern(regexp="^(0|[1-9][0-9]*)$", message = "ISBN must be a number")
//    private String isbn;
//
//    @NotNull(message = "Title is required")
//    @Size(min = 3, max = 72, message = "Title must be between 3 and 72 characters")
//    private String title;
//
//    private String author;
//
//    @NotNull(message = "Price is required")
//    @Min(value = 1, message = "Price must be at least 1")
//    @Max(value = 999, message = "Price must be at most 999")
//    private double price;
}
