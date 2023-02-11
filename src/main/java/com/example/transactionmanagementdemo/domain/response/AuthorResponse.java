package com.example.transactionmanagementdemo.domain.response;


import com.example.transactionmanagementdemo.domain.entity.Author;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthorResponse {
    private String message;
    private Author author;
}
