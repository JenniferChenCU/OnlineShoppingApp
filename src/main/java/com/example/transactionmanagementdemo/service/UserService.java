package com.example.transactionmanagementdemo.service;

import com.example.transactionmanagementdemo.dao.ProductDao;
import com.example.transactionmanagementdemo.dao.UserDao;
import com.example.transactionmanagementdemo.domain.OrderProduct.OrderProduct;
import com.example.transactionmanagementdemo.domain.Orders.OrderStatus;
import com.example.transactionmanagementdemo.domain.Orders.Orders;
import com.example.transactionmanagementdemo.domain.Orders.OrdersResponse;
import com.example.transactionmanagementdemo.domain.Product.Product;
import com.example.transactionmanagementdemo.domain.User.User;
import com.example.transactionmanagementdemo.domain.WatchList.WatchList;
import com.example.transactionmanagementdemo.domain.WatchList.WatchListResponse;
import com.example.transactionmanagementdemo.exception.NotEnoughInventoryException;
import com.example.transactionmanagementdemo.exception.OrderNotFoundException;
import com.example.transactionmanagementdemo.exception.UserSaveFailedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import javax.transaction.Transaction;
import javax.transaction.Transactional;
import javax.websocket.Session;
import java.sql.Timestamp;
import java.util.*;
import java.util.Optional;

@Service
public class UserService {

    private final UserDao userDao;
    private final ProductDao productDao;

    @Autowired
    public UserService(UserDao userDao, ProductDao productDao) {
        this.userDao = userDao;
        this.productDao = productDao;
    }

    @Transactional
    public List<User> getAllUsersSuccess(){
        return userDao.getAllUsers();
    }

    @Transactional
    public User getUserById(int id){
        return userDao.getUserById(id);
    }

    @Transactional
    public User getUserByUsername(String username){
        return userDao.getUserByUsername(username);
    }

    @Transactional
    public User getUserByEmail(String email){
        return userDao.getUserByEmail(email);
    }

    @Transactional
    public void addUser(User user){
        userDao.addUser(user);
    }

    @Transactional
    public WatchListResponse addProductToWatchList(User user, Product product){
        return userDao.addProductToWatchList(user, product);
    }

    @Transactional
    public WatchListResponse deleteProductFromWatchList(User user, int productId){
        return userDao.deleteProductFromWatchList(user, productId);
    }

    @Transactional
    public void saveUserSuccess(User user){
        userDao.addUser(user);
    }

    @Transactional(rollbackOn = UserSaveFailedException.class)
    public void saveUserFailed(User user) throws UserSaveFailedException {
        //success operation
        userDao.addUser(user);
        //failed operation
        userDao.somethingWentWrong();
    }

    @Transactional
    public void deleteUserById(int id){
        User user = userDao.getUserById(id);
        userDao.deleteUser(user);
    }

    @Transactional
    public Optional<User> validateLogin(String email, String password) {
        return userDao.getAllUsers().stream()
                .filter(a -> a.getEmail().equals(email)
                        && a.getPassword().equals(password))
                .findAny();
    }

    @Transactional
    public List<User> getTop3Users(){
        List<User> allUsers = getAllUsersSuccess();
        return userDao.getTop3Users(allUsers);
    }


    // ------ security part ------ //
//    private List<GrantedAuthority> getAuthoritiesFromUser(User user){
//        List<GrantedAuthority> userAuthorities = new ArrayList<>();
//
//        for (String permission :  user.getPermissions()){
//            userAuthorities.add(new SimpleGrantedAuthority(permission));    // SimpleGrantedAuthority can be created from role Strings
//        }
//
//        return userAuthorities;
//    }

}
