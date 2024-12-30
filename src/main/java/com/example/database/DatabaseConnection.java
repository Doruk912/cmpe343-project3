package com.example.database;

import com.example.model.Role;
import com.example.model.User;

import java.sql.*;

public class DatabaseConnection {

    private static final String URL = "jdbc:mysql://localhost:3306/group20";
    private static final String USER = "myuser";
    private static final String PASSWORD = "1234";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public User getUser(String username) {
        String query = "SELECT * FROM users WHERE username = ?";
        User user = null;
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                user = new User(resultSet.getInt("id"), resultSet.getString("username"), resultSet.getString("password"), Role.valueOf(resultSet.getString("role")));
            }
        } catch (SQLException e) {
            System.out.println("An error occurred while getting the user");
            e.printStackTrace();
        }
        return user;
    }
}
