package com.example.database;

import com.example.model.Movie;
import com.example.model.Product;
import com.example.model.Role;
import com.example.model.User;
import com.example.model.Session;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * This class provides methods for interacting with the database, including operations for movies, products,
 * users, and sessions.
 * 
 * <p>It allows for querying, inserting, updating, and deleting records in various tables, such as movies, products, 
 * users, and sessions, and supports getting information about prices and available seats.</p>
 */
public class DatabaseConnection {

    private static final String URL = "jdbc:mysql://localhost:3306/group20";
    private static final String USER = "myuser";
    private static final String PASSWORD = "1234";

    /**
     * Establishes a connection to the database.
     * 
     * @return A {@link Connection} object that represents the established database connection.
     * @throws SQLException If a database access error occurs.
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    /**
     * Retrieves a user from the database based on their username.
     *
     * @param username The username of the user to be retrieved.
     * @return A {@link User} object representing the user, or {@code null} if no such user exists.
     */
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

    /**
     * Retrieves all movies from the database.
     *
     * @return A list of {@link Movie} objects representing all movies in the database.
     */
    public List<Movie> getAllMovies() {
        String query = "SELECT * FROM movies";  // Assuming table name is 'movies'
        List<Movie> movies = new ArrayList<>();
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String title = resultSet.getString("title");
                String genre = resultSet.getString("genre");
                String poster = resultSet.getString("poster");
                String summary = resultSet.getString("summary");
                movies.add(new Movie(id, title, genre, poster, summary));
            }
        } catch (SQLException e) {
            System.out.println("An error occurred while getting the movies");
            e.printStackTrace();
        }
        return movies;
    }

    /**
     * Adds a new movie to the database.
     *
     * @param movie The {@link Movie} object representing the movie to be added.
     * @return {@code true} if the movie was successfully added, {@code false} otherwise.
     */
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

    /**
     * Updates an existing movie in the database.
     *
     * @param title The title of the movie to be updated.
     * @param movie The {@link Movie} object containing the updated movie data.
     * @return {@code true} if the movie was successfully updated, {@code false} otherwise.
     */
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

    /**
     * Retrieves all products from the database.
     *
     * @return A list of {@link Product} objects representing all products in the database.
     */
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

    /**
     * Adds a new product to the database.
     *
     * @param product The {@link Product} object representing the product to be added.
     * @return {@code true} if the product was successfully added, {@code false} otherwise.
     */
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

    /**
     * Updates an existing product in the database.
     *
     * @param product The {@link Product} object containing the updated product data.
     * @return {@code true} if the product was successfully updated, {@code false} otherwise.
     */
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

    /**
     * Deletes a product from the database.
     *
     * @param selectedProduct The {@link Product} object representing the product to be deleted.
     * @return {@code true} if the product was successfully deleted, {@code false} otherwise.
     */
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

    /**
     * Retrieves all users from the database.
     *
     * @return A list of {@link User} objects representing all users in the database.
     */
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

    /**
     * Adds a new user to the database.
     *
     * @param user The {@link User} object representing the user to be added.
     * @return {@code true} if the user was successfully added, {@code false} otherwise.
     */
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

    /**
     * Updates an existing user in the database.
     *
     * @param user The {@link User} object containing the updated user data.
     * @return {@code true} if the user was successfully updated, {@code false} otherwise.
     */
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

    /**
     * Removes a user from the database.
     *
     * @param selectedUser The {@link User} object representing the user to be removed.
     * @return {@code true} if the user was successfully removed, {@code false} otherwise.
     */
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

    /**
     * Retrieves all distinct movie genres from the database.
     *
     * @return A list of strings representing all distinct movie genres.
     */
    public List<String> getAllGenres() {
        String query = "SELECT DISTINCT genre FROM movies";
        List<String> genres = new ArrayList<>();
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                genres.add(resultSet.getString("genre"));
            }
        } catch (SQLException e) {
            System.out.println("An error occurred while getting the genres");
            e.printStackTrace();
        }
        return genres;
    }

    /**
     * Retrieves the current ticket price from the database.
     *
     * @return A {@link BigDecimal} representing the current ticket price, or {@code null} if an error occurs.
     */
    public BigDecimal getTicketPrice() {
        String query = "SELECT ticket FROM prices";
        try (Connection connection = getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getBigDecimal("ticket");
            }
        } catch (SQLException e) {
            System.out.println("An error occurred while getting the ticket price");
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Retrieves the current discount rate from the database.
     *
     * @return A {@link BigDecimal} representing the current discount rate, or {@code null} if an error occurs.
     */
    public BigDecimal getDiscountRate() {
        String query = "SELECT discount FROM prices";
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getBigDecimal("discount");
            }
        } catch (SQLException e) {
            System.out.println("An error occurred while getting the discount rate");
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Updates the ticket price and discount rate in the database.
     *
     * @param newTicketPrice The new ticket price.
     * @param newDiscountRate The new discount rate.
     * @return {@code true} if the prices were successfully updated, {@code false} otherwise.
     */
    public boolean updatePrices(BigDecimal newTicketPrice, BigDecimal newDiscountRate) {
        String query = "UPDATE prices SET ticket = ?, discount = ?";
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setBigDecimal(1, newTicketPrice);
            preparedStatement.setBigDecimal(2, newDiscountRate);
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("An error occurred while updating the prices");
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Retrieves all sessions for a specific movie based on its ID.
     *
     * @param id The ID of the movie.
     * @return A list of {@link Session} objects representing all sessions for the specified movie.
     */
    public List<Session> getSessionsForMovie(int id) {
        String query = "SELECT * FROM sessions WHERE movie_id = ?";
        List<Session> sessions = new ArrayList<>();
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int sessionId = resultSet.getInt("id");
                int movieId = resultSet.getInt("movie_id");
                LocalDate date = resultSet.getDate("date").toLocalDate();
                LocalTime time = resultSet.getTime("time").toLocalTime();
                String location = resultSet.getString("location");
                sessions.add(new Session(sessionId, movieId, date, time, location));
            }
        } catch (SQLException e) {
            System.out.println("An error occurred while getting the sessions");
            e.printStackTrace();
        }
        return sessions;
    }


    /**
     * Adds a new session to the database and initializes seats for the session
     * based on the hall's capacity.
     *
     * @param session The session object containing the details of the session to be added.
     * @return {@code true} if the session and its seats were successfully added, {@code false} otherwise.
     *
     * This method performs the following steps:
     * <ul>
     *     <li>Fetches the hall's capacity based on the provided location (hall name).</li>
     *     <li>Inserts the session details into the {@code sessions} table.</li>
     *     <li>Retrieves the auto-generated session ID.</li>
     *     <li>Initializes seats in the {@code seats} table based on the hall's capacity.</li>
     * </ul>
     */
    public boolean addSession(Session session) {
        String sessionQuery = "INSERT INTO sessions (movie_id, date, time, location) VALUES (?, ?, ?, ?)";
        String seatQuery = "INSERT INTO seats (session_id, seat_number) VALUES (?, ?)";
        String capacityQuery = "SELECT capacity FROM halls WHERE name = ?";

        try (Connection connection = getConnection();
             PreparedStatement sessionStatement = connection.prepareStatement(sessionQuery, Statement.RETURN_GENERATED_KEYS);
             PreparedStatement seatStatement = connection.prepareStatement(seatQuery);
             PreparedStatement capacityStatement = connection.prepareStatement(capacityQuery)) {

            capacityStatement.setString(1, session.getLocation());
            ResultSet capacityResult = capacityStatement.executeQuery();

            int hallCapacity = 0;
            if (capacityResult.next()) {
                hallCapacity = capacityResult.getInt("capacity");
            } else {
                System.out.println("Error: Hall not found: " + session.getLocation());
                return false;
            }

            sessionStatement.setInt(1, session.getMovieId());
            sessionStatement.setDate(2, Date.valueOf(session.getDate()));
            sessionStatement.setTime(3, Time.valueOf(session.getTime()));
            sessionStatement.setString(4, session.getLocation());
            int rowsAffected = sessionStatement.executeUpdate();

            if (rowsAffected > 0) {
                ResultSet generatedKeys = sessionStatement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int sessionId = generatedKeys.getInt(1);

                    for (int seatNumber = 1; seatNumber <= hallCapacity; seatNumber++) {
                        seatStatement.setInt(1, sessionId);
                        seatStatement.setInt(2, seatNumber);
                        seatStatement.addBatch();
                    }
                    seatStatement.executeBatch();
                }
                return true;
            }
        } catch (SQLException e) {
            System.out.println("An error occurred while adding the session and initializing seats");
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Updates an existing session in the database and reinitializes seats
     * if the hall (location) is changed.
     *
     * @param session The session object containing the updated details of the session.
     * @return {@code true} if the session was successfully updated and seats were reinitialized (if needed),
     *         {@code false} otherwise.
     *
     * This method performs the following steps:
     * <ul>
     *     <li>Updates the session details (e.g., date, time, location) in the {@code sessions} table.</li>
     *     <li>Fetches the hall's capacity based on the updated location (hall name).</li>
     *     <li>Deletes existing seats if the location changes.</li>
     *     <li>Initializes new seats in the {@code seats} table based on the new hall's capacity.</li>
     * </ul>
     */
    public boolean updateSession(Session session) {
        String updateSessionQuery = "UPDATE sessions SET date = ?, time = ?, location = ? WHERE id = ?";
        String capacityQuery = "SELECT capacity FROM halls WHERE name = ?";
        String deleteSeatsQuery = "DELETE FROM seats WHERE session_id = ?";
        String insertSeatsQuery = "INSERT INTO seats (session_id, seat_number) VALUES (?, ?)";

        try (Connection connection = getConnection();
             PreparedStatement updateSessionStatement = connection.prepareStatement(updateSessionQuery);
             PreparedStatement capacityStatement = connection.prepareStatement(capacityQuery);
             PreparedStatement deleteSeatsStatement = connection.prepareStatement(deleteSeatsQuery);
             PreparedStatement insertSeatsStatement = connection.prepareStatement(insertSeatsQuery)) {


            updateSessionStatement.setDate(1, Date.valueOf(session.getDate()));
            updateSessionStatement.setTime(2, Time.valueOf(session.getTime()));
            updateSessionStatement.setString(3, session.getLocation());
            updateSessionStatement.setInt(4, session.getId());
            int rowsUpdated = updateSessionStatement.executeUpdate();

            if (rowsUpdated > 0) {
                // Fetch the hall capacity based on the updated location
                capacityStatement.setString(1, session.getLocation());
                ResultSet capacityResult = capacityStatement.executeQuery();

                int hallCapacity = 0;
                if (capacityResult.next()) {
                    hallCapacity = capacityResult.getInt("capacity");
                } else {
                    System.out.println("Error: Hall not found: " + session.getLocation());
                    return false; // Invalid hall
                }


                deleteSeatsStatement.setInt(1, session.getId());
                deleteSeatsStatement.executeUpdate();

                for (int seatNumber = 1; seatNumber <= hallCapacity; seatNumber++) {
                    insertSeatsStatement.setInt(1, session.getId());
                    insertSeatsStatement.setInt(2, seatNumber);
                    insertSeatsStatement.addBatch();
                }
                insertSeatsStatement.executeBatch();

                return true;
            }
        } catch (SQLException e) {
            System.out.println("An error occurred while updating the session and reinitializing seats");
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Removes an existing session and all its associated seats from the database.
     *
     * @param selectedSession The session object representing the session to be removed.
     * @return {@code true} if the session and its associated seats were successfully removed, {@code false} otherwise.
     *
     * This method performs the following steps:
     * <ul>
     *     <li>Deletes all seats associated with the specified session ID from the {@code seats} table.</li>
     *     <li>Deletes the session entry from the {@code sessions} table.</li>
     * </ul>
     */
    public boolean removeSession(Session selectedSession) {
        String deleteSeatsQuery = "DELETE FROM seats WHERE session_id = ?";
        String deleteSessionQuery = "DELETE FROM sessions WHERE id = ?";
        try (Connection connection = getConnection();
             PreparedStatement deleteSeatsStatement = connection.prepareStatement(deleteSeatsQuery);
             PreparedStatement deleteSessionStatement = connection.prepareStatement(deleteSessionQuery)) {

            // Delete associated seats
            deleteSeatsStatement.setInt(1, selectedSession.getId());
            deleteSeatsStatement.executeUpdate();

            // Delete the session
            deleteSessionStatement.setInt(1, selectedSession.getId());
            return deleteSessionStatement.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("An error occurred while removing the session and its associated seats");
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Retrieves the current discount percentage from the database.
     *
     * @return A {@link BigDecimal} representing the current discount percentage, or {@code null} if an error occurs.
     */
    public BigDecimal getDiscountPercentage() {
        String query = "SELECT discount FROM prices";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            if (resultSet.next()) {
                return resultSet.getBigDecimal("discount");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Retrieves the available seats for a given movie session.
     *
     * @param movieId The ID of the movie.
     * @param date The date of the session.
     * @param location The location of the session.
     * @return A list of integers representing the available seat numbers.
     */
    public List<Integer> getAvailableSeats(int movieId, LocalDate date, String location) {
        List<Integer> availableSeats = new ArrayList<>();
        try (Connection connection = getConnection()) {
            String query = "SELECT seat_number FROM seats WHERE session_id = (SELECT id FROM sessions WHERE movie_id = ? AND date = ? AND location = ?) AND is_taken = FALSE";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, movieId);
            statement.setDate(2, java.sql.Date.valueOf(date));
            statement.setString(3, location);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                availableSeats.add(resultSet.getInt("seat_number"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return availableSeats;
    }

    public boolean takeSeats(int movieId, LocalDate date, String location, List<Integer> seatNumbers) {
        String query = "UPDATE seats SET is_taken = TRUE WHERE session_id = (SELECT id FROM sessions WHERE movie_id = ? AND date = ? AND location = ?) AND seat_number = ?";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            for (int seatNumber : seatNumbers) {
                statement.setInt(1, movieId);
                statement.setDate(2, java.sql.Date.valueOf(date));
                statement.setString(3, location);
                statement.setInt(4, seatNumber);
                statement.addBatch();
            }
            int[] updateCounts = statement.executeBatch();
            for (int count : updateCounts) {
                if (count == 0) {
                    return false;
                }
            }
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
