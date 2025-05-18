package com.gym.oop_huce_gymsystem.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.gym.oop_huce_gymsystem.model.*;
import com.gym.oop_huce_gymsystem.util.*;


public class EquipmentsDao {
    //Hàm tạo thêm thành viên
    public void addEquipment(Equipments equipment) throws SQLException {
        String query = "INSERT INTO equipment (name,quantity, status, purchase_date) " +
                "VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, equipment.getName());
            stmt.setInt(2, equipment.getQuantity());
            stmt.setString(3, equipment.getStatus());
            stmt.setDate(4, java.sql.Date.valueOf(equipment.getPurchase_Date()));

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

    //Hàm sửa Equipments
    public void updateEquipment(Equipments equipment) throws SQLException {
        String query = "UPDATE equipment SET name = ?, quantity = ?, status = ?, " +
                "purchase_date = ? WHERE equipment_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, equipment.getName());
            stmt.setInt(2, equipment.getQuantity());
            stmt.setString(3, equipment.getStatus());;
            stmt.setDate(4, java.sql.Date.valueOf(equipment.getPurchase_Date()));
            stmt.setString(5, equipment.getEquipmentId());
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("Không tìm thấy thiết bij  với ID: " + equipment.getEquipmentId());
            }
        } catch (SQLException e) {
            throw new SQLException("Lỗi khi cập nhật thiết bị : " + e.getMessage(), e);
        }
    }

    public void deleteEquipment(String equipmentId) throws SQLException {
        String query = "DELETE FROM equipment WHERE equipment_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, equipmentId);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("Không tìm thấy thiết bị  với ID: " + equipmentId);
            }
        }
    }


    //Hàm lấy danh sách thành viên
    public List<Equipments> getAllEquipments() throws SQLException {
        List<Equipments> equipments = new ArrayList<>();
        String query = "SELECT * FROM equipment";
        try (Connection conn = DatabaseConnection.getConnection();
        PreparedStatement stmt = conn.prepareStatement(query)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Equipments equipment = new Equipments();
                equipment.setEquipmentId(rs.getString("equipment_id"));
                equipment.setName(rs.getString("name"));
                equipment.setQuantity(rs.getInt("quantity"));
                equipment.setStatus(rs.getString("status"));
                equipment.setPurchase_Date(rs.getDate("purchase_date").toLocalDate());
                equipments.add(equipment);
            }
        }
        return equipments;
    }

    //Hàm lấy details 1 equipments
    public Equipments getEquipmentById(String equipmentId) throws SQLException {
        String query = "SELECT * FROM equipment WHERE equipment_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
        PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, equipmentId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Equipments equipment = new Equipments();
                equipment.setEquipmentId(rs.getString("equipment_id"));
                equipment.setName(rs.getString("name"));
                equipment.setQuantity(rs.getInt("quantity"));
                equipment.setStatus(rs.getString("status"));
                equipment.setPurchase_Date(rs.getDate("purchase_date").toLocalDate());
                return equipment;
            }
        }
        return null;
    }

    // Hàm kiểm tra trùng tên Equipments
    public boolean checkEquipment(Equipments equipment) throws SQLException {
        String query = "SELECT 1 FROM equipment WHERE name = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, equipment.getName()); // Dùng tên để kiểm tra
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next(); // Trả về true nếu tìm thấy bản ghi trùng tên
            }
        }
    }

}
