package com.example.transactionmanagementdemo.service;

import com.example.transactionmanagementdemo.dao.ProductDao;
import com.example.transactionmanagementdemo.dao.UserDao;
import com.example.transactionmanagementdemo.domain.product.Product;
import com.example.transactionmanagementdemo.domain.user.User;
import com.example.transactionmanagementdemo.domain.watchList.WatchListResponse;
import com.example.transactionmanagementdemo.exception.InvalidCredentialsException;
import com.example.transactionmanagementdemo.exception.UserSaveFailedException;
import com.example.transactionmanagementdemo.security.AuthUserDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    private final UserDao userDao;
    private final ProductDao productDao;


    @Autowired
    public UserService(UserDao userDao, ProductDao productDao) {
        this.userDao = userDao;
        this.productDao = productDao;
    }

    @Transactional
    @Override
    public UserDetails loadUserByUsername(String username) throws InvalidCredentialsException {
        Optional<User> userOptional = userDao.loadUserByUsername(username);

        if (!userOptional.isPresent()){
            throw new InvalidCredentialsException("Invalid credential!");
        }

        User user = userOptional.get(); // database user

        return AuthUserDetail.builder() // spring security's userDetail
                .username(user.getUsername())
                .password(new BCryptPasswordEncoder().encode(user.getPassword()))
                .authorities(getAuthoritiesFromUser(user))
                .accountNonExpired(true)
                .accountNonLocked(true)
                .credentialsNonExpired(true)
                .enabled(true)
                .build();
    }

    private List<GrantedAuthority> getAuthoritiesFromUser(User user){
        List<GrantedAuthority> userAuthorities = new ArrayList<>();

        userAuthorities.add(new SimpleGrantedAuthority(user.isSeller() ? "admin" : "user"));

        return userAuthorities;
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

}
