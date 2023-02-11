package com.example.transactionmanagementdemo.domain.User;

import lombok.*;
import org.springframework.boot.jdbc.DataSourceBuilder;

import javax.persistence.*;
import javax.sql.DataSource;

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

}
