package com.example.doan.DatabaseClass;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class User implements Serializable {

    @SerializedName("id")
    private int id;

    @SerializedName("email")
    private String email;

    @SerializedName("name")
    private String name;

    @SerializedName("password")
    private String password;

    @SerializedName("phone")
    private String phone;

    @SerializedName("date_birth")
    private String date_birth;

    @SerializedName("role")
    private String role;

    // Constructor đầy đủ
    public User(int id, String email, String name, String password, String phone, String date_birth, String role) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.password = password;
        this.phone = phone;
        this.date_birth = date_birth;
        this.role = role;
    }

    // Constructor không có id (dùng khi đăng ký chẳng hạn)
    public User(String email, String name, String password, String phone, String date_birth, String role) {
        this.email = email;
        this.name = name;
        this.password = password;
        this.phone = phone;
        this.date_birth = date_birth;
        this.role = role;
    }

    // Getter – Setter
    public int getId() {
        return id;
    }

    // Không có setId vì ID được sinh tự động bởi DB
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDate_birth() {
        return date_birth;
    }

    public void setDate_birth(String date_birth) {
        this.date_birth = date_birth;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
