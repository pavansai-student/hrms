package com.hrms.service;

import com.hrms.dao.AttendanceDAO;
import com.hrms.dao.LoginDAO.OrganizationSession;
import com.hrms.model.Attendance;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class AttendanceService {

    private final AttendanceDAO attendanceDAO =
            new AttendanceDAO();

    public void showMenu(
            Scanner scanner,
            OrganizationSession session) {

        boolean running = true;

        while (running) {
            System.out.println("\n===== ATTENDANCE MANAGEMENT =====");
            System.out.println("1. Check In");
            System.out.println("2. Check Out");
            System.out.println("3. View Attendance");
            System.out.println("4. Update Attendance");
            System.out.println("5. Delete Attendance");
            System.out.println("6. Back to Dashboard");
            System.out.print("Enter your choice: ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1" -> checkIn(scanner, session);
                case "2" -> checkOut(scanner, session);
                case "3" -> viewAttendance(session);
                case "4" -> updateAttendance(scanner, session);
                case "5" -> deleteAttendance(scanner, session);
                case "6" -> running = false;
                default -> System.out.println(
                        "Invalid choice. Enter 1 to 6.");
            }
        }
    }

    private void checkIn(
            Scanner scanner,
            OrganizationSession session) {

        try {
            long employeeId = readPositiveId(
                    scanner, "Enter employee ID: ");

            boolean success = attendanceDAO.checkIn(
                    session.getOrganizationId(), employeeId);

            System.out.println(success
                    ? "Employee checked in successfully!"
                    : "Check-in failed. Verify the employee ID, "
                    + "organization, and employee status.");

        } catch (NumberFormatException e) {
            System.out.println("Enter a valid numeric employee ID.");
        } catch (SQLException e) {
            handleDatabaseError(e);
        }
    }

    private void checkOut(
            Scanner scanner,
            OrganizationSession session) {

        try {
            long employeeId = readPositiveId(
                    scanner, "Enter employee ID: ");

            boolean success = attendanceDAO.checkOut(
                    session.getOrganizationId(), employeeId);

            System.out.println(success
                    ? "Employee checked out successfully!"
                    : "Check-out failed. No open check-in was found "
                    + "for this employee today.");

        } catch (NumberFormatException e) {
            System.out.println("Enter a valid numeric employee ID.");
        } catch (SQLException e) {
            handleDatabaseError(e);
        }
    }

    private void viewAttendance(OrganizationSession session) {

        try {
            List<Attendance> records = attendanceDAO.getAll(
                    session.getOrganizationId());

            System.out.println("\n===== ATTENDANCE RECORDS =====");

            if (records.isEmpty()) {
                System.out.println("No attendance records found.");
                return;
            }

            for (Attendance a : records) {
                System.out.println("------------------------------");
                System.out.println("Attendance ID: " + a.getId());
                System.out.println("Employee ID: " + a.getEmployeeId());
                System.out.println("Date: " + a.getAttendanceDate());
                System.out.println("Check-in: " + a.getCheckIn());
                System.out.println("Check-out: " + a.getCheckOut());
                System.out.println("Status: " + a.getStatus());
                System.out.println("Remarks: " + a.getRemarks());
            }

        } catch (SQLException e) {
            handleDatabaseError(e);
        }
    }

    private void updateAttendance(
            Scanner scanner,
            OrganizationSession session) {

        try {
            long attendanceId = readPositiveId(
                    scanner, "Enter attendance ID to update: ");

            System.out.print(
                    "Status (PRESENT/ABSENT/LEAVE/HALF_DAY): ");
            String status = scanner.nextLine()
                    .trim().toUpperCase();

            if (!List.of(
                    "PRESENT", "ABSENT", "LEAVE", "HALF_DAY"
            ).contains(status)) {
                System.out.println("Invalid attendance status.");
                return;
            }

            System.out.print("Remarks (optional): ");
            String remarks = scanner.nextLine().trim();

            boolean success = attendanceDAO.update(
                    session.getOrganizationId(),
                    attendanceId,
                    status,
                    remarks.isBlank() ? null : remarks);

            System.out.println(success
                    ? "Attendance updated successfully!"
                    : "Attendance record not found.");

        } catch (NumberFormatException e) {
            System.out.println("Enter a valid attendance ID.");
        } catch (SQLException e) {
            handleDatabaseError(e);
        }
    }

    private void deleteAttendance(
            Scanner scanner,
            OrganizationSession session) {

        try {
            long attendanceId = readPositiveId(
                    scanner, "Enter attendance ID to delete: ");

            System.out.print(
                    "Type YES to confirm deletion: ");

            if (!scanner.nextLine().trim().equals("YES")) {
                System.out.println("Deletion cancelled.");
                return;
            }

            boolean success = attendanceDAO.delete(
                    session.getOrganizationId(), attendanceId);

            System.out.println(success
                    ? "Attendance deleted successfully!"
                    : "Attendance record not found.");

        } catch (NumberFormatException e) {
            System.out.println("Enter a valid attendance ID.");
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

    private void handleDatabaseError(SQLException e) {
        if ("23000".equals(e.getSQLState())) {
            System.out.println(
                    "Database constraint error. Attendance may "
                            + "already exist for this employee today.");
        } else {
            System.out.println("Attendance database operation failed.");
            System.err.println(e.getMessage());
        }
    }
}
