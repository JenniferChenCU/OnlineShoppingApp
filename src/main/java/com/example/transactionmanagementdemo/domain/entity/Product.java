package com.example.transactionmanagementdemo.domain.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name="PRODUCT")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "retailPrice")
    private float retailPrice;

    @Column(name = "wholesalePrice")
    private float wholesalePrice;

    @Column(name = "stockQuantity")
    private int stockQuantity;
}
