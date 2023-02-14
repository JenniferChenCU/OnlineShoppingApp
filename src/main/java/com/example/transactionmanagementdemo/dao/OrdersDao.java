package com.example.transactionmanagementdemo.dao;

import com.example.transactionmanagementdemo.domain.OrderProduct.OrderProduct;
import com.example.transactionmanagementdemo.domain.Orders.OrderStatus;
import com.example.transactionmanagementdemo.domain.Orders.Orders;
import com.example.transactionmanagementdemo.domain.Orders.OrdersResponse;
import com.example.transactionmanagementdemo.domain.Product.Product;
import com.example.transactionmanagementdemo.domain.User.User;
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
    public OrdersResponse updateOrdersStatus(int orderId, int status, boolean isAdmin) throws OrderNotFoundException {
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

            // Admin can make "Processing" orders "Completed" or "Canceled"
            // User can make "Processing" orders "Canceled"
            OrderStatus newStatus = OrderStatus.values()[status];
            OrderStatus currentStatus = theOrders.getOrderStatus();
            if (isAdmin && OrderStatus.PROCESSING==currentStatus){
                 theOrders.setOrderStatus(newStatus);
                 session.saveOrUpdate(theOrders);
            }else if (!isAdmin && OrderStatus.PROCESSING==currentStatus && OrderStatus.CANCELED==newStatus){
                 theOrders.setOrderStatus(newStatus);
                 session.saveOrUpdate(theOrders);
            }
            // TODO: update stock if newStatus is canceled
//            if (OrderStatus.CANCELED==newStatus){
//                ProductDao productDao;
//                List<OrderProduct> orderProducts = theOrders.getOrderProducts();
//                for (OrderProduct orderProduct: orderProducts){
//                    int productId = orderProduct.getProducts().getId();
//                    int productQuantity = orderProduct.getPurchasedQuantity();
//                    Product product = productDao.getProductById(productId);
//
//                }
//            }

            // update productProfit, productSoldQuantity, userSpends
            if (status == 1) {
                updateProfit(orderId);
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
                float profitPerItem = product.getRetailPrice()-product.getWholesalePrice();
                product.setSoldQuantity(quantity + product.getSoldQuantity());
                product.setProfit(profitPerItem*quantity + product.getProfit());
                user.setTotalSpent(product.getWholesalePrice()*quantity + user.getTotalSpent());
                session.saveOrUpdate(product);
            }
            session.saveOrUpdate(user);
        }
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
