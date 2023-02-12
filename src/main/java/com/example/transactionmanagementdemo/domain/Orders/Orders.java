package com.example.transactionmanagementdemo.domain.Orders;

import com.example.transactionmanagementdemo.domain.OrderProduct.OrderProduct;
import com.example.transactionmanagementdemo.domain.User.User;
import lombok.*;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="ORDERS")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class Orders {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Integer id;

    @Column(name = "userId", nullable = false)
    private String userId;

    @Column(name = "orderStatus")
    private String orderStatus;

    @Column(name = "datePlaced")
    private Timestamp datePlaced;

    // Orders --- M to 1 --- User
//    @ManyToOne(fetch = FetchType.LAZY)  // the Owner of the relationship
//    @JoinColumn(name = "id") // <- name here is the exact name Hibernate can use when looking for fk in the "choice" table inside database
//    private User user;

//    // Orders --- 1 to M --- OrderProduct
//    @OneToMany(mappedBy = "orders", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
//    @ToString.Exclude
//    private List<OrderProduct> orderProducts = new ArrayList<>();
//
//    public void addOrderProducts(OrderProduct orderProduct) {
//        this.orderProducts.add(orderProduct);
//        orderProduct.setOrders(this);
//    }
}
