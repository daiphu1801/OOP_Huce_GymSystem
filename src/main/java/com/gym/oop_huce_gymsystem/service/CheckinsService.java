package com.gym.oop_huce_gymsystem.service;

import com.gym.oop_huce_gymsystem.dao.CheckinsDao;
import com.gym.oop_huce_gymsystem.dao.MembersDao;
import com.gym.oop_huce_gymsystem.model.Checkins;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

public class CheckinsService {
    private CheckinsDao checkinDAO;
    private MembersDao membersDao;

    public CheckinsService() {
        try {
            this.checkinDAO = new CheckinsDao();
            this.membersDao = new MembersDao();
        } catch (Exception e) {
            throw new RuntimeException("Không thể khởi tạo CheckinsService: " + e.getMessage(), e);
        }
    }

    // Thêm bản ghi check-in với kiểm tra hợp lệ
    public void addCheckin(String memberId) throws SQLException {
        // Kiểm tra hội viên tồn tại
        try {
            membersDao.getMemberById(memberId);
        } catch (SQLException e) {
            throw new SQLException("Hội viên với ID " + memberId + " không tồn tại.");
        }

        // Kiểm tra thẻ tập còn hiệu lực
        if (!checkinDAO.isMembershipCardValid(memberId)) {
            throw new SQLException("Thẻ tập của hội viên đã hết hạn hoặc không tồn tại.");
        }

        // Thêm check-in
        checkinDAO.addCheckin(memberId);
    }

    // Lấy danh sách check-in theo member_id
    public List<Checkins> getCheckinsByMemberId(String memberId) throws SQLException {
        // Kiểm tra hội viên tồn tại
        try {
            membersDao.getMemberById(memberId);
        } catch (SQLException e) {
            throw new SQLException("Hội viên với ID " + memberId + " không tồn tại.");
        }

        return checkinDAO.getCheckinsByMemberId(memberId);
    }

    // Lấy danh sách check-in theo member_id và ngày
    public List<Checkins> getCheckinsByDate(String memberId, String date) throws SQLException {
        // Kiểm tra hội viên tồn tại
        try {
            membersDao.getMemberById(memberId);
        } catch (SQLException e) {
            throw new SQLException("Hội viên với ID " + memberId + " không tồn tại.");
        }

        // Kiểm tra định dạng ngày
        if (!isValidDateFormat(date)) {
            throw new SQLException("Định dạng ngày không hợp lệ. Vui lòng sử dụng định dạng YYYY-MM-DD.");
        }

        return checkinDAO.getCheckinsByDate(memberId, date);
    }

    // Xóa bản ghi check-in
    public void deleteCheckin(int checkinId) throws SQLException {
        // Kiểm tra check-in tồn tại
        try {
            checkinDAO.getCheckinById(checkinId);
        } catch (SQLException e) {
            throw new SQLException("Bản ghi check-in với ID " + checkinId + " không tồn tại.");
        }

        checkinDAO.deleteCheckin(checkinId);
    }

    // Kiểm tra định dạng ngày
    private boolean isValidDateFormat(String date) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate.parse(date, formatter);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }
}