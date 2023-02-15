package com.example.transactionmanagementdemo.service;

import com.example.transactionmanagementdemo.dao.UserDao;
import com.example.transactionmanagementdemo.domain.user.User;
import com.example.transactionmanagementdemo.exception.InvalidCredentialsException;
import com.example.transactionmanagementdemo.security.AuthUserDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;


import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class LoginService implements UserDetailsService {
    private final UserService userService;

    @Autowired
    public LoginService(UserService userService) {this.userService = userService; }

    private UserDao userDao;

    @Autowired
    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

//    @Transactional
//    public Optional<User> validateLogin(String email, String password) {
//        return userService.validateLogin(email, password);
//    }

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
//        List<String> buyer = new ArrayList<>(Arrays.asList("retailPrice"));
//        List<String> seller = new ArrayList<>(Arrays.asList("description", "wholesalePrice", "retailPrice", "stockQuantity"));

        List<String> buyer = new ArrayList<>(Arrays.asList("read"));
        List<String> seller = new ArrayList<>(Arrays.asList("update"));

        if (user.isSeller()){
            for (String permission: seller) {
                userAuthorities.add(new SimpleGrantedAuthority(permission));
            }
        }else{
            for (String permission: buyer){
                userAuthorities.add(new SimpleGrantedAuthority((permission)));
            }
        }

        return userAuthorities;
    }
}
