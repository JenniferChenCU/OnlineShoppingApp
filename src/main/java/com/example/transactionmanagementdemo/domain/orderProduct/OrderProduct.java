package com.example.transactionmanagementdemo.domain.orderProduct;

import com.example.transactionmanagementdemo.domain.orders.Orders;
import com.example.transactionmanagementdemo.domain.product.Product;
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

//    @Column(name = "product_id")
//    private Integer productId;

    @Column(name = "purchasedQuantity")
    private Integer purchasedQuantity;

    @Column(name = "executionRetailPrice")
    private float executionRetailPrice;

    @Column(name = "executionWholesalePrice")
    private float executionWholesalePrice;

    // OrderProduct --- M to 1 --- Order
    @ManyToOne(fetch = FetchType.EAGER)  // the Owner of the relationship
    @JoinColumn(name = "order_id") // <- name here is the exact name Hibernate can use when looking for fk inside database
    private Orders orders;

    // OrderProduct --- M to 1 --- Product
    @ManyToOne(fetch = FetchType.EAGER)  // the Owner of the relationship
    @JoinColumn(name = "product_id") //, insertable=false, updatable=false) // <- name here is the exact name Hibernate can use when looking for fk in the "choice" table inside database
    private Product products;
}
