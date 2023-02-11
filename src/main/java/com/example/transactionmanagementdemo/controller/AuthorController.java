package com.example.transactionmanagementdemo.controller;

import com.example.transactionmanagementdemo.domain.entity.Author;
import com.example.transactionmanagementdemo.domain.response.AuthorResponse;
import com.example.transactionmanagementdemo.exception.AuthorSaveFailedException;
import com.example.transactionmanagementdemo.service.AuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api")
public class AuthorController {

    private final AuthorService authorService;

    @Autowired
    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @GetMapping("/success")
    public List<Author> getAllAuthorsSuccess(){
        return authorService.getAllAuthorsSuccess();
    }

    @GetMapping("/id/{id}")
    public AuthorResponse getAuthorById(@PathVariable int id){
        Author author = authorService.getAuthorById(id);
        return AuthorResponse.builder()
                .message("Returning author with id: " + id)
                .author(author)
                .build();
    }

    @PutMapping("/success")
    public AuthorResponse saveAuthorSuccess(@RequestBody Author author){
        authorService.saveAuthorSuccess(author);
        return AuthorResponse.builder()
                .message("Author saved, committing...")
                .author(author)
                .build();
    }

    @PutMapping("/failed")
    public ResponseEntity saveAuthorFailed(@RequestBody Author author) throws AuthorSaveFailedException {
        authorService.saveAuthorFailed(author);
        return  ResponseEntity.ok().build();
    }

    @DeleteMapping("/id/{id}")
    public ResponseEntity<String> deleteAuthorById(@PathVariable int id){
        authorService.deleteAuthor(id);
        return new ResponseEntity<>("Author deleted.", HttpStatus.OK);
    }

}
