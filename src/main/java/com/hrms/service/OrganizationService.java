package com.hrms.service;

import com.hrms.dao.OrganizationDAO;
import com.hrms.model.Organization;

import java.sql.SQLException;
import java.util.Scanner;

public class OrganizationService {

    private final OrganizationDAO organizationDAO =
            new OrganizationDAO();

    public void registerOrganization(Scanner scanner) {

        System.out.println("\n===== CREATE ORGANIZATION =====");

        System.out.print("Organization name: ");
        String name = scanner.nextLine().trim();

        System.out.print("Address: ");
        String address = scanner.nextLine().trim();

        System.out.print("PAN: ");
        String pan = scanner.nextLine().trim().toUpperCase();

        System.out.print("GST (optional): ");
        String gst = scanner.nextLine().trim().toUpperCase();

        System.out.print("Department: ");
        String department = scanner.nextLine().trim();

        System.out.print("Email for login: ");
        String email = scanner.nextLine().trim().toLowerCase();

        System.out.print("Password: ");
        String password = scanner.nextLine();

        System.out.print("Organization employee count: ");
        String countInput = scanner.nextLine().trim();

        // Validate required fields.
        if (name.isBlank()
                || address.isBlank()
                || department.isBlank()
                || !email.matches(
                "^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$")) {

            System.out.println(
                    "Invalid input. Check required fields and email.");
            return;
        }

        // Validate PAN.
        if (!pan.matches("[A-Z]{5}[0-9]{4}[A-Z]")) {
            System.out.println("Invalid PAN format.");
            return;
        }

        // Validate GST when provided.
        if (!gst.isEmpty()
                && !gst.matches(
                "[0-9]{2}[A-Z]{5}[0-9]{4}[A-Z]"
                        + "[1-9A-Z]Z[0-9A-Z]")) {

            System.out.println("Invalid GST format.");
            return;
        }

        if (password.isBlank() || password.length() > 255) {
            System.out.println(
                    "Password must not be empty or exceed 255 characters.");
            return;
        }

        int employeeCount;

        try {
            employeeCount = Integer.parseInt(countInput);

            if (employeeCount < 0) {
                System.out.println(
                        "Employee count cannot be negative.");
                return;
            }

        } catch (NumberFormatException e) {
            System.out.println(
                    "Employee count must be a whole number.");
            return;
        }

        Organization organization = new Organization(
                name,
                address,
                pan,
                gst,
                department,
                email,
                employeeCount
        );

        // Register the organization and its login account.
        try {
            long id = organizationDAO.registerOrganization(
                    organization, password);

            System.out.println(
                    "\nOrganization created successfully!");
            System.out.println("Organization ID: " + id);
            System.out.println(
                    "You can now log in using your registered email.");

        } catch (SQLException e) {

            if ("23000".equals(e.getSQLState())) {
                System.out.println(
                        "Registration failed: email, PAN, or GST "
                                + "may already be registered. "
                                + "Please check your details.");
            } else {
                System.out.println(
                        "Registration failed due to a database error.");
                System.err.println(e.getMessage());
            }
        }
    }
}