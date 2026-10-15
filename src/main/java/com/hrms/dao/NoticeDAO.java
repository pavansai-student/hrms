package com.hrms.dao;

import com.hrms.config.DatabaseConnection;
import com.hrms.model.Notice;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class NoticeDAO {

    // CREATE
    public boolean createNotice(Notice notice)
            throws SQLException {

        String sql = """
            INSERT INTO notices
            (organization_id, title, description, notice_type,
             start_date, end_date)
            VALUES (?, ?, ?, ?, ?, ?)
            """;

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setLong(1, notice.getOrganizationId());
            ps.setString(2, notice.getTitle());
            ps.setString(3, notice.getDescription());
            ps.setString(4, notice.getNoticeType());
            ps.setDate(5, Date.valueOf(notice.getStartDate()));
            ps.setDate(6, Date.valueOf(notice.getEndDate()));

            return ps.executeUpdate() == 1;
        }
    }

    // READ
    public List<Notice> getAllNotices(long organizationId)
            throws SQLException {

        String sql = """
            SELECT id, organization_id, title, description,
                   notice_type, start_date, end_date
            FROM notices
            WHERE organization_id = ?
            ORDER BY start_date DESC, id DESC
            """;

        List<Notice> notices = new ArrayList<>();

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setLong(1, organizationId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Notice notice = new Notice(
                            rs.getLong("organization_id"),
                            rs.getString("title"),
                            rs.getString("description"),
                            rs.getString("notice_type"),
                            rs.getDate("start_date").toLocalDate(),
                            rs.getDate("end_date").toLocalDate()
                    );

                    notice.setId(rs.getLong("id"));
                    notices.add(notice);
                }
            }
        }

        return notices;
    }

    // UPDATE
    public boolean updateNotice(
            long organizationId,
            long noticeId,
            String title,
            String description,
            String noticeType,
            java.time.LocalDate startDate,
            java.time.LocalDate endDate)
            throws SQLException {

        String sql = """
            UPDATE notices
            SET title = ?,
                description = ?,
                notice_type = ?,
                start_date = ?,
                end_date = ?
            WHERE id = ?
              AND organization_id = ?
            """;

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, title);
            ps.setString(2, description);
            ps.setString(3, noticeType);
            ps.setDate(4, Date.valueOf(startDate));
            ps.setDate(5, Date.valueOf(endDate));
            ps.setLong(6, noticeId);
            ps.setLong(7, organizationId);

            return ps.executeUpdate() == 1;
        }
    }

    // DELETE
    public boolean deleteNotice(
            long organizationId,
            long noticeId) throws SQLException {

        String sql = """
            DELETE FROM notices
            WHERE id = ?
              AND organization_id = ?
            """;

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setLong(1, noticeId);
            ps.setLong(2, organizationId);

            return ps.executeUpdate() == 1;
        }
    }
}
