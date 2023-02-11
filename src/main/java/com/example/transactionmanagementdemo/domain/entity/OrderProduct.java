package com.example.transactionmanagementdemo.domain.entity;

import lombok.*;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name="ORDERPRODUCT")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class OrderProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Integer id;

    @Column(name = "orderId")
    private int orderId;

    @Column(name = "productId")
    private int productId;

    @Column(name = "purchasedQuantity")
    private int purchasedQuantity;

    @Column(name = "executionRetailPrice")
    private float executionRetailPrice;

    @Column(name = "executionWholesalePrice")
    private float executionWholesalePrice;
}
