package com.example.transactionmanagementdemo.domain.orders;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.sql.Timestamp;

@Getter
@Setter
@Builder
public class OrdersRequest {

    @NotNull(message = "UserId is required")
    private Integer userId;

    @Pattern(regexp="/Processing|Completed|Canceled/", message = "OrderStatus can be Processing, Completed or Canceled")
    private String orderStatus;

    @NotNull(message = "DatePlaced is required")
    private Timestamp datePlaced;

}
