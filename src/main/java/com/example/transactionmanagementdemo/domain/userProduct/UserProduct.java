package com.example.transactionmanagementdemo.domain.userProduct;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserProduct {
    int id;
    String name;
    String description;
    float price;

}
