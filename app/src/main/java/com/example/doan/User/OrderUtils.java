package com.example.doan.User;

import com.example.doan.DatabaseClass.FoodItem;
import com.example.doan.DatabaseClassRequest.OrderRequest;

import java.util.ArrayList;
import java.util.List;

public class OrderUtils {
    public static List<OrderRequest.Item> convertFoodItemsToOrderItems(List<FoodItem> foodList) {
        List<OrderRequest.Item> items = new ArrayList<>();
        for (FoodItem food : foodList) {
            items.add(new OrderRequest.Item(food.getId(), Integer.parseInt(food.getItemQuantity()), food.getNote()));
        }
        return items;
    }
}