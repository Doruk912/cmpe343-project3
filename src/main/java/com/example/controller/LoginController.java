package com.example.controller;
import com.example.database.DatabaseConnection;
import com.example.model.User;

public class LoginController {

    private final DatabaseConnection db = new DatabaseConnection();

    public User getUser(String username, String password) {
        return db.getUser(username, password);
    }
}
