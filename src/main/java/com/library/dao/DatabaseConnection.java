package com.library.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.PreparedStatement;

public class DatabaseConnection {
    private static final String URL = "jdbc:sqlite:scenario.db";

    public static Connection getConnection() throws SQLException {
        Connection conn = DriverManager.getConnection(URL);
        try (PreparedStatement ps = conn.prepareStatement("PRAGMA foreign_keys = ON;")) {
            ps.execute();
        }
        return conn;
    }
}
