package com.example.transactionmanagementdemo.domain.entity;

import lombok.*;

import javax.persistence.*;

@Table(name="ORDERPRODUCT")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class productWatchList {

    @Column(name = "orderId")
    private int orderId;

    @Column(name = "productId")
    private int productId;
}
