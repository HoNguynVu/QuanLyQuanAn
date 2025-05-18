package com.example.doan.AdminActivity;

public interface ImageUploadCallback {
    void onSuccess(String imageUrl);
    void onFailure(String error);
}