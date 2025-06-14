package com.example.doan.DatabaseClass;

import java.util.List;

public class CreateOrderRequest {
    public int user_id;
    public List<OrderItemRequest> items;
    public String discount_code;
    public String payment_method;
}