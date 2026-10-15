package com.hrms.dao;

import com.hrms.config.DatabaseConnection;
import com.hrms.model.Salary;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SalaryDAO {

    // CREATE salary
    public boolean createSalary(Salary salary)
            throws SQLException {

        String sql = """
            INSERT INTO salaries
            (organization_id, employee_id, pay_month,
             basic_salary, allowances, deductions,
             net_salary, payment_status)
            SELECT e.organization_id, e.id, ?, ?, ?, ?, ?, ?
            FROM employees e
            WHERE e.id = ?
              AND e.organization_id = ?
              AND e.status = 'ACTIVE'
            """;

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setDate(1, Date.valueOf(salary.getPayMonth()));
            ps.setBigDecimal(2, salary.getBasicSalary());
            ps.setBigDecimal(3, salary.getAllowances());
            ps.setBigDecimal(4, salary.getDeductions());
            ps.setBigDecimal(5, salary.getNetSalary());
            ps.setString(6, salary.getPaymentStatus());
            ps.setLong(7, salary.getEmployeeId());
            ps.setLong(8, salary.getOrganizationId());

            return ps.executeUpdate() == 1;
        }
    }

    // READ all salaries for an organization
    public List<Salary> getAllSalaries(long organizationId)
            throws SQLException {

        String sql = """
            SELECT id, organization_id, employee_id, pay_month,
                   basic_salary, allowances, deductions,
                   payment_status
            FROM salaries
            WHERE organization_id = ?
            ORDER BY pay_month DESC, employee_id
            """;

        List<Salary> salaries = new ArrayList<>();

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setLong(1, organizationId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Salary salary = new Salary(
                            rs.getLong("organization_id"),
                            rs.getLong("employee_id"),
                            rs.getDate("pay_month").toLocalDate(),
                            rs.getBigDecimal("basic_salary"),
                            rs.getBigDecimal("allowances"),
                            rs.getBigDecimal("deductions"),
                            rs.getString("payment_status")
                    );

                    salary.setId(rs.getLong("id"));
                    salaries.add(salary);
                }
            }
        }

        return salaries;
    }

    // UPDATE salary components and payment status
    public boolean updateSalary(
            long organizationId,
            long salaryId,
            java.math.BigDecimal basicSalary,
            java.math.BigDecimal allowances,
            java.math.BigDecimal deductions,
            String paymentStatus) throws SQLException {

        String sql = """
            UPDATE salaries
            SET basic_salary = ?,
                allowances = ?,
                deductions = ?,
                net_salary = ?,
                payment_status = ?
            WHERE id = ?
              AND organization_id = ?
            """;

        java.math.BigDecimal netSalary = basicSalary
                .add(allowances)
                .subtract(deductions);

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setBigDecimal(1, basicSalary);
            ps.setBigDecimal(2, allowances);
            ps.setBigDecimal(3, deductions);
            ps.setBigDecimal(4, netSalary);
            ps.setString(5, paymentStatus);
            ps.setLong(6, salaryId);
            ps.setLong(7, organizationId);

            return ps.executeUpdate() == 1;
        }
    }

    // DELETE salary
    public boolean deleteSalary(
            long organizationId,
            long salaryId) throws SQLException {

        String sql = """
            DELETE FROM salaries
            WHERE id = ?
              AND organization_id = ?
            """;

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setLong(1, salaryId);
            ps.setLong(2, organizationId);

            return ps.executeUpdate() == 1;
        }
    }
}
