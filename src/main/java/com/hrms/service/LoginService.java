package com.hrms.service;

import com.hrms.dao.LoginDAO;
import com.hrms.dao.LoginDAO.OrganizationSession;
import com.hrms.ui.OrganizationDashboard;

import java.sql.SQLException;
import java.util.Scanner;

public class LoginService {

    private final LoginDAO loginDAO = new LoginDAO();

    private final OrganizationDashboard dashboard =
            new OrganizationDashboard();

    // Returns false when the user chooses Exit.
    public boolean showLoginMenu(Scanner scanner) {

        while (true) {
            System.out.println("\n===== ORGANIZATION LOGIN =====");
            System.out.println("1. Enter Email and Password");
            System.out.println("2. Forgot Password");
            System.out.println("3. Back to Main Menu");
            System.out.println("4. Exit");
            System.out.print("Enter your choice: ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    if (performLogin(scanner)) {
                        return true;
                    }
                    break;

                case "2":
                    System.out.println(
                            "Password recovery will be implemented next.");
                    break;

                case "3":
                    return true;

                case "4":
                    System.out.println(
                            "Thank you for using HRMS!");
                    return false;

                default:
                    System.out.println(
                            "Invalid choice. Enter 1 to 4.");
            }
        }
    }

    private boolean performLogin(Scanner scanner) {

        System.out.print("Enter email: ");
        String email = scanner.nextLine()
                .trim().toLowerCase();

        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        if (email.isBlank() || password.isBlank()) {
            System.out.println(
                    "Email and password are required.");
            return false;
        }

        try {
            OrganizationSession session =
                    loginDAO.login(email, password);

            if (session == null) {
                System.out.println(
                        "Invalid credentials or inactive account.");
                return false;
            }

            System.out.println(
                    "\nLogin successful!");
            System.out.println(
                    "Welcome to " + session.getOrganizationName());

            // Show the dashboard until the user logs out.
            dashboard.show(scanner, session);

            // After logout, return to the main menu.
            return true;

        } catch (SQLException e) {
            System.out.println(
                    "Login failed due to a database error.");
            System.err.println(e.getMessage());
            return false;
        }
    }
}
