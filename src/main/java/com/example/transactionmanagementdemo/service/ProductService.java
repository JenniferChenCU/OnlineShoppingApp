package com.example.transactionmanagementdemo.service;

import com.example.transactionmanagementdemo.dao.ProductDao;
import com.example.transactionmanagementdemo.domain.Product.Product;
import com.example.transactionmanagementdemo.exception.ProductSaveFailedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private final ProductDao productDao;

    @Autowired
    public ProductService(ProductDao productDao) {
        this.productDao = productDao;
    }

    @Transactional
    public List<Product> getAllProductsSuccess(){
        return productDao.getAllProducts();
    }

    @Transactional
    public List<Product> getInstockProductsSuccess(){
        return productDao.getInstockProductsSuccess();
    }

    @Transactional
    public List<Product> getTop3Products(){
        List<Product> allProducts = getAllProductsSuccess();
        return productDao.getTop3Products(allProducts);
    }

    @Transactional
    public Product userGetProductById(int userId, int productId){
        return productDao.userGetProductById(userId, productId);
    }

    @Transactional
    public Product getProductById(int id){
        return productDao.getProductById(id);
    }

    @Transactional
    public Product updateProduct(Product product){
        return productDao.updateProduct(product);
    }

    @Transactional
    public Product getProductByProductname(String productname){
        return productDao.getProductByProductname(productname);
    }

    @Transactional
    public void addProduct(Product product){
        productDao.addProduct(product);
    }

    @Transactional
    public void saveProductSuccess(Product product){
        productDao.addProduct(product);
    }

    @Transactional(rollbackOn = ProductSaveFailedException.class)
    public void saveProductFailed(Product product) throws ProductSaveFailedException {
        //success operation
        productDao.addProduct(product);
        //failed operation
        productDao.somethingWentWrong();
    }

    @Transactional
    public void deleteProductById(int id){
        Product product = productDao.getProductById(id);
        productDao.deleteProduct(product);
    }

}
