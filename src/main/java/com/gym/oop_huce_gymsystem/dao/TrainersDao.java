package com.gym.oop_huce_gymsystem.dao;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import com.gym.oop_huce_gymsystem.model.*;
import com.gym.oop_huce_gymsystem.util.*;

public class TrainersDao {
    public void addTrainers(Trainers trainer) throws Exception {
        String query = "INSERT INTO trainers (name, gender, phone, email, specialization) " +
                "VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, trainer.getName());
            stmt.setString(2, trainer.getGender());
            stmt.setString(3, trainer.getPhone());
            stmt.setString(4, trainer.getEmail());
            stmt.setString(5, trainer.getSpecialization());

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Đã thêm huấn luyện viên thành công: " + trainer.getPhone());
            } else {
                throw new SQLException("Không thể thêm huấn luyện viên: Không có hàng nào được thêm.");
            }
        } catch (SQLException e) {
            throw e;
        }
    }

    public void updateTrainers(Trainers trainer) throws SQLException {
        String query = "UPDATE trainers SET name = ?, gender = ?, phone = ?, email = ?, " +
                "specialization = ? WHERE trainer_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, trainer.getName());
            stmt.setString(2, trainer.getGender());
            stmt.setString(3, trainer.getPhone());
            stmt.setString(4, trainer.getEmail());
            stmt.setString(5, trainer.getSpecialization());
            stmt.setString(6, trainer.getTrainerId());
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("Không tìm thấy huấn luyện viên với ID: " + trainer.getTrainerId());
            }
        } catch (SQLException e) {
            if (e.getErrorCode() == 2627) {
                throw new SQLException("Số điện thoại đã tồn tại trong hệ thống.");
            }
            throw e;
        }
    }

    public void deleteTrainers(String trainerId) throws SQLException {
        String query = "DELETE FROM trainers WHERE trainer_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, trainerId);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("Không tìm thấy huấn luyện viên với ID: " + trainerId);
            }
        }
    }

    // Lấy danh sách tất cả huấn luyện viên
    public List<Trainers> getAllTrainers() throws SQLException {
        List<Trainers> trainers = new ArrayList<>();
        String query = "SELECT trainer_id, name, gender, phone, email, specialization FROM trainers";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Trainers trainer = new Trainers(
                        rs.getString("trainer_id"),
                        rs.getString("name"),
                        rs.getString("gender"),
                        rs.getString("phone"),
                        rs.getString("email"),
                        rs.getString("specialization")
                );
                trainers.add(trainer);
            }
        }
        return trainers;
    }

    // Lấy chi tiết huấn luyện viên theo ID
    public Trainers getTrainerById(String trainerId) throws SQLException {
        String query = "SELECT trainer_id, name, gender, phone, email, specialization FROM trainers WHERE trainer_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, trainerId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Trainers(
                            rs.getString("trainer_id"),
                            rs.getString("name"),
                            rs.getString("gender"),
                            rs.getString("phone"),
                            rs.getString("email"),
                            rs.getString("specialization")
                    );
                } else {
                    throw new SQLException("Không tìm thấy huấn luyện viên với ID: " + trainerId);
                }
            }
        }
    }

    // Kiểm tra sự tồn tại của huấn luyện viên theo ID
    public boolean trainerExists(String trainerId) throws SQLException {
        String query = "SELECT COUNT(*) FROM trainers WHERE trainer_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, trainerId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }

    //Kiem tra so dien thoai
    public boolean isPhoneExists(String phone) throws SQLException {
        String query = "SELECT COUNT(*) FROM trainers WHERE phone = ?";
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


}