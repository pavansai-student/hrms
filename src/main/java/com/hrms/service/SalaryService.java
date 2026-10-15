package com.hrms.service;

import com.hrms.dao.LoginDAO.OrganizationSession;
import com.hrms.dao.SalaryDAO;
import com.hrms.model.Salary;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

public class SalaryService {

    private final SalaryDAO salaryDAO = new SalaryDAO();

    public void showMenu(
            Scanner scanner,
            OrganizationSession session) {

        boolean running = true;

        while (running) {
            System.out.println("\n===== SALARY MANAGEMENT =====");
            System.out.println("1. Create Salary");
            System.out.println("2. View Salaries");
            System.out.println("3. Update Salary");
            System.out.println("4. Delete Salary");
            System.out.println("5. Back to Dashboard");
            System.out.print("Enter your choice: ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1" -> createSalary(scanner, session);
                case "2" -> viewSalaries(session);
                case "3" -> updateSalary(scanner, session);
                case "4" -> deleteSalary(scanner, session);
                case "5" -> running = false;
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    private void createSalary(
            Scanner scanner,
            OrganizationSession session) {

        try {
            long employeeId = readPositiveId(
                    scanner, "Employee ID: ");

            System.out.print("Pay month (YYYY-MM, e.g. 2026-10): ");
            String monthInput = scanner.nextLine().trim();

            LocalDate payMonth = LocalDate.parse(
                    monthInput + "-01");

            BigDecimal basic = readAmount(scanner, "Basic salary: ");
            BigDecimal allowances =
                    readAmount(scanner, "Allowances: ");
            BigDecimal deductions =
                    readAmount(scanner, "Deductions: ");

            if (deductions.compareTo(basic.add(allowances)) > 0) {
                System.out.println(
                        "Deductions cannot exceed total earnings.");
                return;
            }

            Salary salary = new Salary(
                    session.getOrganizationId(),
                    employeeId,
                    payMonth,
                    basic,
                    allowances,
                    deductions,
                    "PENDING"
            );

            boolean created = salaryDAO.createSalary(salary);

            System.out.println(created
                    ? "Salary created successfully. Net salary: "
                    + salary.getNetSalary()
                    : "Salary was not created. Check the employee ID, "
                    + "organization, and employee status.");

        } catch (DateTimeParseException e) {
            System.out.println("Invalid month. Use YYYY-MM.");
        } catch (NumberFormatException e) {
            System.out.println(
                    "Enter valid numeric IDs and salary amounts.");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        } catch (SQLException e) {
            handleDatabaseError(e);
        }
    }

    private void viewSalaries(OrganizationSession session) {
        try {
            List<Salary> salaries = salaryDAO.getAllSalaries(
                    session.getOrganizationId());

            System.out.println("\n===== SALARY RECORDS =====");

            if (salaries.isEmpty()) {
                System.out.println("No salary records found.");
                return;
            }

            for (Salary s : salaries) {
                System.out.println("-------------------------");
                System.out.println("Salary ID: " + s.getId());
                System.out.println("Employee ID: " + s.getEmployeeId());
                System.out.println("Pay month: " + s.getPayMonth());
                System.out.println("Basic: " + s.getBasicSalary());
                System.out.println("Allowances: " + s.getAllowances());
                System.out.println("Deductions: " + s.getDeductions());
                System.out.println("Net salary: " + s.getNetSalary());
                System.out.println(
                        "Status: " + s.getPaymentStatus());
            }

        } catch (SQLException e) {
            handleDatabaseError(e);
        }
    }

    private void updateSalary(
            Scanner scanner,
            OrganizationSession session) {

        try {
            long salaryId = readPositiveId(
                    scanner, "Salary ID to update: ");

            BigDecimal basic = readAmount(
                    scanner, "New basic salary: ");
            BigDecimal allowances = readAmount(
                    scanner, "New allowances: ");
            BigDecimal deductions = readAmount(
                    scanner, "New deductions: ");

            if (deductions.compareTo(basic.add(allowances)) > 0) {
                System.out.println(
                        "Deductions cannot exceed total earnings.");
                return;
            }

            System.out.print("Payment status (PENDING/PAID): ");
            String status = scanner.nextLine().trim().toUpperCase();

            if (!status.equals("PENDING") && !status.equals("PAID")) {
                System.out.println("Invalid payment status.");
                return;
            }

            boolean updated = salaryDAO.updateSalary(
                    session.getOrganizationId(),
                    salaryId,
                    basic,
                    allowances,
                    deductions,
                    status
            );

            System.out.println(updated
                    ? "Salary updated successfully."
                    : "Salary record not found.");

        } catch (NumberFormatException e) {
            System.out.println("Enter valid numeric values.");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        } catch (SQLException e) {
            handleDatabaseError(e);
        }
    }

    private void deleteSalary(
            Scanner scanner,
            OrganizationSession session) {

        try {
            long salaryId = readPositiveId(
                    scanner, "Salary ID to delete: ");

            System.out.print(
                    "Type YES to confirm salary deletion: ");

            if (!scanner.nextLine().trim().equals("YES")) {
                System.out.println("Deletion cancelled.");
                return;
            }

            boolean deleted = salaryDAO.deleteSalary(
                    session.getOrganizationId(), salaryId);

            System.out.println(deleted
                    ? "Salary deleted successfully."
                    : "Salary record not found.");

        } catch (NumberFormatException e) {
            System.out.println("Enter a valid salary ID.");
        } catch (SQLException e) {
            handleDatabaseError(e);
        }
    }

    private long readPositiveId(
            Scanner scanner, String prompt) {

        System.out.print(prompt);
        long id = Long.parseLong(scanner.nextLine().trim());

        if (id <= 0) {
            throw new NumberFormatException(
                    "ID must be positive.");
        }

        return id;
    }

    private BigDecimal readAmount(
            Scanner scanner, String prompt) {

        System.out.print(prompt);
        BigDecimal amount =
                new BigDecimal(scanner.nextLine().trim());

        if (amount.signum() < 0) {
            throw new IllegalArgumentException(
                    "Salary amounts cannot be negative.");
        }

        return amount;
    }

    private void handleDatabaseError(SQLException e) {
        if ("23000".equals(e.getSQLState())) {
            System.out.println(
                    "Duplicate salary record or database constraint "
                            + "violation. Check employee and pay month.");
        } else {
            System.out.println("Salary database operation failed.");
            System.err.println(e.getMessage());
        }
    }
}
