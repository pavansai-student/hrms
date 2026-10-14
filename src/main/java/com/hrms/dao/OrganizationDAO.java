package com.hrms.dao;

import com.hrms.config.DatabaseConnection;
import com.hrms.model.Organization;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class OrganizationDAO {

    public long registerOrganization(
            Organization organization,
            String password) throws SQLException {

        String organizationSql = """
                INSERT INTO organizations
                (name, address, pan, gst, email,
                 employee_count, department)
                VALUES (?, ?, ?, ?, ?, ?, ?)
                """;

        String roleSql =
                "SELECT id FROM roles WHERE name = ?";

        String userSql = """
                INSERT INTO users
                (organization_id, role_id, email, password)
                VALUES (?, ?, ?, ?)
                """;

        try (Connection connection =
                     DatabaseConnection.getConnection()) {

            connection.setAutoCommit(false);

            try {
                long organizationId;

                // 1. Insert organization.
                try (PreparedStatement ps =
                             connection.prepareStatement(
                                     organizationSql,
                                     Statement.RETURN_GENERATED_KEYS)) {

                    ps.setString(1, organization.getName());
                    ps.setString(2, organization.getAddress());
                    ps.setString(3, organization.getPan());

                    if (organization.getGst() == null
                            || organization.getGst().isBlank()) {
                        ps.setNull(4, java.sql.Types.VARCHAR);
                    } else {
                        ps.setString(4, organization.getGst());
                    }

                    ps.setString(5, organization.getEmail());
                    ps.setInt(6, organization.getEmployeeCount());
                    ps.setString(7, organization.getDepartment());

                    if (ps.executeUpdate() != 1) {
                        throw new SQLException(
                                "Organization could not be created.");
                    }

                    try (ResultSet keys = ps.getGeneratedKeys()) {
                        if (!keys.next()) {
                            throw new SQLException(
                                    "Organization ID was not generated.");
                        }

                        organizationId = keys.getLong(1);
                    }
                }

                // 2. Find the ADMIN role.
                int adminRoleId;

                try (PreparedStatement ps =
                             connection.prepareStatement(roleSql)) {

                    ps.setString(1, "ADMIN");

                    try (ResultSet rs = ps.executeQuery()) {
                        if (!rs.next()) {
                            throw new SQLException(
                                    "ADMIN role is missing. "
                                            + "Insert it into the roles table.");
                        }

                        adminRoleId = rs.getInt("id");
                    }
                }

                // 3. Create the organization login account.
                try (PreparedStatement ps =
                             connection.prepareStatement(userSql)) {

                    ps.setLong(1, organizationId);
                    ps.setInt(2, adminRoleId);
                    ps.setString(3, organization.getEmail());
                    ps.setString(4, password);

                    if (ps.executeUpdate() != 1) {
                        throw new SQLException(
                                "Administrator account could not be created.");
                    }
                }

                connection.commit();
                return organizationId;

            } catch (SQLException | RuntimeException e) {
                try {
                    connection.rollback();
                } catch (SQLException rollbackError) {
                    e.addSuppressed(rollbackError);
                }

                throw e;

            } finally {
                try {
                    connection.setAutoCommit(true);
                } catch (SQLException ignored) {
                    // Closing the connection will release this resource.
                }
            }
        }
    }
}
