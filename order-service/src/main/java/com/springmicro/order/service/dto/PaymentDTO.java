package com.springmicro.order.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentDTO {
    private String payMode;
    private double amount;
    private Date paidDate;
    private int userId;
    private String orderId;
    private String paymentStatus;
}
