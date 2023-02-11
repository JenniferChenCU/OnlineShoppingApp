package com.example.transactionmanagementdemo.dao;


import com.example.transactionmanagementdemo.domain.entity.Author;
import com.example.transactionmanagementdemo.exception.AuthorSaveFailedException;
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
public class AuthorDao {

    @Autowired
    SessionFactory sessionFactory;

    public List<Author> getAllAuthors(){
        Session session;
        List<Author> authorList = null;
        try{
            session = sessionFactory.getCurrentSession();
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Author> cq = cb.createQuery(Author.class);
            Root<Author> root = cq.from(Author.class);
            cq.select(root);
            authorList = session.createQuery(cq).getResultList();
        }catch (Exception e){
            e.printStackTrace();
        }
        return (authorList.isEmpty()) ? null : authorList;
    }

    public Author getAuthorById(int id){
        Session session;
        Optional<Author> author = null;
        try{
            session = sessionFactory.getCurrentSession();
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Author> cq = cb.createQuery(Author.class);
            Root<Author> root = cq.from(Author.class);
            Predicate predicate = cb.equal(root.get("id"), id);
            cq.select(root).where(predicate);
            author = session.createQuery(cq).uniqueResultOptional();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return (author.isPresent())? author.get() : null;
    }


    public void addAuthor(Author author){
        Session session;
        try{
            session = sessionFactory.getCurrentSession();
            session.saveOrUpdate(author);
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    //Another operation, but it went wrong
    public void somethingWentWrong () throws AuthorSaveFailedException {
        throw new AuthorSaveFailedException("Something went wrong, rolling back");
    }

    public void deleteById(Author author){
        Session session;
        try{
            session = sessionFactory.getCurrentSession();
            session.delete(author);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
