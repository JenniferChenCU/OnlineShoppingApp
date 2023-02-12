package com.example.transactionmanagementdemo.service;

import com.example.transactionmanagementdemo.dao.UserDao;
import com.example.transactionmanagementdemo.domain.User.User;
import com.example.transactionmanagementdemo.exception.UserSaveFailedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserDao userDao;

    @Autowired
    public UserService(UserDao userDao) {
        this.userDao = userDao;
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
    public void deleteUser(int id){
        User user = userDao.getUserById(id);
        userDao.deleteById(user);
    }

}
