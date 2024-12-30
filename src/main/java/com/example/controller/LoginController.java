package com.example.controller;
import at.favre.lib.crypto.bcrypt.BCrypt;
import com.example.database.DatabaseConnection;
import com.example.model.User;

public class LoginController {

    private final DatabaseConnection db = new DatabaseConnection();

    public User getAuthenticatedUser(String username, String password) {
        User user = db.getUser(username);
        if(user != null && verifyPassword(password, user.getPassword())) {
            return user;
        }
        return null;
    }

    private String hashPassword(String password) {
        return BCrypt.withDefaults().hashToString(12, password.toCharArray());
    }

    private boolean verifyPassword(String password, String hashedPassword) {
        BCrypt.Result result = BCrypt.verifyer().verify(password.toCharArray(), hashedPassword);
        return result.verified;
    }
}
