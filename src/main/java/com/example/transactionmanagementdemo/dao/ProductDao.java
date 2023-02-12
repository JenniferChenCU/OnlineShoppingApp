package com.example.transactionmanagementdemo.dao;

import com.example.transactionmanagementdemo.domain.Product.Product;
import com.example.transactionmanagementdemo.exception.ProductSaveFailedException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
public class ProductDao {

    @Autowired
    SessionFactory sessionFactory;

    public List<Product> getAllProducts(){
        Session session;
        List<Product> productList = null;
        try{
            session = sessionFactory.getCurrentSession();
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Product> cq = cb.createQuery(Product.class);
            Root<Product> root = cq.from(Product.class);
            cq.select(root);
            productList = session.createQuery(cq).getResultList();
        }catch (Exception e){
            e.printStackTrace();
        }
        return (productList.isEmpty()) ? null : productList;
    }

    public List<Product> getInstockProductsSuccess(){
        Session session;
        List<Product> instockProductList = null;
        try{
            session = sessionFactory.getCurrentSession();
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Product> cq = cb.createQuery(Product.class);
            Root<Product> root = cq.from(Product.class);
            Predicate predicate = cb.gt(root.get("stockQuantity"), 0);
            cq.select(root).where(predicate);
            instockProductList = session.createQuery(cq).getResultList();
        }catch (Exception e){
            e.printStackTrace();
        }
        System.out.println("size: "+instockProductList.size());
        return (instockProductList.isEmpty()) ? null : instockProductList;
    }

    public Product getProductById(int id){
        Session session;
        Optional<Product> product = null;
        try{
            session = sessionFactory.getCurrentSession();
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Product> cq = cb.createQuery(Product.class);
            Root<Product> root = cq.from(Product.class);
            Predicate predicate = cb.equal(root.get("id"), id);
            cq.select(root).where(predicate);
            product = session.createQuery(cq).uniqueResultOptional();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return (product.isPresent())? product.get() : null;
    }

    public Product getProductByProductname(String productname){
        Session session;
        Optional<Product> product = null;
        try{
            session = sessionFactory.getCurrentSession();
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Product> cq = cb.createQuery(Product.class);
            Root<Product> root = cq.from(Product.class);
            Predicate predicate = cb.equal(root.get("name"), productname);
            cq.select(root).where(predicate);
            product = session.createQuery(cq).uniqueResultOptional();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return (product.isPresent())? product.get() : null;
    }

    public void addProduct(Product product){
        Session session;
        try{
            session = sessionFactory.getCurrentSession();
            session.saveOrUpdate(product);
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    public void somethingWentWrong () throws ProductSaveFailedException {
        throw new ProductSaveFailedException("Something went wrong, rolling back");
    }

    public void deleteProduct(Product product){
        Session session;
        try{
            session = sessionFactory.getCurrentSession();
            session.delete(product);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
