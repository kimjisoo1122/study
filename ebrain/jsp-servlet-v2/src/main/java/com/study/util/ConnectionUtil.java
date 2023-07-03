package com.study.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public interface ConnectionUtil {

    String DB_URL = "jdbc:mysql://localhost:3306/ebrainsoft_study";
    String USER = "ebsoft";
    String PASS = "ebsoft";

    static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Not Found JDBC driver", e);
        }
        return DriverManager.getConnection(DB_URL, USER, PASS);
    }
}