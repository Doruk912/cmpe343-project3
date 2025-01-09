package com.example.model;

import java.math.BigDecimal;

public class Product {

    private int Id;
    private String name;
    private BigDecimal price;
    private int quantity;

    public Product(int Id, String name, BigDecimal price, int quantity) {
        this.Id = Id;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    public Product(String name, BigDecimal price, int quantity) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    public int getId() {
        return Id;
    }

    public void setId(int Id) {
        this.Id = Id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
