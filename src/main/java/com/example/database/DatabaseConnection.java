package com.example.database;

import com.example.model.Movie;
import com.example.model.Product;
import com.example.model.Role;
import com.example.model.User;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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
                user = new User(resultSet.getInt("id"), resultSet.getString("username"), resultSet.getString("name"), resultSet.getString("password"), Role.valueOf(resultSet.getString("role")));
            }
        } catch (SQLException e) {
            System.out.println("An error occurred while getting the user");
            e.printStackTrace();
        }
        return user;
    }

    public List<Movie> getAllMovies() {
        String query = "SELECT * FROM movies";  // Assuming table name is 'movies'
        List<Movie> movies = new ArrayList<>();
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String title = resultSet.getString("title");
                String genre = resultSet.getString("genre");
                String poster = resultSet.getString("poster");
                String summary = resultSet.getString("summary");
                movies.add(new Movie(title, genre, poster, summary));
            }
        } catch (SQLException e) {
            System.out.println("An error occurred while getting the movies");
            e.printStackTrace();
        }
        return movies;
    }

    public boolean addMovie(Movie movie) {
        String query = "INSERT INTO movies (title, genre, poster, summary) VALUES (?, ?, ?, ?)";
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, movie.getTitle());
            preparedStatement.setString(2, movie.getGenre());
            preparedStatement.setString(3, movie.getPoster());
            preparedStatement.setString(4, movie.getSummary());
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("An error occurred while adding the movie");
            e.printStackTrace();
        }
        return false;
    }


    public boolean updateMovie(String title, Movie movie) {
        String query = "UPDATE movies SET title = ?, genre = ?, poster = ?, summary = ? WHERE title = ?";
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, movie.getTitle());
            preparedStatement.setString(2, movie.getGenre());
            preparedStatement.setString(3, movie.getPoster());
            preparedStatement.setString(4, movie.getSummary());
            preparedStatement.setString(5, title);
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("An error occurred while updating the movie");
            e.printStackTrace();
        }
        return false;
    }

    public List<Product> getAllProducts() {
        String query = "SELECT * FROM products";  // Assuming table name is 'products'
        List<Product> products = new ArrayList<>();
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                BigDecimal price = resultSet.getBigDecimal("price");
                int quantity = resultSet.getInt("quantity");
                products.add(new Product(id, name, price, quantity));
            }
        } catch (SQLException e) {
            System.out.println("An error occurred while getting the products");
            e.printStackTrace();
        }
        return products;
    }

    public boolean addProduct(Product product) {
        String query = "INSERT INTO products (name, price, quantity) VALUES (?, ?, ?)";
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, product.getName());
            preparedStatement.setBigDecimal(2, product.getPrice());
            preparedStatement.setInt(3, product.getQuantity());
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("An error occurred while adding the product");
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateProduct(Product product) {
        String query = "UPDATE products SET name = ?, price = ?, quantity = ? WHERE id = ?";
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, product.getName());
            preparedStatement.setBigDecimal(2, product.getPrice());
            preparedStatement.setInt(3, product.getQuantity());
            preparedStatement.setInt(4, product.getId());
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("An error occurred while updating the product");
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteProduct(Product selectedProduct) {
        String query = "DELETE FROM products WHERE id = ?";
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, selectedProduct.getId());
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("An error occurred while deleting the product");
            e.printStackTrace();
        }
        return false;
    }

    public List<User> getAllUsers() {
        String query = "SELECT * FROM users";
        List<User> users = new ArrayList<>();
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String username = resultSet.getString("username");
                String name = resultSet.getString("name");
                String password = resultSet.getString("password");
                Role role = Role.valueOf(resultSet.getString("role"));
                users.add(new User(id, username, name, password, role));
            }
        } catch (SQLException e) {
            System.out.println("An error occurred while getting the users");
            e.printStackTrace();
        }
        return users;
    }

    public boolean addUser(User user) {
        String query = "INSERT INTO users (username, name, password, role) VALUES (?, ?, ?, ?)";
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setString(2, user.getName());
            preparedStatement.setString(3, user.getPassword());
            preparedStatement.setString(4, user.getRole().name());
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("An error occurred while adding the user");
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateUser(User user) {
        String query = "UPDATE users SET username = ?, name = ?, password = ?, role = ? WHERE id = ?";
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setString(2, user.getName());
            preparedStatement.setString(3, user.getPassword());
            preparedStatement.setString(4, user.getRole().name());
            preparedStatement.setInt(5, user.getId());
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("An error occurred while updating the user");
            e.printStackTrace();
        }
        return false;
    }

    public boolean removeUser(User selectedUser) {
        String query = "DELETE FROM users WHERE id = ?";
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, selectedUser.getId());
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("An error occurred while deleting the user");
            e.printStackTrace();
        }
        return false;
    }
}
