package com.hrms.service;

import com.hrms.dao.LoginDAO.OrganizationSession;
import com.hrms.dao.NoticeDAO;
import com.hrms.model.Notice;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

public class NoticeService {

    private final NoticeDAO noticeDAO = new NoticeDAO();

    public void showMenu(
            Scanner scanner,
            OrganizationSession session) {

        boolean running = true;

        while (running) {
            System.out.println("\n===== NOTICE / HOLIDAY MANAGEMENT =====");
            System.out.println("1. Create Notice / Holiday");
            System.out.println("2. View Notices / Holidays");
            System.out.println("3. Update Notice / Holiday");
            System.out.println("4. Delete Notice / Holiday");
            System.out.println("5. Back to Dashboard");
            System.out.print("Enter your choice: ");

            switch (scanner.nextLine().trim()) {
                case "1" -> createNotice(scanner, session);
                case "2" -> viewNotices(session);
                case "3" -> updateNotice(scanner, session);
                case "4" -> deleteNotice(scanner, session);
                case "5" -> running = false;
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    private void createNotice(
            Scanner scanner,
            OrganizationSession session) {

        try {
            System.out.print("Title: ");
            String title = scanner.nextLine().trim();

            System.out.print("Description: ");
            String description = scanner.nextLine().trim();

            System.out.print("Type (NOTICE/HOLIDAY): ");
            String type = scanner.nextLine().trim().toUpperCase();

            LocalDate start = readDate(
                    scanner, "Start date (YYYY-MM-DD): ");
            LocalDate end = readDate(
                    scanner, "End date (YYYY-MM-DD): ");

            if (title.isBlank() || description.isBlank()) {
                System.out.println(
                        "Title and description are required.");
                return;
            }

            if (title.length() > 150 || description.length() > 1000) {
                System.out.println("Title or description is too long.");
                return;
            }

            if (!validType(type)) {
                System.out.println(
                        "Type must be NOTICE or HOLIDAY.");
                return;
            }

            if (end.isBefore(start)) {
                System.out.println(
                        "End date cannot be before start date.");
                return;
            }

            Notice notice = new Notice(
                    session.getOrganizationId(),
                    title, description, type, start, end);

            if (noticeDAO.createNotice(notice)) {
                System.out.println(
                        "Notice / Holiday created successfully!");
            } else {
                System.out.println("Record was not created.");
            }

        } catch (DateTimeParseException e) {
            System.out.println("Invalid date. Use YYYY-MM-DD.");
        } catch (SQLException e) {
            handleDatabaseError(e);
        }
    }

    private void viewNotices(OrganizationSession session) {
        try {
            List<Notice> notices = noticeDAO.getAllNotices(
                    session.getOrganizationId());

            System.out.println("\n===== NOTICES / HOLIDAYS =====");

            if (notices.isEmpty()) {
                System.out.println("No records found.");
                return;
            }

            for (Notice n : notices) {
                System.out.println("------------------------------");
                System.out.println("ID: " + n.getId());
                System.out.println("Title: " + n.getTitle());
                System.out.println("Description: " + n.getDescription());
                System.out.println("Type: " + n.getNoticeType());
                System.out.println("Start: " + n.getStartDate());
                System.out.println("End: " + n.getEndDate());
            }

        } catch (SQLException e) {
            handleDatabaseError(e);
        }
    }

    private void updateNotice(
            Scanner scanner,
            OrganizationSession session) {

        try {
            long id = readId(scanner, "Record ID to update: ");

            System.out.print("New title: ");
            String title = scanner.nextLine().trim();

            System.out.print("New description: ");
            String description = scanner.nextLine().trim();

            System.out.print("New type (NOTICE/HOLIDAY): ");
            String type = scanner.nextLine().trim().toUpperCase();

            LocalDate start = readDate(
                    scanner, "New start date (YYYY-MM-DD): ");
            LocalDate end = readDate(
                    scanner, "New end date (YYYY-MM-DD): ");

            if (title.isBlank() || description.isBlank()
                    || title.length() > 150
                    || description.length() > 1000) {
                System.out.println(
                        "Enter a valid title and description.");
                return;
            }

            if (!validType(type)) {
                System.out.println("Invalid type.");
                return;
            }

            if (end.isBefore(start)) {
                System.out.println(
                        "End date cannot be before start date.");
                return;
            }

            boolean updated = noticeDAO.updateNotice(
                    session.getOrganizationId(),
                    id, title, description, type, start, end);

            System.out.println(updated
                    ? "Record updated successfully."
                    : "Record not found.");

        } catch (NumberFormatException e) {
            System.out.println("Enter a valid positive record ID.");
        } catch (DateTimeParseException e) {
            System.out.println("Invalid date. Use YYYY-MM-DD.");
        } catch (SQLException e) {
            handleDatabaseError(e);
        }
    }

    private void deleteNotice(
            Scanner scanner,
            OrganizationSession session) {

        try {
            long id = readId(scanner, "Record ID to delete: ");

            System.out.print("Type YES to confirm deletion: ");

            if (!scanner.nextLine().trim().equals("YES")) {
                System.out.println("Deletion cancelled.");
                return;
            }

            boolean deleted = noticeDAO.deleteNotice(
                    session.getOrganizationId(), id);

            System.out.println(deleted
                    ? "Record deleted successfully."
                    : "Record not found.");

        } catch (NumberFormatException e) {
            System.out.println("Enter a valid positive record ID.");
        } catch (SQLException e) {
            handleDatabaseError(e);
        }
    }

    private LocalDate readDate(
            Scanner scanner, String prompt) {

        System.out.print(prompt);
        return LocalDate.parse(scanner.nextLine().trim());
    }

    private long readId(Scanner scanner, String prompt) {
        System.out.print(prompt);
        long id = Long.parseLong(scanner.nextLine().trim());

        if (id <= 0) {
            throw new NumberFormatException(
                    "ID must be positive.");
        }

        return id;
    }

    private boolean validType(String type) {
        return type.equals("NOTICE") || type.equals("HOLIDAY");
    }

    private void handleDatabaseError(SQLException e) {
        System.out.println("Notice / Holiday database operation failed.");
        System.err.println(e.getMessage());
    }
}
