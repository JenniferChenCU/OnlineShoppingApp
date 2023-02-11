package com.example.transactionmanagementdemo.domain.Author;

import com.example.transactionmanagementdemo.domain.Author.Author;
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
