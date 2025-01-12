package com.example.model;

import java.math.BigDecimal;

/**
 * Represents a product available for purchase in the cinema.
 *
 * This class stores details about a product, including its ID, name, price, and available quantity.
 */
public class Product {

    private int Id;
    private String name;
    private BigDecimal price;
    private int quantity;

    /**
     * Constructs a Product with the specified details.
     *
     * @param Id        the unique ID of the product
     * @param name      the name of the product
     * @param price     the price of the product
     * @param quantity  the available quantity of the product
     */
    public Product(int Id, String name, BigDecimal price, int quantity) {
        this.Id = Id;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    /**
     * Constructs a Product with the specified details (without ID).
     *
     * @param name      the name of the product
     * @param price     the price of the product
     * @param quantity  the available quantity of the product
     */
    public Product(String name, BigDecimal price, int quantity) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    /**
     * Gets the unique ID of the product.
     *
     * @return the unique ID of the product
     */
    public int getId() {
        return Id;
    }

    /**
     * Sets the unique ID of the product.
     *
     * @param Id the unique ID to set
     */
    public void setId(int Id) {
        this.Id = Id;
    }

    /**
     * Gets the name of the product.
     *
     * @return the name of the product
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the product.
     *
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the price of the product.
     *
     * @return the price of the product
     */
    public BigDecimal getPrice() {
        return price;
    }

    /**
     * Sets the price of the product.
     *
     * @param price the price to set
     */
    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    /**
     * Gets the available quantity of the product.
     *
     * @return the available quantity of the product
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * Sets the available quantity of the product.
     *
     * @param quantity the quantity to set
     */
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
