package com.example.transactionmanagementdemo.dao;

import com.example.transactionmanagementdemo.domain.Orders.OrderStatus;
import com.example.transactionmanagementdemo.domain.Orders.Orders;
import com.example.transactionmanagementdemo.domain.Orders.OrdersResponse;
import com.example.transactionmanagementdemo.exception.OrderNotFoundException;
import com.example.transactionmanagementdemo.exception.ProductSaveFailedException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;

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
            OrderStatus currentStatus = orders.get().getOrderStatus();
            if (isAdmin && OrderStatus.PROCESSING==currentStatus){
                 theOrders.setOrderStatus(newStatus);
                 session.saveOrUpdate(theOrders);
                 // TODO: update stock
            }else if (!isAdmin && OrderStatus.PROCESSING==currentStatus && OrderStatus.CANCELED==newStatus){
                 theOrders.setOrderStatus(newStatus);
                 session.saveOrUpdate(theOrders);
                 // TODO: update stock
            }
        }catch (OrderNotFoundException e){
            e.printStackTrace();
            return OrdersResponse.builder().message("Order "+ orderId +" does not exist!").build();
        }catch (Exception e){
            e.printStackTrace();
            return OrdersResponse.builder().message("Illegal operation!").build();
        }
        return OrdersResponse.builder().message("Order "+ orderId +" status get updated!").build();
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
