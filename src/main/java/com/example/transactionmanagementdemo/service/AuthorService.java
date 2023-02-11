package com.example.transactionmanagementdemo.service;

import com.example.transactionmanagementdemo.dao.AuthorDao;
import com.example.transactionmanagementdemo.domain.entity.Author;
import com.example.transactionmanagementdemo.exception.AuthorSaveFailedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class AuthorService {

    private final AuthorDao authorDao;

    @Autowired
    public AuthorService(AuthorDao authorDao) {
        this.authorDao = authorDao;
    }

    @Transactional
    public List<Author> getAllAuthorsSuccess(){
        return authorDao.getAllAuthors();
    }

    @Transactional
    public Author getAuthorById(int id){
        return authorDao.getAuthorById(id);
    }

    @Transactional
    public void saveAuthorSuccess(Author author){
        authorDao.addAuthor(author);
    }

    @Transactional(rollbackOn = AuthorSaveFailedException.class)
    public void saveAuthorFailed(Author author) throws AuthorSaveFailedException {
        //success operation
        authorDao.addAuthor(author);
        //failed operation
        authorDao.somethingWentWrong();
    }

    @Transactional
    public void deleteAuthor(int id){
        Author author = authorDao.getAuthorById(id);
        authorDao.deleteById(author);
    }

}
