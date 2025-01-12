package com.example.model;

/**
 * Represents a user in the system.
 *
 * This class stores the user's information such as ID, username, name, password, and role.
 * It provides getter and setter methods to access and modify the user's data.
 */
public class User {
    private int id;
    private String username;
    private String name;
    private String password;
    private Role role;

    /**
     * Constructs a User with the specified details.
     *
     * @param id       the unique ID of the user
     * @param username the username of the user
     * @param name     the name of the user
     * @param password the password of the user
     * @param role     the role of the user (e.g., Admin, Cashier)
     */
    public User(int id, String username, String name, String password, Role role) {
        this.id = id;
        this.username = username;
        this.name = name;
        this.password = password;
        this.role = role;
    }

    /**
     * Constructs a User without the ID, typically used for creating new users.
     *
     * @param username the username of the user
     * @param name     the name of the user
     * @param password the password of the user
     * @param role     the role of the user (e.g., Admin, Cashier)
     */
    public User(String username, String name, String password, Role role) {
        this.username = username;
        this.name = name;
        this.password = password;
        this.role = role;
    }

    /**
     * Gets the unique ID of the user.
     *
     * @return the unique ID of the user
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the unique ID of the user.
     *
     * @param id the unique ID to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Gets the username of the user.
     *
     * @return the username of the user
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the username of the user.
     *
     * @param username the username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Gets the name of the user.
     *
     * @return the name of the user
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the user.
     *
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the password of the user.
     *
     * @return the password of the user
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the password of the user.
     *
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Gets the role of the user.
     *
     * @return the role of the user
     */
    public Role getRole() {
        return role;
    }

    /**
     * Sets the role of the user.
     *
     * @param role the role to set
     */
    public void setRole(Role role) {
        this.role = role;
    }
}
