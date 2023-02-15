package com.example.transactionmanagementdemo.AOP;

import com.example.transactionmanagementdemo.domain.orders.OrdersResponse;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


@Aspect
@Component
public class LoggingAspect {

    private Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

//    @Before("com.beaconfire.springaop.AOPDemo.AOP.PointCuts.inControllerLayer()")
//    public void logStartTime(){
//        logger.info("From LoggingAspect.logStartTime in controller: " + System.currentTimeMillis()); // advice
//    }
//
//    @After("com.beaconfire.springaop.AOPDemo.AOP.PointCuts.inService()")
//    public void logEndTime(JoinPoint joinPoint){
//        logger.info("From LoggingAspect.logEndTime in service: " + System.currentTimeMillis()  + ": " + joinPoint.getSignature());
//    }
//
//    @AfterReturning(value = "com.beaconfire.springaop.AOPDemo.AOP.PointCuts.inDAOLayer()", returning = "res")
//    public void logReturnObject(JoinPoint joinPoint, Object res){
//        logger.info("From LoggingAspect.logReturnObject in DAO: " + res + ": " + joinPoint.getSignature());
//    }
//
//    @AfterThrowing(value = "com.beaconfire.springaop.AOPDemo.AOP.PointCuts.inControllerLayer()", throwing = "ex")
//    public void logThrownException(JoinPoint joinPoint, Throwable ex){
//        logger.error("From LoggingAspect.logThrownException in controller: " + ex.getMessage() + ": " + joinPoint.getSignature());
//    }

    @Around("com.example.transactionmanagementdemo.AOP.PointCuts.inDAOLayerNewOrder()")
    public OrdersResponse logNewOrderStartAndEndTime(ProceedingJoinPoint proceedingJoinPoint) throws Throwable{
        // before
        logger.info("From LoggingAspect.logNewOrderStartAndEndTime: " + proceedingJoinPoint.getSignature());
        logger.info("New order start time: " + System.currentTimeMillis());
        // Invoke the actual object
        OrdersResponse ordersResponse = (OrdersResponse) proceedingJoinPoint.proceed();
        // after
        logger.info("New order end time: " + System.currentTimeMillis());
        return ordersResponse;
    }

    @Around("com.example.transactionmanagementdemo.AOP.PointCuts.inDAOLayerUpdateOrder()")
    public OrdersResponse logUpdateOrderStartAndEndTime(ProceedingJoinPoint proceedingJoinPoint) throws Throwable{
        // before
        logger.info("From LoggingAspect.logUpdateOrderStartAndEndTime: " + proceedingJoinPoint.getSignature());
        logger.info("Order status update start time: " + System.currentTimeMillis());
        //Invoke the actual object
        OrdersResponse ordersResponse = (OrdersResponse) proceedingJoinPoint.proceed();
        // after
        logger.info("Order status update end time: " + System.currentTimeMillis());
        return ordersResponse;
    }
}
