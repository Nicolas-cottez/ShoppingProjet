package com.example.shoppingprojet.DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/shopping"; // Change 'shopping' si le nom de ta BDD est différent
    private static final String USER = "root"; // À adapter selon ton config
    private static final String PASSWORD = ""; // À adapter aussi

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}