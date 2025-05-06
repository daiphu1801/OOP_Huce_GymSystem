package com.gym.oop_huce_gymsystem.dao;

import com.gym.oop_huce_gymsystem.model.Members;
import com.gym.oop_huce_gymsystem.util.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MembersDao {

    // Kiểm tra số điện thoại đã tồn tại trong cơ sở dữ liệu
    public boolean isPhoneExists(String phone) throws SQLException {
        String query = "SELECT COUNT(*) FROM members WHERE phone = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, phone);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }

    // Thêm một thành viên mới vào cơ sở dữ liệu
    public void addMember(Members member) throws SQLException {
        String query = "INSERT INTO members (card_code, full_name, phone, gender, email) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, member.getCardCode());
            stmt.setString(2, member.getFullName());
            stmt.setString(3, member.getPhone());
            stmt.setString(4, member.getGender());
            stmt.setString(5, member.getEmail());
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Đã thêm hội viên thành công: " + member.getPhone());
            } else {
                throw new SQLException("Không thể thêm hội viên: Không có hàng nào được thêm.");
            }
        } catch (SQLException e) {
            if (e.getErrorCode() == 2627) {
                throw new SQLException("Số điện thoại hoặc mã thẻ đã tồn tại trong hệ thống.");
            }
            throw e;
        }
    }

    // Cập nhật thông tin một thành viên trong cơ sở dữ liệu
    public void updateMember(Members member) throws SQLException {
        String query = "UPDATE members SET card_code = ?, full_name = ?, phone = ?, gender = ?, email = ? WHERE member_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, member.getCardCode());
            stmt.setString(2, member.getFullName());
            stmt.setString(3, member.getPhone());
            stmt.setString(4, member.getGender());
            stmt.setString(5, member.getEmail());
            stmt.setInt(6, member.getMemberId());
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("Không tìm thấy hội viên với ID: " + member.getMemberId());
            }
        } catch (SQLException e) {
            if (e.getErrorCode() == 2627) {
                throw new SQLException("Số điện thoại hoặc mã thẻ đã tồn tại trong hệ thống.");
            }
            throw e;
        }
    }

    // Xóa một thành viên khỏi cơ sở dữ liệu dựa trên ID
    public void deleteMember(int memberId) throws SQLException {
        String query = "DELETE FROM members WHERE member_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, memberId);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("Không tìm thấy hội viên với ID: " + memberId);
            }
        }
    }

    // Lấy danh sách tất cả thành viên từ cơ sở dữ liệu
    public List<Members> getAllMembers() throws SQLException {
        List<Members> members = new ArrayList<>();
        String query = "SELECT * FROM members";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Members member = new Members(
                        rs.getInt("member_id"),
                        rs.getString("card_code"),
                        rs.getString("full_name"),
                        rs.getString("phone"),
                        rs.getString("gender"),
                        rs.getString("email")
                );
                members.add(member);
                System.out.println("Lấy hội viên từ DB: " + member.getMemberId() + ", " + member.getFullName());
            }
        }
        return members;
    }

    // Lấy thông tin một thành viên dựa trên ID
    public Members getMemberById(int memberId) throws SQLException {
        String query = "SELECT * FROM members WHERE member_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, memberId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Members(
                            rs.getInt("member_id"),
                            rs.getString("card_code"),
                            rs.getString("full_name"),
                            rs.getString("phone"),
                            rs.getString("gender"),
                            rs.getString("email")
                    );
                } else {
                    throw new SQLException("Không tìm thấy hội viên với ID: " + memberId);
                }
            }
        }
    }

    // Lấy thông tin một thành viên dựa trên mã thẻ
    public Members getMemberByCardCode(String cardCode) throws SQLException {
        String query = "SELECT * FROM members WHERE card_code = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, cardCode);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Members(
                            rs.getInt("member_id"),
                            rs.getString("card_code"),
                            rs.getString("full_name"),
                            rs.getString("phone"),
                            rs.getString("gender"),
                            rs.getString("email")
                    );
                } else {
                    throw new SQLException("Không tìm thấy hội viên với mã thẻ: " + cardCode);
                }
            }
        }
    }
}