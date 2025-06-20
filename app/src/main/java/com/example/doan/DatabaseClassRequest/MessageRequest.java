package com.example.doan.DatabaseClassRequest;

public class MessageRequest {
    private int sender_id;
    private int receiver_id;
    private String message;

    public MessageRequest(int sender_id, int receiver_id, String message) {
        this.sender_id = sender_id;
        this.receiver_id = receiver_id;
        this.message = message;
    }

    // Getters and setters nếu cần
}

