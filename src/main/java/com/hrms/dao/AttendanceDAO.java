package com.hrms.dao;

import com.hrms.config.DatabaseConnection;
import com.hrms.model.Attendance;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class AttendanceDAO {

    // 1. Create attendance / Check-in
    public boolean checkIn(
            long organizationId,
            long employeeId) throws SQLException {

        String sql = """
            INSERT INTO attendance
                (organization_id, employee_id,
                 attendance_date, check_in, status)
            SELECT e.organization_id, e.id,
                   ?, ?, 'PRESENT'
            FROM employees e
            WHERE e.id = ?
              AND e.organization_id = ?
              AND e.status = 'ACTIVE'
            """;

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setDate(1, Date.valueOf(LocalDate.now()));
            ps.setTime(2, Time.valueOf(LocalTime.now()));
            ps.setLong(3, employeeId);
            ps.setLong(4, organizationId);

            return ps.executeUpdate() == 1;
        }
    }

    // 2. Check-out
    public boolean checkOut(
            long organizationId,
            long employeeId) throws SQLException {

        String sql = """
            UPDATE attendance
            SET check_out = ?
            WHERE organization_id = ?
              AND employee_id = ?
              AND attendance_date = ?
              AND check_in IS NOT NULL
              AND check_out IS NULL
            """;

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setTime(1, Time.valueOf(LocalTime.now()));
            ps.setLong(2, organizationId);
            ps.setLong(3, employeeId);
            ps.setDate(4, Date.valueOf(LocalDate.now()));

            return ps.executeUpdate() == 1;
        }
    }

    // 3. View attendance for an organization
    public List<Attendance> getAll(
            long organizationId) throws SQLException {

        List<Attendance> records = new ArrayList<>();

        String sql = """
            SELECT id, organization_id, employee_id,
                   attendance_date, check_in, check_out,
                   status, remarks
            FROM attendance
            WHERE organization_id = ?
            ORDER BY attendance_date DESC, employee_id
            """;

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setLong(1, organizationId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Attendance a = new Attendance();

                    a.setId(rs.getLong("id"));
                    a.setOrganizationId(
                            rs.getLong("organization_id"));
                    a.setEmployeeId(
                            rs.getLong("employee_id"));
                    a.setAttendanceDate(
                            rs.getDate("attendance_date")
                                    .toLocalDate());

                    Time in = rs.getTime("check_in");
                    Time out = rs.getTime("check_out");

                    a.setCheckIn(
                            in == null ? null : in.toLocalTime());
                    a.setCheckOut(
                            out == null ? null : out.toLocalTime());

                    a.setStatus(rs.getString("status"));
                    a.setRemarks(rs.getString("remarks"));

                    records.add(a);
                }
            }
        }

        return records;
    }

    // 4. Update attendance status and remarks
    public boolean update(
            long organizationId,
            long attendanceId,
            String status,
            String remarks) throws SQLException {

        String sql = """
            UPDATE attendance
            SET status = ?, remarks = ?
            WHERE id = ? AND organization_id = ?
            """;

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, status);
            ps.setString(2, remarks);
            ps.setLong(3, attendanceId);
            ps.setLong(4, organizationId);

            return ps.executeUpdate() == 1;
        }
    }

    // 5. Delete attendance
    public boolean delete(
            long organizationId,
            long attendanceId) throws SQLException {

        String sql = """
            DELETE FROM attendance
            WHERE id = ? AND organization_id = ?
            """;

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setLong(1, attendanceId);
            ps.setLong(2, organizationId);

            return ps.executeUpdate() == 1;
        }
    }
}
