package com.example.doan.DatabaseClass;

public class Message {
    private int id;
    private int sender_id;
    private int receiver_id;
    private String message;
    private String timestamp;

    public Message(int id, int sender_id, int receiver_id, String message, String timestamp) {
        this.id = id;
        this.sender_id = sender_id;
        this.receiver_id = receiver_id;
        this.message = message;
        this.timestamp = timestamp;
    }

    public int getSender_id() {
        return sender_id;
    }

    public int getReceiver_id() {
        return receiver_id;
    }

    public String getMessage() {
        return message;
    }

    public String getTimestamp() {
        return timestamp;
    }
}
