package com.example.transactionmanagementdemo.domain.User;

import com.example.transactionmanagementdemo.domain.Orders.Orders;
import com.example.transactionmanagementdemo.domain.Product.Product;
import lombok.*;
import org.springframework.boot.jdbc.DataSourceBuilder;

import javax.persistence.*;
import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name="USER")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Integer id;

    @Column(name = "username", nullable = false, length = 250)
    private String username;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "isSeller")
    private boolean isSeller;

    // user --- 1 to M --- orders
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<Orders> orders = new ArrayList<>();

    public void addOrders(Orders orders) {
        this.orders.add(orders);
        orders.setUser(this);
    }

    // User --- M to M --- Product
    @ManyToMany(cascade = CascadeType.ALL)      // owner of relationship
    @JoinTable(name = "user_product",         // ???
            joinColumns = {@JoinColumn(name = "user_id")},           //joinColumn specify current class's fk
            inverseJoinColumns = {@JoinColumn(name = "product_id")})     //inverseJoinColumn that of referenced class's fk
    Set<Product> products = new HashSet<>();

}
