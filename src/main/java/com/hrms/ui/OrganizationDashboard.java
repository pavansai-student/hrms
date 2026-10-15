package com.hrms.ui;
import com.hrms.service.NoticeService;
import com.hrms.service.SalaryService;
import com.hrms.service.EmployeeService;
import com.hrms.dao.LoginDAO.OrganizationSession;
import com.hrms.service.AttendanceService;
import java.util.Scanner;

public class OrganizationDashboard {

    private final EmployeeService employeeService =new EmployeeService();
    private final AttendanceService attendanceService =new AttendanceService();
    private final SalaryService salaryService =new SalaryService();
    private final NoticeService noticeService = new NoticeService();

    public void show( Scanner scanner, OrganizationSession session) {

        boolean loggedIn = true;

        while (loggedIn) {
            System.out.println( "\n===== WELCOME TO " + session.getOrganizationName().toUpperCase() + " =====");

            System.out.println("1. Employee Management");
            System.out.println("2. Attendance Management");
            System.out.println("3. Salary Management");
            System.out.println("4. Notice / Holiday Management");
            System.out.println("5. Logout");
            System.out.print("Enter your choice: ");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    employeeService.showMenu(scanner, session);
                    break;


                case "2":
                    attendanceService.showMenu(scanner, session);
                    break;

                case "3":
                    salaryService.showMenu(scanner, session);
                    break;

                case "4":
                    noticeService.showMenu(scanner, session);
                    break;

                case "5":
                    loggedIn = false;
                    System.out.println("Logged out successfully.");
                    break;

                default:
                    System.out.println(
                            "Invalid choice. Enter 1 to 5.");
            }
        }
    }
}
