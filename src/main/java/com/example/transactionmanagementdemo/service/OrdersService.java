package com.example.transactionmanagementdemo.service;

import com.example.transactionmanagementdemo.dao.OrdersDao;
import com.example.transactionmanagementdemo.dao.ProductDao;
import com.example.transactionmanagementdemo.domain.Product.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.transactionmanagementdemo.domain.Orders.Orders;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class OrdersService {
    private final OrdersDao ordersDao;

    @Autowired
    public OrdersService(OrdersDao ordersDao) {
        this.ordersDao = ordersDao;
    }

    @Transactional
    public List<Orders> getAllOrdersSuccess(){
        return ordersDao.getAllOrders();
    }

    @Transactional
    public void updateOrdersStatus(int orderId, String status, boolean isAdmin){
        ordersDao.updateOrdersStatus(orderId, status, isAdmin);
    }
}
