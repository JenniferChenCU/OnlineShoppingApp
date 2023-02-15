package com.example.transactionmanagementdemo.service;

import com.example.transactionmanagementdemo.dao.OrdersDao;
import com.example.transactionmanagementdemo.dao.ProductDao;
import com.example.transactionmanagementdemo.dao.UserDao;
import com.example.transactionmanagementdemo.domain.orderProduct.OrderProduct;
import com.example.transactionmanagementdemo.domain.orders.OrderStatus;
import com.example.transactionmanagementdemo.domain.orders.OrdersResponse;
import com.example.transactionmanagementdemo.domain.product.Product;
import com.example.transactionmanagementdemo.domain.user.User;
import com.example.transactionmanagementdemo.exception.NotEnoughInventoryException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.transactionmanagementdemo.domain.orders.Orders;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.util.*;

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

    @Transactional
    public List<Map.Entry<Product, Integer>> top3Frequent(int userId){
        List<Orders> orders = ordersDao.getValidOrders(userId);
        Map<Product, Integer> map = new HashMap<>();
        for (Orders o: orders){
            for (OrderProduct p: o.getOrderProducts()){
                Product product = p.getProducts();
                if (!map.containsKey(product)){
                    map.put(product, 0);
                }
                map.put(product, map.get(product)+1);
            }
        }
        List<Map.Entry<Product, Integer>> list = new ArrayList<>(map.entrySet());
        list.sort((o1, o2) -> o2.getValue() - o1.getValue());
        return list.subList(0, Math.min(3, list.size()));
    }

    @Transactional
    public List<Product> top3Recent(int userId){
        List<Orders> orders = ordersDao.getValidOrders(userId);
        HashSet<Product> top3 = new HashSet<>();
        for (Orders o: orders){
            for (OrderProduct p: o.getOrderProducts()){
                int id = p.getProducts().getId();
                top3.add(productDao.getProductById(id));
                if (top3.size()>=3){
                    break;
                }
            }
        }

        List<Product> list = new ArrayList<>();
        list.addAll(top3);
        return list;
    }
}
