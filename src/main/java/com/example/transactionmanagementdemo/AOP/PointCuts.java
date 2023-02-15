package com.example.transactionmanagementdemo.AOP;
import com.example.transactionmanagementdemo.domain.orders.Orders;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class PointCuts {

    @Pointcut("within(com.example.transactionmanagementdemo.controller.*)")
    public void inControllerLayer(){}

    @Pointcut("bean(*Service)")
    public void inService(){}

    @Pointcut("execution(* com.example.transactionmanagementdemo.dao.OrdersDao.createNewOrders(..))")
    public void inDAOLayerNewOrder(){}

    @Pointcut("execution(* com.example.transactionmanagementdemo.dao.OrdersDao.updateOrdersStatus(..))")
    public void inDAOLayerUpdateOrder(){}

}
