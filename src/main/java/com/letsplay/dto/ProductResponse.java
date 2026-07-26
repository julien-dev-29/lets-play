package com.letsplay.dto;

public class ProductResponse {

    private String id;
    private String name;
    private String description;
    private double price;
    private String owner;

    public ProductResponse() {}

    public ProductResponse(String id, String name, String description, double price, String owner) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.owner = owner;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public String getOwner() { return owner; }
    public void setOwner(String owner) { this.owner = owner; }
}