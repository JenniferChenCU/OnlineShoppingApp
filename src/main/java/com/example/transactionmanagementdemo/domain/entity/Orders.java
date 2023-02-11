package com.example.transactionmanagementdemo.domain.entity;

import lombok.*;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name="ORDERS")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Orders {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Integer id;

    @Column(name = "userId")
    private int userId;

    @Column(name = "orderStatus")
    private String orderStatus;

    @Column(name = "datePlaced")
    private Timestamp datePlaced;
}
