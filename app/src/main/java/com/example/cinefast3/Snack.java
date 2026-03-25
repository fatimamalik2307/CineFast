package com.example.cinefast3;

import java.io.Serializable;

public class Snack implements Serializable {
    private String name;
    private String description;
    private double price;
    private int imageResource;
    private int quantity;
    private int colorRes; // Background color for the image

    public Snack(String name, String description, double price, int imageResource, int colorRes) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.imageResource = imageResource;
        this.quantity = 0;
        this.colorRes = colorRes;
    }

    public String getName() { return name; }
    public String getDescription() { return description; }
    public double getPrice() { return price; }
    public int getImageResource() { return imageResource; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public int getColorRes() { return colorRes; }
}
