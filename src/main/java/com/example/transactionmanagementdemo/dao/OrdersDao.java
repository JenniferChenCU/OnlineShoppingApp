package com.example.transactionmanagementdemo.dao;

import com.example.transactionmanagementdemo.domain.orderProduct.OrderProduct;
import com.example.transactionmanagementdemo.domain.orders.OrderStatus;
import com.example.transactionmanagementdemo.domain.orders.Orders;
import com.example.transactionmanagementdemo.domain.orders.OrdersResponse;
import com.example.transactionmanagementdemo.domain.product.Product;
import com.example.transactionmanagementdemo.domain.user.User;
import com.example.transactionmanagementdemo.exception.OrderNotFoundException;
import com.example.transactionmanagementdemo.exception.ProductSaveFailedException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.*;
import java.util.*;

@Repository
public class OrdersDao {

    @Autowired
    SessionFactory sessionFactory;

    public List<Orders> getAllOrders(){
        Session session;
        List<Orders> ordersList = null;
        try{
            session = sessionFactory.getCurrentSession();
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Orders> cq = cb.createQuery(Orders.class);
            Root<Orders> root = cq.from(Orders.class);
            cq.select(root);
            ordersList = session.createQuery(cq).getResultList();
        }catch (Exception e){
            e.printStackTrace();
        }
        return (ordersList.isEmpty()) ? null : ordersList;
    }

