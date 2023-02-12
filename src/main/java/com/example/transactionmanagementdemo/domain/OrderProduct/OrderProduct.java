package com.example.transactionmanagementdemo.domain.OrderProduct;

import com.example.transactionmanagementdemo.domain.Orders.Orders;
import com.example.transactionmanagementdemo.domain.Product.Product;
import com.example.transactionmanagementdemo.domain.User.User;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name="ORDERPRODUCT")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class OrderProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Integer id;

    @Column(name = "orderId", nullable = false)
    private Integer orderId;

    @Column(name = "productId")
    private Integer productId;

    @Column(name = "purchasedQuantity")
    private Integer purchasedQuantity;

    @Column(name = "executionRetailPrice")
    private float executionRetailPrice;

    @Column(name = "executionWholesalePrice")
    private float executionWholesalePrice;

//    // OrderProduct --- M to 1 --- Order
//    @ManyToOne(fetch = FetchType.LAZY)  // the Owner of the relationship
//    @JoinColumn(name = "id") // <- name here is the exact name Hibernate can use when looking for fk inside database
//    private Orders orders;
//
//    // OrderProduct --- M to 1 --- Product
//    @ManyToOne(fetch = FetchType.LAZY)  // the Owner of the relationship
//    @JoinColumn(name = "id") // <- name here is the exact name Hibernate can use when looking for fk in the "choice" table inside database
//    private Product product;
}
