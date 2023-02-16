package com.example.transactionmanagementdemo.service;

import com.example.transactionmanagementdemo.dao.OrdersDao;
import com.example.transactionmanagementdemo.dao.OrdersProductDao;
import com.example.transactionmanagementdemo.dao.ProductDao;
import com.example.transactionmanagementdemo.dao.UserDao;
import com.example.transactionmanagementdemo.domain.orderProduct.OrderProduct;
import com.example.transactionmanagementdemo.domain.orders.OrderStatus;
import com.example.transactionmanagementdemo.domain.orders.Orders;
import com.example.transactionmanagementdemo.domain.orders.OrdersResponse;
import com.example.transactionmanagementdemo.domain.product.Product;
import com.example.transactionmanagementdemo.domain.user.User;
import com.example.transactionmanagementdemo.exception.NotEnoughInventoryException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.util.*;

@Service
public class OrdersProductService {
    private final OrdersProductDao ordersProductDao;

    @Autowired
    public OrdersProductService(OrdersProductDao ordersProductDao) {
        this.ordersProductDao = ordersProductDao;
    }

    @Transactional
    public List<OrderProduct> getOrderProductsByOrder(Orders orders){
        return ordersProductDao.getOrderProductsByOrder(orders);
    }
}
