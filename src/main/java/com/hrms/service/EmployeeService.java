package com.hrms.service;

import com.hrms.dao.EmployeeDAO;
import com.hrms.dao.LoginDAO.OrganizationSession;
import com.hrms.model.Employee;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

public class EmployeeService {

    private final EmployeeDAO employeeDAO = new EmployeeDAO();

    public void showMenu(
            Scanner scanner,
            OrganizationSession session) {

        boolean running = true;

        while (running) {
            System.out.println("\n===== EMPLOYEE MANAGEMENT =====");
            System.out.println("1. Add Employee");
            System.out.println("2. View Employees");
            System.out.println("3. Update Employee");
            System.out.println("4. Delete Employee");
            System.out.println("5. Back to Dashboard");
            System.out.print("Enter your choice: ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1" -> addEmployee(scanner, session);
                case "2" -> viewEmployees(session);
                case "3" -> updateEmployee(scanner, session);
                case "4" -> deleteEmployee(scanner, session);
                case "5" -> running = false;
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    private void addEmployee(
            Scanner scanner,
            OrganizationSession session) {

        try {
            System.out.println("\n--- ADD EMPLOYEE ---");

            String code = readRequired(scanner, "Employee code: ");
            String firstName = readRequired(scanner, "First name: ");
            String lastName = readRequired(scanner, "Last name: ");
            String email = readRequired(scanner, "Email: ")
                    .toLowerCase();
            String phone = readOptional(scanner, "Phone (optional): ");
            String department = readRequired(scanner, "Department: ");
            String designation = readRequired(scanner, "Designation: ");

            LocalDate joiningDate = LocalDate.parse(
                    readRequired(scanner, "Joining date (YYYY-MM-DD): "));

            BigDecimal salary = new BigDecimal(
                    readRequired(scanner, "Salary: "));

            if (salary.signum() < 0) {
                System.out.println("Salary cannot be negative.");
                return;
            }

            if (!email.matches(
                    "^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$")) {
                System.out.println("Invalid email format.");
                return;
            }

            Employee employee = new Employee(
                    session.getOrganizationId(),
                    code,
                    firstName,
                    lastName,
                    email,
                    phone.isBlank() ? null : phone,
                    department,
                    designation,
                    joiningDate,
                    salary
            );

            if (employeeDAO.addEmployee(employee)) {
                System.out.println("Employee added successfully!");
            } else {
                System.out.println("Employee was not added.");
            }

        } catch (DateTimeParseException e) {
            System.out.println("Invalid date. Use YYYY-MM-DD.");
        } catch (NumberFormatException e) {
            System.out.println("Enter a valid numeric salary.");
        } catch (SQLException e) {
            handleDatabaseError(e);
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid input: " + e.getMessage());
        }
    }

    private void viewEmployees(OrganizationSession session) {
        try {
            List<Employee> employees =
                    employeeDAO.getEmployees(
                            session.getOrganizationId());

            System.out.println("\n===== EMPLOYEE LIST =====");

            if (employees.isEmpty()) {
                System.out.println("No employees found.");
                return;
            }

            for (Employee e : employees) {
                System.out.println("-------------------------");
                System.out.println("Code: " + e.getEmployeeCode());
                System.out.println("Name: "
                        + e.getFirstName() + " " + e.getLastName());
                System.out.println("Email: " + e.getEmail());
                System.out.println("Phone: " + e.getPhone());
                System.out.println("Department: " + e.getDepartment());
                System.out.println("Designation: " + e.getDesignation());
                System.out.println("Joining date: " + e.getJoiningDate());
                System.out.println("Salary: " + e.getSalary());
            }

        } catch (SQLException e) {
            handleDatabaseError(e);
        }
    }

    private void updateEmployee(
            Scanner scanner,
            OrganizationSession session) {

        try {
            System.out.println("\n--- UPDATE EMPLOYEE ---");

            String code = readRequired(
                    scanner, "Employee code to update: ");

            String firstName = readRequired(scanner, "New first name: ");
            String lastName = readRequired(scanner, "New last name: ");
            String email = readRequired(scanner, "New email: ")
                    .toLowerCase();
            String phone = readOptional(scanner, "New phone (optional): ");
            String department = readRequired(scanner, "New department: ");
            String designation = readRequired(scanner, "New designation: ");

            BigDecimal salary = new BigDecimal(
                    readRequired(scanner, "New salary: "));

            if (salary.signum() < 0) {
                System.out.println("Salary cannot be negative.");
                return;
            }

            if (!email.matches(
                    "^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$")) {
                System.out.println("Invalid email format.");
                return;
            }

            boolean updated = employeeDAO.updateEmployee(
                    session.getOrganizationId(),
                    code,
                    firstName,
                    lastName,
                    email,
                    phone.isBlank() ? null : phone,
                    department,
                    designation,
                    salary
            );

            System.out.println(updated
                    ? "Employee updated successfully!"
                    : "Employee not found in your organization.");

        } catch (NumberFormatException e) {
            System.out.println("Enter a valid numeric salary.");
        } catch (SQLException e) {
            handleDatabaseError(e);
        }
    }

    private void deleteEmployee(
            Scanner scanner,
            OrganizationSession session) {

        String code = readRequired(
                scanner, "Employee code to delete: ");

        System.out.print(
                "Delete employee " + code + "? Type YES to confirm: ");

        if (!scanner.nextLine().trim().equals("YES")) {
            System.out.println("Delete cancelled.");
            return;
        }

        try {
            boolean deleted = employeeDAO.deleteEmployee(
                    session.getOrganizationId(), code);

            System.out.println(deleted
                    ? "Employee deleted successfully!"
                    : "Employee not found in your organization.");

        } catch (SQLException e) {
            handleDatabaseError(e);
        }
    }

    private String readRequired(
            Scanner scanner, String prompt) {

        System.out.print(prompt);
        String value = scanner.nextLine().trim();

        if (value.isBlank()) {
            throw new IllegalArgumentException(
                    "This field cannot be empty.");
        }

        return value;
    }

    private String readOptional(
            Scanner scanner, String prompt) {

        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    private void handleDatabaseError(SQLException e) {
        if ("23000".equals(e.getSQLState())) {
            System.out.println(
                    "Duplicate employee code/email or another "
                            + "database constraint was violated.");
        } else {
            System.out.println("Database operation failed.");
            System.err.println(e.getMessage());
        }
    }
}
