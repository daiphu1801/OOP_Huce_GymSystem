package com.gym.oop_huce_gymsystem.dao;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import com.gym.oop_huce_gymsystem.model.*;
import com.gym.oop_huce_gymsystem.util.*;


public class EquipmentsDao {
    //Hàm tạo thêm thành viên
    public void addEquipment(Equipments equipment) throws SQLException {
        String query = "INSERT INTO members (name,quantity, status, create_at) " +
                "VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, equipment.getName());
            stmt.setString(2, equipment.getStatus());
            stmt.setString(3, equipment.getQuantity());
            stmt.setDate(4, equipment.getCreate_at());

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Đã thêm thiết bị  thành công: " + equipment.getName());
            } else {
                throw new SQLException("Không thể thêm thiết bị : Không có hàng nào được thêm.");
            }
        } catch (SQLException e) {
            throw e;
        }
    }
    public void updateMember(Equipments equipment) throws SQLException {
        String query = "UPDATE equipment SET name = ?, status = ?, quantity = ?, " +
                "create_at = ? WHERE equipment_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, equipment.getName());
            stmt.setString(2, equipment.getStatus());
            stmt.setString(5, equipment.getQuantity());
            stmt.setDate(4, java.sql.Date.valueOf(equipment.getCreate_at()));
            stmt.setInt(6, equipment.getEquipmentId());
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("Không tìm thấy thiết bij  với ID: " + equipment.getEquipmentId());
            }
        } catch (SQLException e) {
            throw new SQLException("Lỗi khi cập nhật thiết bị : " + e.getMessage(), e);
        }
        }

public void deleteEquipment(int equipmentId) throws SQLException {
    String query = "DELETE FROM equipment WHERE equipment_id = ?";
    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement stmt = conn.prepareStatement(query)) {
        stmt.setInt(1, equipmentId);
        int rowsAffected = stmt.executeUpdate();
        if (rowsAffected == 0) {
            throw new SQLException("Không tìm thấy thiết bị  với ID: " + equipmentId);
        }
    }
}

}
