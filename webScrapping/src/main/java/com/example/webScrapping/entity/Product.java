package com.example.webScrapping.entity;

public class Product {

    private String productName;
    private String price;
    private String availability;
    private String manufacturer;

    // Constructor
    public Product(String productName, String price, String availability, String manufacturer) {
        this.productName = productName;
        this.price = price;
        this.availability = availability;
        this.manufacturer = manufacturer;
    }

    // Getters and Setters
    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getAvailability() {
        return availability;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    @Override
    public String toString() {
        return "Product{" +
                "productName='" + productName + '\'' +
                ", price='" + price + '\'' +
                ", availability='" + availability + '\'' +
                ", manufacturer='" + manufacturer + '\'' +
                '}';
    }
}
