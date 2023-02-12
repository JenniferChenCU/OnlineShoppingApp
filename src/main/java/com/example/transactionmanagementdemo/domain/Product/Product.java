package com.example.transactionmanagementdemo.domain.Product;

import com.example.transactionmanagementdemo.domain.OrderProduct.OrderProduct;
import com.example.transactionmanagementdemo.domain.Orders.Orders;
import com.example.transactionmanagementdemo.domain.User.User;
import lombok.*;
import org.hibernate.criterion.Order;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name="PRODUCT")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Integer id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "retailPrice")
    private float retailPrice;

    @Column(name = "wholesalePrice")
    private float wholesalePrice;

    @Column(name = "stockQuantity")
    private Integer stockQuantity;

    // product --- 1 to M --- OrderProduct
    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<OrderProduct> orderProducts = new ArrayList<>();

    public void addOrderProducts(OrderProduct orderProduct) {
        this.orderProducts.add(orderProduct);
        orderProduct.setProduct(this);
    }

    // User --- M : M --- Product
    @ManyToMany(mappedBy = "products", cascade = CascadeType.ALL)
    @ToString.Exclude
    private Set<User> users = new HashSet<>();

}