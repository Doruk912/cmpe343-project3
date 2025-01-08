package com.example.database;

import com.example.model.Movie;
import com.example.model.Role;
import com.example.model.User;

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
                user = new User(resultSet.getInt("id"), resultSet.getString("username"), resultSet.getString("password"), Role.valueOf(resultSet.getString("role")));
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
}
