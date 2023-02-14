package com.example.transactionmanagementdemo.service;

import com.example.transactionmanagementdemo.dao.OrdersDao;
import com.example.transactionmanagementdemo.dao.ProductDao;
import com.example.transactionmanagementdemo.dao.UserDao;
import com.example.transactionmanagementdemo.domain.OrderProduct.OrderProduct;
import com.example.transactionmanagementdemo.domain.Orders.OrderStatus;
import com.example.transactionmanagementdemo.domain.Orders.OrdersResponse;
import com.example.transactionmanagementdemo.domain.Product.Product;
import com.example.transactionmanagementdemo.domain.User.User;
import com.example.transactionmanagementdemo.exception.NotEnoughInventoryException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.transactionmanagementdemo.domain.Orders.Orders;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class OrdersService {
    private final UserDao userDao;
    private final OrdersDao ordersDao;
    private final ProductDao productDao;

    @Autowired
    public OrdersService(UserDao userDao, OrdersDao ordersDao, ProductDao productDao) {
        this.userDao = userDao;
        this.ordersDao = ordersDao;
        this.productDao = productDao;
    }

    @Transactional
    public List<Orders> getAllOrdersSuccess(){
        return ordersDao.getAllOrders();
    }

    @Transactional
    public OrdersResponse updateOrdersStatus(int orderId, int status, boolean isAdmin){
        return ordersDao.updateOrdersStatus(orderId, status, isAdmin);
    }

    @Transactional(rollbackOn = {NotEnoughInventoryException.class, IllegalArgumentException.class})
    public OrdersResponse createNewOrders(int userId, Map<Integer, Integer> purchaseDetail) throws NotEnoughInventoryException{
        Orders orders = new Orders();
        User user = userDao.getUserById(userId);
        if (user==null){
            throw new IllegalArgumentException("Invalid user Id");
        }
        orders.setUser(user);
        orders.setOrderStatus(OrderStatus.PROCESSING);
        // Update OrderProduct
        List<OrderProduct> orderProducts = new ArrayList<>();
        for (Map.Entry<Integer, Integer> entry: purchaseDetail.entrySet()){
            try {
                int productId = entry.getKey();
                int productQuantity = entry.getValue();
                Product product = productDao.getProductById(productId);
                if (product == null) {
                    throw new IllegalArgumentException("Product does not exist!");
                }
                if (product.getStockQuantity() < productQuantity) {
                    throw new NotEnoughInventoryException("Inventory shortage!");
                }
                // update stock, setup orderProducts
                product.setStockQuantity(product.getStockQuantity() - productQuantity);
                OrderProduct orderProduct = new OrderProduct();
                orderProduct.setProducts(product);
                orderProduct.setOrders(orders);
                orderProduct.setExecutionRetailPrice(product.getRetailPrice());
                orderProduct.setExecutionWholesalePrice(product.getWholesalePrice());
                orderProduct.setPurchasedQuantity(productQuantity);
                orderProducts.add(orderProduct);
            }catch (IllegalArgumentException e) {
                e.printStackTrace();
                return OrdersResponse.builder().message("Product does not exist!").build();
            }catch (NotEnoughInventoryException e) {
                e.printStackTrace();
                return OrdersResponse.builder().message("Inventory shortage!!").build();
            }
        }
        // new order
        orders.setOrderProducts(orderProducts);
        Date date = new Date();
        Timestamp timestamp = new Timestamp(date.getTime());
        orders.setDatePlaced(timestamp);
        ordersDao.createNewOrders(orders);

        return OrdersResponse.builder().orders(orders).message("Order created!").build();
    }
}
