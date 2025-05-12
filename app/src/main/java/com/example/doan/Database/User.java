package com.example.doan.Database;

public class User {
    private String email;
    private String name;
    private String phone;
    private String dateBirth;
    private String password;
    private String role;

    // Constructor mặc định (dùng cho Firebase)
    public User() {
        this.role = "User";
    }

    // Constructor đầy đủ
    public User(String email, String name, String phone, String dateBirth, String password) {
        this.email = email;
        this.name = name;
        this.phone = phone;
        this.dateBirth = dateBirth;
        this.password = password;
        this.role = "User"; // Mặc định là User
    }

    // Getter và Setter

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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDateBirth() {
        return dateBirth;
    }

    public void setDateBirth(String dateBirth) {
        this.dateBirth = dateBirth;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}

