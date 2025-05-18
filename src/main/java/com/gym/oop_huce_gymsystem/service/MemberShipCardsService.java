package com.gym.oop_huce_gymsystem.service;

import com.gym.oop_huce_gymsystem.dao.MemberShipCardsDao;
import com.gym.oop_huce_gymsystem.model.MemberShipCards;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class MemberShipCardsService {
    private final MemberShipCardsDao memberShipCardDao;

    public MemberShipCardsService(MemberShipCardsDao memberShipCardDao) {
        this.memberShipCardDao = memberShipCardDao;
    }

    // Validate MemberShipCards object
    private void validateMemberShipCard(MemberShipCards card) throws IllegalArgumentException {
        if (card == null) {
            throw new IllegalArgumentException("Thẻ thành viên không được null");
        }
        if (card.getTrainingPackage() == null || card.getTrainingPackage().trim().isEmpty()) {
            throw new IllegalArgumentException("Gói tập luyện không được null hoặc rỗng");
        }
        if (card.getCardType() == null || card.getCardType().trim().isEmpty()) {
            throw new IllegalArgumentException("Loại thẻ không được null hoặc rỗng");
        }
        if (card.getPrice() == null || card.getPrice() <= 0) {
            throw new IllegalArgumentException("Giá thẻ phải là số dương");
        }
        if (card.getRegistrationDate() == null) {
            throw new IllegalArgumentException("Ngày đăng ký không được null");
        }
        if (card.getExpiryDate() == null) {
            throw new IllegalArgumentException("Ngày hết hạn không được null");
        }
        if (!card.getExpiryDate().isAfter(card.getRegistrationDate())) {
            throw new IllegalArgumentException("Ngày hết hạn phải sau ngày đăng ký");
        }
    }

    // Kiểm tra xem cardId đã tồn tại hay chưa
    private void validateCardIdNotExists(String cardId) throws SQLException, IllegalArgumentException {
        MemberShipCards existingCard = memberShipCardDao.getMemberShipCardById(cardId);
        if (existingCard != null) {
            throw new IllegalArgumentException("ID thẻ thành viên đã tồn tại: " + cardId);
        }
    }

    // Thêm thẻ thành viên
//    public void addMemberShipCard(MemberShipCards card) throws SQLException, IllegalArgumentException {
//        validateMemberShipCard(card);
//        validateCardIdNotExists(card.getCardId());
//        memberShipCardDao.addMemberShipCard(card);
//    }

    public String addMemberShipCard(MemberShipCards card) throws SQLException, IllegalArgumentException {
        validateMemberShipCard(card);
        return memberShipCardDao.addMemberShipCard(card);
    }

    // Cập nhật thẻ thành viên
    public void updateMemberShipCard(MemberShipCards card) throws SQLException, IllegalArgumentException {
        validateMemberShipCard(card);
        MemberShipCards existingCard = memberShipCardDao.getMemberShipCardById(card.getCardId());
        if (existingCard == null) {
            throw new IllegalArgumentException("Không tìm thấy thẻ thành viên với ID: " + card.getCardId());
        }
        memberShipCardDao.updateMemberShipCard(card);
    }

    // Xóa thẻ thành viên
    public void deleteMemberShipCard(String cardId) throws SQLException, IllegalArgumentException {
        if (cardId == null || cardId.trim().isEmpty()) {
            throw new IllegalArgumentException("ID thẻ thành viên không được null hoặc rỗng");
        }
        MemberShipCards existingCard = memberShipCardDao.getMemberShipCardById(cardId);
        if (existingCard == null) {
            throw new IllegalArgumentException("Không tìm thấy thẻ thành viên với ID: " + cardId);
        }
        memberShipCardDao.deleteMemberShipCard(cardId);
    }

    // Lấy thông tin thẻ thành viên theo ID
    public MemberShipCards getMemberShipCardById(String cardId) throws SQLException, IllegalArgumentException {
        if (cardId == null || cardId.trim().isEmpty()) {
            throw new IllegalArgumentException("ID thẻ thành viên không được null hoặc rỗng");
        }
        MemberShipCards card = memberShipCardDao.getMemberShipCardById(cardId);
        if (card == null) {
            throw new IllegalArgumentException("Không tìm thấy thẻ thành viên với ID: " + cardId);
        }
        return card;
    }

    // Lấy danh sách tất cả thẻ thành viên
    public List<MemberShipCards> getAllMemberShipCards() throws SQLException {
        return memberShipCardDao.getAllMemberShipCards();
    }
}