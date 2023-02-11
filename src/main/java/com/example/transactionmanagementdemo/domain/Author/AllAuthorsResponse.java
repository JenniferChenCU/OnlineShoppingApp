package com.example.transactionmanagementdemo.domain.Author;

import com.example.transactionmanagementdemo.domain.Author.Author;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AllAuthorsResponse {
    private String message;
    private List<Author> author;
}
