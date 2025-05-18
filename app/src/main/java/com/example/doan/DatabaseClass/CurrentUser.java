package com.example.doan.DatabaseClass;

public class CurrentUser {
    private static CurrentUser instance;

    private int id;
    private String email;
    private String name;
    private String phone;
    private String dateBirth;
    private String role;

    private CurrentUser() {}

    public static synchronized CurrentUser getInstance() {
        if (instance == null) {
            instance = new CurrentUser();
        }
        return instance;
    }

    public void setUser(int id, String email, String name, String phone, String dateBirth, String role) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.phone = phone;
        this.dateBirth = dateBirth;
        this.role = role;
    }

    public int getId() { return id; }
    public String getEmail() { return email; }
    public String getName() { return name; }
    public String getPhone() { return phone; }
    public String getDateBirth() { return dateBirth; }
    public String getRole() { return role; }

    public void clear() {
        instance = null;
    }
}
