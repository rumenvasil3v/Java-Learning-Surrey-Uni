package com.blog.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseUtil {
    private static final String DB_URL = "jdbc:sqlite:blog.db";
    
    static {
        try {
            Class.forName("org.sqlite.JDBC");
            initializeDatabase();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }
    
    private static void initializeDatabase() {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            
            // Users table
            stmt.execute("CREATE TABLE IF NOT EXISTS users (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "username TEXT UNIQUE NOT NULL," +
                "email TEXT UNIQUE NOT NULL," +
                "password TEXT NOT NULL," +
                "full_name TEXT NOT NULL," +
                "bio TEXT," +
                "created_at TEXT NOT NULL)");
            
            // Posts table
            stmt.execute("CREATE TABLE IF NOT EXISTS posts (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "title TEXT NOT NULL," +
                "content TEXT NOT NULL," +
                "author_id INTEGER NOT NULL," +
                "author_name TEXT NOT NULL," +
                "created_at TEXT NOT NULL," +
                "updated_at TEXT NOT NULL," +
                "views INTEGER DEFAULT 0," +
                "FOREIGN KEY (author_id) REFERENCES users(id))");
            
            // Comments table
            stmt.execute("CREATE TABLE IF NOT EXISTS comments (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "post_id INTEGER NOT NULL," +
                "user_id INTEGER NOT NULL," +
                "username TEXT NOT NULL," +
                "content TEXT NOT NULL," +
                "created_at TEXT NOT NULL," +
                "FOREIGN KEY (post_id) REFERENCES posts(id)," +
                "FOREIGN KEY (user_id) REFERENCES users(id))");
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
