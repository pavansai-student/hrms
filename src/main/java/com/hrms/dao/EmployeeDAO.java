package com.hrms.dao;

import com.hrms.config.DatabaseConnection;
import com.hrms.model.Employee;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EmployeeDAO {

    // CREATE
    public boolean addEmployee(Employee employee)
            throws SQLException {

        String sql = """
                INSERT INTO employees
                (organization_id, employee_code, first_name,
                 last_name, email, phone, department,
                 designation, joining_date, salary)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;

        try (Connection con =
                     DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setLong(1, employee.getOrganizationId());
            ps.setString(2, employee.getEmployeeCode());
            ps.setString(3, employee.getFirstName());
            ps.setString(4, employee.getLastName());
            ps.setString(5, employee.getEmail());
            ps.setString(6, employee.getPhone());
            ps.setString(7, employee.getDepartment());
            ps.setString(8, employee.getDesignation());
            ps.setDate(9, Date.valueOf(employee.getJoiningDate()));
            ps.setBigDecimal(10, employee.getSalary());

            return ps.executeUpdate() == 1;
        }
    }

    // READ
    public List<Employee> getEmployees(long organizationId)
            throws SQLException {

        String sql = """
                SELECT id, employee_code, first_name, last_name,
                       email, phone, department, designation,
                       joining_date, salary
                FROM employees
                WHERE organization_id = ?
                ORDER BY id
                """;

        List<Employee> employees = new ArrayList<>();

        try (Connection con =
                     DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setLong(1, organizationId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Employee employee = new Employee(
                            organizationId,
                            rs.getString("employee_code"),
                            rs.getString("first_name"),
                            rs.getString("last_name"),
                            rs.getString("email"),
                            rs.getString("phone"),
                            rs.getString("department"),
                            rs.getString("designation"),
                            rs.getDate("joining_date").toLocalDate(),
                            rs.getBigDecimal("salary")
                    );

                    employees.add(employee);
                }
            }
        }

        return employees;
    }

    // UPDATE
    public boolean updateEmployee(
            long organizationId,
            String employeeCode,
            String firstName,
            String lastName,
            String email,
            String phone,
            String department,
            String designation,
            java.math.BigDecimal salary)
            throws SQLException {

        String sql = """
                UPDATE employees
                SET first_name = ?,
                    last_name = ?,
                    email = ?,
                    phone = ?,
                    department = ?,
                    designation = ?,
                    salary = ?
                WHERE organization_id = ?
                  AND employee_code = ?
                """;

        try (Connection con =
                     DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, firstName);
            ps.setString(2, lastName);
            ps.setString(3, email);
            ps.setString(4, phone);
            ps.setString(5, department);
            ps.setString(6, designation);
            ps.setBigDecimal(7, salary);
            ps.setLong(8, organizationId);
            ps.setString(9, employeeCode);

            return ps.executeUpdate() == 1;
        }
    }

    // DELETE
    public boolean deleteEmployee(
            long organizationId,
            String employeeCode) throws SQLException {

        String sql = """
                DELETE FROM employees
                WHERE organization_id = ?
                  AND employee_code = ?
                """;

        try (Connection con =
                     DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setLong(1, organizationId);
            ps.setString(2, employeeCode);

            return ps.executeUpdate() == 1;
        }
    }
}
