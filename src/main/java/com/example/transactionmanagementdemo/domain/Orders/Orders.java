package com.example.transactionmanagementdemo.domain.Orders;

import com.example.transactionmanagementdemo.domain.OrderProduct.OrderProduct;
import com.example.transactionmanagementdemo.domain.User.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
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

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "orderStatus")
    private OrderStatus orderStatus;

    @Column(name = "datePlaced")
    private Timestamp datePlaced;

    // Orders --- M to 1 --- User
    @ManyToOne(fetch = FetchType.EAGER)  // the Owner of the relationship
    @JoinColumn(name = "user_id") // <- name here is the exact name Hibernate can use when looking for fk in the "choice" table inside database
    private User user;

    // Orders --- 1 to M --- OrderProduct
    @OneToMany(mappedBy = "orders", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JsonIgnore
    @ToString.Exclude
    private List<OrderProduct> orderProducts = new ArrayList<>();

    public void addOrderProducts(OrderProduct orderProduct) {
        this.orderProducts.add(orderProduct);
        orderProduct.setOrders(this);
    }
}
