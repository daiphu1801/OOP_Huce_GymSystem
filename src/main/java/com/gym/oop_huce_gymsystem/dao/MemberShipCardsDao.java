package com.gym.oop_huce_gymsystem.dao;

import com.gym.oop_huce_gymsystem.model.MemberShipCards;
import com.gym.oop_huce_gymsystem.util.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MemberShipCardsDao {

    // Thêm một thẻ thành viên mới
    public void addMemberShipCard(MemberShipCards card) throws SQLException {
        String query = "INSERT INTO membership_cards (price, training_package, card_type, registration_date, expiry_date) " +
                "VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setDouble(1, card.getPrice());
            stmt.setString(2, card.getTrainingPackage());
            stmt.setString(3, card.getCardType());
            stmt.setDate(4, java.sql.Date.valueOf(card.getRegistrationDate()));
            stmt.setDate(5, java.sql.Date.valueOf(card.getExpiryDate()));

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Đã thêm thẻ thành viên thành công: " + card.getCardId());
            } else {
                throw new SQLException("Không thể thêm thẻ thành viên: Không có hàng nào được thêm.");
            }
        } catch (SQLException e) {
            throw new SQLException("Lỗi khi thêm thẻ thành viên: " + e.getMessage(), e);
        }
    }

    // Cập nhật thông tin thẻ thành viên
    public void updateMemberShipCard(MemberShipCards card) throws SQLException {
        String query = "UPDATE membership_cards SET training_package = ?, card_type = ?, price = ?, " +
                "registration_date = ?, expiry_date = ? WHERE card_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, card.getTrainingPackage());
            stmt.setString(2, card.getCardType());
            stmt.setDouble(3, card.getPrice());
            stmt.setDate(4, java.sql.Date.valueOf(card.getRegistrationDate()));
            stmt.setDate(5, java.sql.Date.valueOf(card.getExpiryDate()));
            stmt.setString(6, card.getCardId());

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("Không tìm thấy thẻ thành viên với ID: " + card.getCardId());
            }
        } catch (SQLException e) {
            throw new SQLException("Lỗi khi cập nhật thẻ thành viên: " + e.getMessage(), e);
        }
    }

    // Xóa thẻ thành viên theo cardId
    public void deleteMemberShipCard(String cardId) throws SQLException {
        String query = "DELETE FROM membership_cards WHERE card_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, cardId);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("Không tìm thấy thẻ thành viên với ID: " + cardId);
            }
        } catch (SQLException e) {
            throw new SQLException("Lỗi khi xóa thẻ thành viên: " + e.getMessage(), e);
        }
    }

    // Lấy thông tin thẻ thành viên theo cardId
    public MemberShipCards getMemberShipCardById(String cardId) throws SQLException {
        String query = "SELECT * FROM membership_cards WHERE card_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, cardId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    MemberShipCards card = new MemberShipCards();
                    card.setCardId(rs.getString("card_id"));
                    card.setTrainingPackage(rs.getString("training_package"));
                    card.setCardType(rs.getString("card_type"));
                    card.setPrice(rs.getDouble("price"));
                    card.setRegistrationDate(rs.getDate("registration_date").toLocalDate());
                    card.setExpiryDate(rs.getDate("expiry_date").toLocalDate());
                    return card;
                }
            }
        } catch (SQLException e) {
            throw new SQLException("Lỗi khi lấy thông tin thẻ thành viên: " + e.getMessage(), e);
        }
        return null;
    }

    // Lấy danh sách tất cả thẻ thành viên
    public List<MemberShipCards> getAllMemberShipCards() throws SQLException {
        List<MemberShipCards> cardList = new ArrayList<>();
        String query = "SELECT * FROM membership_cards";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                MemberShipCards card = new MemberShipCards();
                card.setCardId(rs.getString("card_id"));
                card.setTrainingPackage(rs.getString("training_package"));
                card.setCardType(rs.getString("card_type"));
                card.setPrice(rs.getDouble("price"));
                card.setRegistrationDate(rs.getDate("registration_date").toLocalDate());
                card.setExpiryDate(rs.getDate("expiry_date").toLocalDate());
                cardList.add(card);
            }
        } catch (SQLException e) {
            throw new SQLException("Lỗi khi lấy danh sách thẻ thành viên: " + e.getMessage(), e);
        }
        return cardList;
    }
}