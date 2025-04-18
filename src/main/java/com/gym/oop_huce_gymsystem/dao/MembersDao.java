package com.gym.oop_huce_gymsystem.dao;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import com.gym.oop_huce_gymsystem.model.*;
import com.gym.oop_huce_gymsystem.util.*;

public class MembersDao {

    //Hàm tạo thêm thành viên
    public void addMember(Members member) throws SQLException {
        String query = "INSERT INTO members (name, phone, membership_type, registration_date) " +
                "VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection(); //Kiểm tra kết nối
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, member.getName());
            stmt.setString(2, member.getPhone());
            stmt.setString(3, member.getMembershipType());
            stmt.setDate(4, java.sql.Date.valueOf(member.getRegistrationDate()));
            stmt.executeUpdate();
        } catch (SQLException e) {
            if (e.getErrorCode() == 2627) { // Lỗi vi phạm UNIQUE (phone)
                throw new SQLException("Số điện thoại đã tồn tại trong hệ thống.");
            }
            throw e;
        }
    }

    //Hàm chỉnh sửa thành viên
    public void updateMember(Members member) throws SQLException {
        String query = "UPDATE members SET name = ?, phone = ?, membership_type = ?, " +
                "registration_date = ? WHERE member_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, member.getName());
            stmt.setString(2, member.getPhone());
            stmt.setString(3, member.getMembershipType());
            stmt.setDate(4, java.sql.Date.valueOf(member.getRegistrationDate()));
            stmt.setInt(5, member.getMemberId());
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("Không tìm thấy hội viên với ID: " + member.getMemberId());
            }
        } catch (SQLException e) {
            if (e.getErrorCode() == 2627) { // Lỗi vi phạm UNIQUE (phone)
                throw new SQLException("Số điện thoại đã tồn tại trong hệ thống.");
            }
            throw e;
        }
    }

    //Hàm xóa hội viên
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

    // Lấy tất cả hội viên
    public List<Members> getAllMembers() throws SQLException {
        List<Members> members = new ArrayList<>();
        String query = "SELECT * FROM members";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                Members member = new Members(
                        rs.getInt("member_id"),
                        rs.getString("name"),
                        rs.getString("phone"),
                        rs.getString("membership_type"),
                        rs.getDate("registration_date").toLocalDate(),
                        rs.getTimestamp("created_at") != null ?
                                rs.getTimestamp("created_at").toLocalDateTime() : null
                );
                members.add(member);
            }
        }
        return members;
    }

    //Hàm lấy chi tiết 1 thành viên
    public Members getMemberById(int memberId) throws SQLException {
        String query = "SELECT * FROM members WHERE member_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, memberId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Members(
                            rs.getInt("member_id"),
                            rs.getString("name"),
                            rs.getString("phone"),
                            rs.getString("membership_type"),
                            rs.getDate("registration_date").toLocalDate(),
                            rs.getTimestamp("created_at") != null ?
                                    rs.getTimestamp("created_at").toLocalDateTime() : null
                    );
                } else {
                    throw new SQLException("Không tìm thấy hội viên với ID: " + memberId);
                }
            }
        }
    }

    //Hàm tìm kiếm hội viên theo id


}



