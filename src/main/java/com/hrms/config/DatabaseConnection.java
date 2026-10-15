package com.hrms.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private static final String URL = "jdbc:mysql://localhost:3306/hrms_db";

    private static final String USERNAME = "root";

    private static final String PASSWORD = System.getenv("HRMS_DB_PASSWORD");

    public static Connection getConnection()
            throws SQLException {

        if (PASSWORD == null || PASSWORD.isBlank()) {
            throw new SQLException(
                    "HRMS_DB_PASSWORD environment variable is not configured."
            );
        }

        return DriverManager.getConnection(
                URL, USERNAME, PASSWORD
        );
    }
}
