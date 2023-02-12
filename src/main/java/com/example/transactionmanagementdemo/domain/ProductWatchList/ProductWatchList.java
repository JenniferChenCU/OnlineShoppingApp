package com.example.transactionmanagementdemo.domain.ProductWatchList;

import lombok.*;

import javax.persistence.*;

@Table(name="PRODUCTWATCHLIST")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class ProductWatchList {
    @Column(name = "orderId", nullable = false)
    private Integer orderId;

    @Column(name = "productId")
    private Integer productId;
}
