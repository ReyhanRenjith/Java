package com.java.revaniexchange.repository;

import com.java.revaniexchange.model.Product;
import com.java.revaniexchange.model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Manages all database operations (JDBC).
 */
public class DatabaseManager {
    // Using H2 in-memory database. It will be created and destroyed with the app.
    private static final String DB_URL = "jdbc:h2:mem:store;DB_CLOSE_DELAY=-1";
    private Connection conn;

    public DatabaseManager() {
        try {
            conn = DriverManager.getConnection(DB_URL);
        } catch (SQLException e) {
            System.err.println("Database connection failed: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void initDatabaseAndSeed() {
        try (Statement stmt = conn.createStatement()) {
            String createUserTable = "CREATE TABLE IF NOT EXISTS users (id INT AUTO_INCREMENT PRIMARY KEY, username VARCHAR(255) UNIQUE NOT NULL, password VARCHAR(255) NOT NULL)";
            String createProductsTable = "CREATE TABLE IF NOT EXISTS products (id INT AUTO_INCREMENT PRIMARY KEY, name VARCHAR(255) NOT NULL, description VARCHAR(1024), price DOUBLE NOT NULL, imageUrl VARCHAR(1024))";
            String createOrdersTable = "CREATE TABLE IF NOT EXISTS orders (order_id INT AUTO_INCREMENT PRIMARY KEY, user_id INT, product_id INT, FOREIGN KEY (user_id) REFERENCES users(id), FOREIGN KEY (product_id) REFERENCES products(id))";

            stmt.execute(createUserTable);
            stmt.execute(createProductsTable);
            stmt.execute(createOrdersTable);

            ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM users");
            if (rs.next() && rs.getInt(1) == 0) {
                System.out.println("Seeding database with initial data...");
                stmt.execute("INSERT INTO users(username, password) VALUES('customer', 'password123')");
                stmt.execute("INSERT INTO products(name, description, price, imageUrl) VALUES('Classic Running Shoes', 'Lightweight and durable running shoes, perfect for daily exercise and marathon training.', 79.99, 'https://placehold.co/400x400/e0f2fe/0ea5e9?text=Shoe+A')");
                stmt.execute("INSERT INTO products(name, description, price, imageUrl) VALUES('Leather Hiking Boots', 'Waterproof leather boots designed for tough trails. Provides excellent ankle support and superior grip.', 149.50, 'https://placehold.co/400x400/fefce8/ca8a04?text=Boot+B')");
                stmt.execute("INSERT INTO products(name, description, price, imageUrl) VALUES('Casual Canvas Sneakers', 'Stylish and comfortable sneakers for everyday wear.', 55.00, 'https://placehold.co/400x400/ecfccb/4d7c0f?text=Sneaker+C')");
                stmt.execute("INSERT INTO products(name, description, price, imageUrl) VALUES('Formal Oxford Shoes', 'Elegant and timeless oxford shoes, crafted from genuine leather.', 180.00, 'https://placehold.co/400x400/1e293b/94a3b8?text=Oxford+D')");
                System.out.println("Seeding complete.");
            }
        } catch (SQLException e) {
            System.err.println("Error initializing database schema: " + e.getMessage());
        }
    }

    public Optional<User> validateLogin(String username, String password) {
        String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return Optional.of(new User(rs.getInt("id"), rs.getString("username")));
            }
        } catch (SQLException e) {
            System.err.println("Error validating login: " + e.getMessage());
        }
        return Optional.empty();
    }

    public List<Product> getAllProducts() {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT * FROM products";
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                products.add(new Product(rs.getInt("id"), rs.getString("name"), rs.getString("description"), rs.getDouble("price"), rs.getString("imageUrl")));
            }
        } catch (SQLException e) {
            System.err.println("Error fetching products: " + e.getMessage());
        }
        return products;
    }

    public void placeOrder(int userId, int productId) {
        String sql = "INSERT INTO orders(user_id, product_id) VALUES (?, ?)";
        try(PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            pstmt.setInt(2, productId);
            pstmt.executeUpdate();
        } catch(SQLException e) {
            System.err.println("Error placing order: " + e.getMessage());
        }
    }

    public List<Product> getOrdersForUser(int userId) {
        List<Product> orderedProducts = new ArrayList<>();
        String sql = "SELECT p.* FROM products p JOIN orders o ON p.id = o.product_id WHERE o.user_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                orderedProducts.add(new Product(rs.getInt("id"), rs.getString("name"), rs.getString("description"), rs.getDouble("price"), rs.getString("imageUrl")));
            }
        } catch (SQLException e) {
            System.err.println("Error fetching user orders: " + e.getMessage());
        }
        return orderedProducts;
    }

    public boolean registerUser(String username, String password) {
        String sql = "INSERT INTO users(username, password) VALUES (?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Error registering user: " + e.getMessage());
            return false;
        }
    }
    
    public void closeConnection() {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
                System.out.println("Database connection closed.");
            }
        } catch (SQLException e) {
            System.err.println("Error closing database connection: " + e.getMessage());
        }
    }
}
