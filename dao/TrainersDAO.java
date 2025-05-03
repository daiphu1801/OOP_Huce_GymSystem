package com.gym.oop_huce_gymsystem.dao;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import com.gym.oop_huce_gymsystem.model.*;
import com.gym.oop_huce_gymsystem.util.*;

public class TrainersDAO {
    public void addTrainers(Trainers trainer) throws Exception {
        String query = "INSERT INTO trainer (name, phone, email, specialization,) " +
                "VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, trainer.getName());
            stmt.setString(2, trainer.getPhone());
            stmt.setString(3, trainer.getEmail());
            stmt.setString(4, trainer.getSpecialization());

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
    public void updateMember(Trainers trainer) throws SQLException {
        String query = "UPDATE trainer SET name = ?, phone = ?, email = ?, " +
                "specialization = ?, training_package = ? WHERE trainer_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, trainer.getName());
            stmt.setString(2, trainer.getPhone());
            stmt.setString(3, trainer.getEmail());
            stmt.setString(4, trainer.getSpecialization());
            stmt.setInt(5, trainer.getTrainerId());
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
    //xóa
    public void deleteTrainer(int trainerId) throws SQLException {
        String query = "DELETE FROM trainer WHERE trainer_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, trainerId);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("Không tìm thấy huấn luyện viên  với ID: " + trainerId);
            }
        }
    }


}
