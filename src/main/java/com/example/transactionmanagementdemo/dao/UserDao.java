package com.example.transactionmanagementdemo.dao;

import com.example.transactionmanagementdemo.domain.User.User;
import com.example.transactionmanagementdemo.domain.Product.Product;
import com.example.transactionmanagementdemo.domain.WatchList.WatchListResponse;
import com.example.transactionmanagementdemo.exception.InvalidCredentialsException;
import com.example.transactionmanagementdemo.exception.UserSaveFailedException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import java.util.*;
import java.util.stream.Collectors;

@Repository
public class UserDao {

    @Autowired
    SessionFactory sessionFactory;

    public List<User> getAllUsers(){
        Session session;
        List<User> userList = null;
        try{
            session = sessionFactory.getCurrentSession();
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<User> cq = cb.createQuery(User.class);
            Root<User> root = cq.from(User.class);
            cq.select(root);
            userList = session.createQuery(cq).getResultList();
        }catch (Exception e){
            e.printStackTrace();
        }
        return (userList.isEmpty()) ? null : userList;
    }

    public User getUserById(int id){
        Session session;
        Optional<User> user = null;
        try{
            session = sessionFactory.getCurrentSession();
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<User> cq = cb.createQuery(User.class);
            Root<User> root = cq.from(User.class);
            Predicate predicate = cb.equal(root.get("id"), id);
            cq.select(root).where(predicate);
            user = session.createQuery(cq).uniqueResultOptional();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return (user.isPresent())? user.get() : null;
    }

    public User getUserByUsername(String username){
        Session session;
        Optional<User> user = null;
        try{
            session = sessionFactory.getCurrentSession();
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<User> cq = cb.createQuery(User.class);
            Root<User> root = cq.from(User.class);
            Predicate predicate = cb.equal(root.get("username"), username);
            cq.select(root).where(predicate);
            user = session.createQuery(cq).uniqueResultOptional();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return (user.isPresent())? user.get() : null;
    }

    public User getUserByEmail(String email){
        Session session;
        Optional<User> user = null;
        try{
            session = sessionFactory.getCurrentSession();
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<User> cq = cb.createQuery(User.class);
            Root<User> root = cq.from(User.class);
            Predicate predicate = cb.equal(root.get("email"), email);
            cq.select(root).where(predicate);
            user = session.createQuery(cq).uniqueResultOptional();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return (user.isPresent())? user.get() : null;
    }

    public void addUser(User user){
        Session session;
        try{
            session = sessionFactory.getCurrentSession();
            session.saveOrUpdate(user);
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    public WatchListResponse addProductToWatchList(User user, Product product){
        Session session = sessionFactory.getCurrentSession();
        Set<Product> userWatchList = user.getProducts();
        userWatchList.add(product);
        user.setProducts(userWatchList.stream().filter(p->p.getStockQuantity()>0).collect(Collectors.toSet()));
        session.saveOrUpdate(user);
        return WatchListResponse.builder().message("Product watch list"+userWatchList).build();
    }

    public WatchListResponse deleteProductFromWatchList(User user, int productId){
        Session session = sessionFactory.getCurrentSession();
        Set<Product> userWatchList = user.getProducts();
        Set<Product> newUserWatchList = userWatchList.stream()
                .filter(p->p.getId()!=productId && p.getStockQuantity()>0)
                .collect(Collectors.toSet());
        user.setProducts(newUserWatchList);
        session.saveOrUpdate(user);
        return WatchListResponse.builder().message("Product watch list"+newUserWatchList).build();
    }

    public void somethingWentWrong () throws UserSaveFailedException {
        throw new UserSaveFailedException("Something went wrong, rolling back");
    }

    public void incorrectCredential () throws InvalidCredentialsException {
        throw new InvalidCredentialsException("Incorrect credentials, please try again.");
    }

    public void deleteUser(User user){
        Session session;
        try{
            session = sessionFactory.getCurrentSession();
            session.delete(user);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
