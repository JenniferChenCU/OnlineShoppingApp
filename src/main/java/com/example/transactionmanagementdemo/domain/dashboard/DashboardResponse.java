package com.example.transactionmanagementdemo.domain.dashboard;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DashboardResponse {
    private String message;
    private Dashboard dashboard;
}
