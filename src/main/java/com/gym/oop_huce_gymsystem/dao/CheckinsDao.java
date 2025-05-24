package com.gym.oop_huce_gymsystem.dao;

import com.gym.oop_huce_gymsystem.model.Checkins;
import com.gym.oop_huce_gymsystem.util.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CheckinsDao {
    // Thêm một bản ghi check-in mới
    public void addCheckin(int memberId) throws SQLException {
        String query = "INSERT INTO checkins (member_id, checkin_time) VALUES (?, NOW())";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, memberId);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Đã thêm bản ghi check-in thành công cho member_id: " + memberId);
            } else {
                throw new SQLException("Không thể thêm bản ghi check-in: Không có hàng nào được thêm.");
            }
        } catch (SQLException e) {
            throw new SQLException("Lỗi khi thêm bản ghi check-in: " + e.getMessage(), e);
        }
    }

    // Lấy danh sách tất cả check-in của một hội viên theo member_id
    public List<Checkins> getCheckinsByMemberId(int memberId) throws SQLException {
        List<Checkins> checkins = new ArrayList<>();
        String query = "SELECT checkin_id, member_id, checkin_time FROM checkins WHERE member_id = ? ORDER BY checkin_time DESC";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, memberId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Checkins checkin = new Checkins(
                            rs.getInt("checkin_id"),
                            rs.getInt("member_id"),
                            rs.getTimestamp("checkin_time").toLocalDateTime()
                    );
                    checkins.add(checkin);
                    System.out.println("Lấy check-in từ DB: checkin_id = " + checkin.getCheckinId() + ", member_id = " + checkin.getMemberId());
                }
            }
        } catch (SQLException e) {
            throw new SQLException("Lỗi khi lấy danh sách check-in: " + e.getMessage(), e);
        }
        return checkins;
    }

    // Lấy danh sách check-in của một hội viên theo member_id và ngày
    public List<Checkins> getCheckinsByDate(int memberId, String date) throws SQLException {
        List<Checkins> checkins = new ArrayList<>();
        String query = "SELECT checkin_id, member_id, checkin_time FROM checkins WHERE member_id = ? AND DATE(checkin_time) = ? ORDER BY checkin_time DESC";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, memberId);
            stmt.setString(2, date);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Checkins checkin = new Checkins(
                            rs.getInt("checkin_id"),
                            rs.getInt("member_id"),
                            rs.getTimestamp("checkin_time").toLocalDateTime()
                    );
                    checkins.add(checkin);
                    System.out.println("Lấy check-in từ DB: checkin_id = " + checkin.getCheckinId() + ", member_id = " + checkin.getMemberId() + ", date = " + date);
                }
            }
        } catch (SQLException e) {
            throw new SQLException("Lỗi khi lấy danh sách check-in theo ngày: " + e.getMessage(), e);
        }
        return checkins;
    }

    // Lấy thông tin một bản ghi check-in dựa trên checkin_id
    public Checkins getCheckinById(int checkinId) throws SQLException {
        String query = "SELECT checkin_id, member_id, checkin_time FROM checkins WHERE checkin_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, checkinId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Checkins checkin = new Checkins(
                            rs.getInt("checkin_id"),
                            rs.getInt("member_id"),
                            rs.getTimestamp("checkin_time").toLocalDateTime()
                    );
                    System.out.println("Lấy check-in từ DB: checkin_id = " + checkin.getCheckinId());
                    return checkin;
                } else {
                    throw new SQLException("Không tìm thấy bản ghi check-in với ID: " + checkinId);
                }
            }
        } catch (SQLException e) {
            throw new SQLException("Lỗi khi lấy bản ghi check-in: " + e.getMessage(), e);
        }
    }

    // Xóa một bản ghi check-in dựa trên checkin_id
    public void deleteCheckin(int checkinId) throws SQLException {
        String query = "DELETE FROM checkins WHERE checkin_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, checkinId);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Đã xóa bản ghi check-in: " + checkinId);
            } else {
                throw new SQLException("Không tìm thấy bản ghi check-in để xóa: " + checkinId);
            }
        } catch (SQLException e) {
            throw new SQLException("Lỗi khi xóa bản ghi check-in: " + e.getMessage(), e);
        }
    }

    // Kiểm tra thẻ tập còn hiệu lực
    public boolean isMembershipCardValid(int memberId) throws SQLException {
        String query = "SELECT mc.expiry_date FROM membership_cards mc " +
                "JOIN members m ON m.card_code = mc.card_id " +
                "WHERE m.member_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, memberId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    LocalDate expiryDate = rs.getDate("expiry_date").toLocalDate();
                    LocalDate today = LocalDate.now();
                    return !expiryDate.isBefore(today);
                }
            }
        }
        return false; // Không tìm thấy thẻ tập hoặc hội viên
    }
}