    public List<Orders> getValidOrders(int userId){
        Session session;
        List<Orders> orders = null;
        try{
            session = sessionFactory.getCurrentSession();
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Orders> cq = cb.createQuery(Orders.class);
            Root<Orders> root = cq.from(Orders.class);
            Predicate userPredicate = cb.equal(root.get("user").get("id"), userId);
            Predicate statusPredicate = cb.notEqual(root.get("orderStatus"), 2);
            cq.select(root).where(cb.and(userPredicate, statusPredicate));
            orders = session.createQuery(cq).getResultList();
            System.out.println(orders);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return orders;
    }

//    public List<Orders> getTop3RecentOrders(int userId){
//        Session session;
//        List<Orders> orders = null;
//        try{
//            session = sessionFactory.getCurrentSession();
//            CriteriaBuilder cb = session.getCriteriaBuilder();
//            CriteriaQuery<Orders> cq = cb.createQuery(Orders.class);
//            Root<Orders> root = cq.from(Orders.class);
//            Predicate userPredicate = cb.equal(root.get("user").get("id"), userId);
//            Predicate statusPredicate = cb.notEqual(root.get("status"), OrderStatus.CANCELED);
//            cq.select(root).where(cb.and(userPredicate, statusPredicate)).orderBy(cb.desc(root.get("datePlaced")));
//            orders = session.createQuery(cq).setMaxResults(3).getResultList();
//        }
//        catch (Exception e){
//            e.printStackTrace();
//        }
//        return orders;
//    }

    public Orders getOrdersById(int id){
        Session session;
        Optional<Orders> orders = null;
        try{
            session = sessionFactory.getCurrentSession();
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Orders> cq = cb.createQuery(Orders.class);
            Root<Orders> root = cq.from(Orders.class);
            Predicate predicate = cb.equal(root.get("id"), id);
            cq.select(root).where(predicate);
            orders = session.createQuery(cq).uniqueResultOptional();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return (orders.isPresent())? orders.get() : null;
    }

    public void createNewOrders(Orders orders){
        Session session = sessionFactory.getCurrentSession();
        session.saveOrUpdate(orders);
    }
    public OrdersResponse updateOrdersStatus(int orderId, int status) throws OrderNotFoundException {
        Session session;
        Optional<Orders> orders = null;
        try{
            session = sessionFactory.getCurrentSession();
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Orders> cq = cb.createQuery(Orders.class);
            Root<Orders> root = cq.from(Orders.class);
            Predicate predicate = cb.equal(root.get("id"), orderId);
            cq.select(root).where(predicate);
            orders = session.createQuery(cq).uniqueResultOptional();
            if (!orders.isPresent()){
                throw new OrderNotFoundException("Order " + orderId + " does not Exist!");
            }
            Orders theOrders = orders.get();

            // can make "Processing" orders "Completed" or "Canceled"
            OrderStatus newStatus = OrderStatus.values()[status];
            OrderStatus currentStatus = theOrders.getOrderStatus();
            if (OrderStatus.PROCESSING==currentStatus){
                 theOrders.setOrderStatus(newStatus);
                 session.saveOrUpdate(theOrders);
            }
            // if complete update productProfit, productSoldQuantity, userSpends
            if (status == 1) {
                updateProfit(orderId);
            }
            // if canceled update stock
            if (status == 2){
                putBackStock(orderId);
            }
        }catch (OrderNotFoundException e){
            e.printStackTrace();
            return OrdersResponse.builder().message("Order "+ orderId +" does not exist!").build();
        }catch (Exception e){
            e.printStackTrace();
            return OrdersResponse.builder().message("Illegal operation!").build();
        }
        return OrdersResponse.builder().orders(orders.get()).message("Order "+ orderId +" status get updated!").build();
    }

    public void updateProfit(int orderId){
        Session session = sessionFactory.getCurrentSession();
        Orders orders = getOrdersById(orderId);
        if (orders.getOrderStatus() == OrderStatus.COMPLETED){
            User user = orders.getUser();
            List<OrderProduct> orderProducts = orders.getOrderProducts();
            for (OrderProduct orderProduct: orderProducts){
                Product product = orderProduct.getProducts();
                int quantity = orderProduct.getPurchasedQuantity();
                product.setSoldQuantity(quantity + product.getSoldQuantity());
                float profitPerItem = orderProduct.getExecutionRetailPrice() - orderProduct.getExecutionWholesalePrice();
                product.setProfit(profitPerItem*quantity + product.getProfit());
                user.setTotalSpent(orderProduct.getExecutionWholesalePrice()*quantity + user.getTotalSpent());
                session.saveOrUpdate(product);
            }
            session.saveOrUpdate(user);
        }
    }

    public void putBackStock(int orderId){
        Session session = sessionFactory.getCurrentSession();
        Orders orders = getOrdersById(orderId);
        User user = orders.getUser();
        List<OrderProduct> orderProducts = orders.getOrderProducts();
        for (OrderProduct orderProduct: orderProducts){
            Product product = orderProduct.getProducts();
            int quantity = orderProduct.getPurchasedQuantity();
            // sold quantity and stock
            product.setSoldQuantity(product.getSoldQuantity() - quantity);
            product.setStockQuantity(product.getStockQuantity() + quantity);
            // product profit
            float profitPerItem = orderProduct.getExecutionRetailPrice() - orderProduct.getExecutionWholesalePrice();
            product.setProfit(product.getProfit() - profitPerItem*quantity);
            // user spend
            user.setTotalSpent(user.getTotalSpent() - orderProduct.getExecutionWholesalePrice()*quantity);
            session.saveOrUpdate(product);
        }
        session.saveOrUpdate(user);
    }

//    public Product userGetOrderById(int userId, int productId){
//        Session session;
//        Optional<Product> product = null;
//        try{
//            session = sessionFactory.getCurrentSession();
//            CriteriaBuilder cb = session.getCriteriaBuilder();
//            CriteriaQuery<Product> cq = cb.createQuery(Product.class);
////                    .setProjection(Projections.projectionList()
////                            .add(Projections.property("id"), "id")
////                            .add(Projections.property("name"), "name")
////                            .add(Projections.property("description"), "description")
////                            .add(Projections.property("retailPrice"), "price"));
//            Root<Product> root = cq.from(Product.class);
//            Predicate predicate = cb.equal(root.get("id"), productId);
//            cq.select(root).where(predicate);
//            product = session.createQuery(cq).uniqueResultOptional();
//        }
//        catch (Exception e){
//            e.printStackTrace();
//        }
//        return (product.isPresent())? product.get() : null;
//    }

    public void somethingWentWrong () throws ProductSaveFailedException {
        throw new ProductSaveFailedException("Something went wrong, rolling back");
    }
}
