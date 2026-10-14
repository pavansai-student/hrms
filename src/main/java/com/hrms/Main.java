//////package com.hrms;
//////
//////import java.sql.Connection;
//////import java.sql.DriverManager;
//////import java.sql.ResultSet;
//////import java.sql.Statement;
//////
//////public class Main {
//////
//////    public static void main(String[] args) {
//////
//////        String url = "jdbc:mysql://localhost:3306/hrms_db";
//////        String username = "root";
//////
//////        // Read your MySQL password from an environment variable.
//////        String password = System.getenv("HRMS_DB_PASSWORD");
//////
//////        if (password == null || password.isBlank()) {
//////            System.out.println(
//////                    "ERROR: HRMS_DB_PASSWORD is not configured."
//////            );
//////            return;
//////        }
//////
//////        try (
//////                Connection connection = DriverManager.getConnection(
//////                        url, username, password
//////                );
//////
//////                Statement statement = connection.createStatement();
//////
//////                ResultSet resultSet =
//////                        statement.executeQuery("SELECT DATABASE()")
//////        ) {
//////            if (resultSet.next()) {
//////                System.out.println(
//////                        "MySQL connection successful!"
//////                );
//////                System.out.println(
//////                        "Connected database: "
//////                                + resultSet.getString(1)
//////                );
//////            }
//////
//////        } catch (Exception e) {
//////            System.out.println("MySQL connection failed.");
//////            System.out.println("Reason: " + e.getMessage());
//////        }
//////    }
//////}
////package com.hrms;
////
////import java.util.Scanner;
////
////public class Main {
////
////    private static final Scanner scanner = new Scanner(System.in);
////
////    public static void main(String[] args) {
////
////        boolean running = true;
////
////        while (running) {
////            System.out.println("\n===== HRMS SYSTEM =====");
////            System.out.println("1. Create New Organization");
////            System.out.println("2. Login to Organization");
////            System.out.println("3. Exit");
////            System.out.print("Enter your choice: ");
////
////            String choice = scanner.nextLine().trim();
////
////            switch (choice) {
////                case "1":
////                    System.out.println(
////                            "Organization registration will be implemented next."
////                    );
////                    break;
////
////                case "2":
////                    System.out.println(
////                            "Organization login will be implemented next."
////                    );
////                    break;
////
////                case "3":
////                    running = false;
////                    System.out.println("Thank you for using HRMS!");
////                    break;
////
////                default:
////                    System.out.println(
////                            "Invalid choice. Please enter 1, 2, or 3."
////                    );
////            }
////        }
////
////        scanner.close();
////    }
////}
//package com.hrms;
//
//import com.hrms.service.OrganizationService;
//
//import java.util.Scanner;
//
//public class Main {
//
//    public static void main(String[] args) {
//
//        Scanner scanner = new Scanner(System.in);
//        OrganizationService organizationService =
//                new OrganizationService();
//
//        boolean running = true;
//
//        while (running) {
//            System.out.println("\n===== HRMS SYSTEM =====");
//            System.out.println("1. Create New Organization");
//            System.out.println("2. Login to Organization");
//            System.out.println("3. Exit");
//            System.out.print("Enter your choice: ");
//
//            String choice = scanner.nextLine().trim();
//
//            switch (choice) {
//                case "1":
//                    organizationService.registerOrganization(
//                            scanner);
//                    break;
//
//                case "2":
//                    System.out.println(
//                            "Login functionality is the next step.");
//                    break;
//
//                case "3":
//                    running = false;
//                    System.out.println(
//                            "Thank you for using HRMS!");
//                    break;
//
//                default:
//                    System.out.println(
//                            "Invalid choice. Please enter 1, 2, or 3.");
//            }
//        }
//
//        scanner.close();
//    }
//}
package com.hrms;

import com.hrms.service.LoginService;
import com.hrms.service.OrganizationService;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        try (Scanner scanner = new Scanner(System.in)) {

            OrganizationService organizationService =
                    new OrganizationService();

            LoginService loginService = new LoginService();

            boolean running = true;

            while (running) {
                System.out.println("\n===== HRMS SYSTEM =====");
                System.out.println("1. Create New Organization");
                System.out.println("2. Login to Organization");
                System.out.println("3. Exit");
                System.out.print("Enter your choice: ");

                String choice = scanner.nextLine().trim();

                switch (choice) {
                    case "1":
                        organizationService.registerOrganization(
                                scanner);
                        break;

                    case "2":
                        running =
                                loginService.showLoginMenu(scanner);
                        break;

                    case "3":
                        running = false;
                        System.out.println(
                                "Thank you for using HRMS!");
                        break;

                    default:
                        System.out.println(
                                "Invalid choice. Enter 1, 2, or 3.");
                }
            }
        }
    }
}