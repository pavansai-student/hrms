package com.hrms.dao;

import com.hrms.config.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginDAO {

    public OrganizationSession login(
            String email, String password) throws SQLException {

        String sql = """
                SELECT u.id AS user_id,
                       u.organization_id,
                       u.email,
                       u.role_id,
                       o.name AS organization_name
                FROM users u
                JOIN organizations o
                  ON o.id = u.organization_id
                WHERE u.email = ?
                  AND u.password = ?
                  AND u.status = 'ACTIVE'
                  AND o.status = 'ACTIVE'
                """;

        try (Connection connection =DatabaseConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, email);
            ps.setString(2, password);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new OrganizationSession(
                            rs.getLong("user_id"),
                            rs.getLong("organization_id"),
                            rs.getString("email"),
                            rs.getString("organization_name"),
                            rs.getInt("role_id")
                    );
                }
            }
        }

        return null;
    }

    public static class OrganizationSession {

        private final long userId;
        private final long organizationId;
        private final String email;
        private final String organizationName;
        private final int roleId;

        public OrganizationSession(
                long userId,
                long organizationId,
                String email,
                String organizationName,
                int roleId) {

            this.userId = userId;
            this.organizationId = organizationId;
            this.email = email;
            this.organizationName = organizationName;
            this.roleId = roleId;
        }

        public long getUserId() {
            return userId;
        }

        public long getOrganizationId() {
            return organizationId;
        }

        public String getEmail() {
            return email;
        }

        public String getOrganizationName() {
            return organizationName;
        }

        public int getRoleId() {
            return roleId;
        }
    }
}
