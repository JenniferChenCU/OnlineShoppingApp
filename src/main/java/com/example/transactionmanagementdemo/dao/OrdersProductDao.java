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

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;

@Repository
public class OrdersProductDao {

    @Autowired
    SessionFactory sessionFactory;

    public List<OrderProduct> getAllOrderProduct(){
        Session session;
        List<OrderProduct> orderProductsList = null;
        try{
            session = sessionFactory.getCurrentSession();
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<OrderProduct> cq = cb.createQuery(OrderProduct.class);
            Root<OrderProduct> root = cq.from(OrderProduct.class);
            cq.select(root);
            orderProductsList = session.createQuery(cq).getResultList();
        }catch (Exception e){
            e.printStackTrace();
        }
        return (orderProductsList.isEmpty()) ? null : orderProductsList;
    }

    public List<OrderProduct> getOrderProductsByOrder(Orders orders){
        Session session;
        List<OrderProduct> orderProduct = null;
        try{
            session = sessionFactory.getCurrentSession();
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<OrderProduct> cq = cb.createQuery(OrderProduct.class);
            Root<OrderProduct> root = cq.from(OrderProduct.class);
            Predicate predicate = cb.equal(root.get("orders"), orders);
            cq.select(root).where(predicate);
            orderProduct = session.createQuery(cq).getResultList();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return orderProduct;
    }
}
