package com.example.doan.DatabaseClassRequest;

import java.util.List;

public class CreateOrderRequest {
    public int user_id;
    public List<OrderItemRequest> items;
    public String discount_code;
    public String payment_method;
}