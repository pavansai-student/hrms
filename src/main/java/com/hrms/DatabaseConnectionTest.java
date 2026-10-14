package com.hrms;
import com.hrms.config.DatabaseConnection;
import java.sql.Connection;
import java.sql.SQLException;

    public class DatabaseConnectionTest {

        public static void main(String[] args) {

            try (Connection connection =
                         DatabaseConnection.getConnection()) {

                System.out.println("MySQL connection successful!");
                System.out.println(
                        "Database: "
                                + connection.getCatalog()
                );

            } catch (SQLException e) {
                System.out.println(
                        "Connection failed: " + e.getMessage()
                );
            }
        }
    }