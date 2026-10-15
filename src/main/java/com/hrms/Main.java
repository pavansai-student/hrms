
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
