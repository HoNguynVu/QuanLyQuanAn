package com.example.doan.DatabaseClass;

public class Food {
    private String idFood;
    private String nameFood;
    private double price;
    private String image;
    private String description;
    private String type;

    public Food() {}

    public Food(String idFood, String nameFood, double price, String image, String description, String type) {
        this.idFood = idFood;
        this.nameFood = nameFood;
        this.price = price;
        this.image = image;
        this.description = description;
        this.type = type;
    }

    public String getIdFood() { return idFood; }
    public void setIdFood(String idFood) { this.idFood = idFood; }

    public String getNameFood() { return nameFood; }
    public void setNameFood(String nameFood) { this.nameFood = nameFood; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public String getImage() { return image; }
    public void setImage(String image) { this.image = image; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
}

