package com.example.transactionmanagementdemo.domain.entity;

import lombok.*;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PurchaseRequest {
    int userId;
    Map<Integer, Integer> purchaseDetail;
}
