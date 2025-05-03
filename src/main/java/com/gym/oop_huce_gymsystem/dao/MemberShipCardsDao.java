//package com.gym.oop_huce_gymsystem.dao;
//
//import java.sql.*;
//import java.time.LocalDateTime;
//import java.util.ArrayList;
//import java.util.List;
//import com.gym.oop_huce_gymsystem.model.*;
//import com.gym.oop_huce_gymsystem.util.*;
//
//public class MemberShipCardsDao {
//    public void addMember(MemberShipCards membershipcard) throws SQLException {
//        String query = "INSERT INTO members (name, phone, membership_type, training_package, registration_date) " +
//                "VALUES (?, ?, ?, ?, ?)";
//        try (Connection conn = DatabaseConnection.getConnection();
//             PreparedStatement stmt = conn.prepareStatement(query)) {
//            stmt.setString(1, member.getName());
//            stmt.setString(2, member.getPhone());
//            stmt.setString(3, member.getMembershipType());
//            stmt.setString(4, member.getTrainingPackage());
//            stmt.setDate(5, java.sql.Date.valueOf(member.getRegistrationDate()));
//
//            int rowsAffected = stmt.executeUpdate();
//            if (rowsAffected > 0) {
//                System.out.println("Đã thêm hội viên thành công: " + member.getPhone());
//            } else {
//                throw new SQLException("Không thể thêm hội viên: Không có hàng nào được thêm.");
//            }
//        } catch (SQLException e) {
//            throw e;
//        }
//    }
//
//
//
//}
