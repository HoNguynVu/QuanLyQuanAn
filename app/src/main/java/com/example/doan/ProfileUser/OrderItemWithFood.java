package com.example.doan.ProfileUser;

import com.example.doan.DatabaseClass.FoodItem;
import com.example.doan.DatabaseClass.OrderItem;

public class OrderItemWithFood {
    private OrderItem orderItem;
    private FoodItem food;

    public OrderItemWithFood(OrderItem orderItem, FoodItem food) {
        this.orderItem = orderItem;
        this.food = food;
    }

    public OrderItem getOrderItem() {
        return orderItem;
    }

    public FoodItem getFood() {
        return food;
    }
}

