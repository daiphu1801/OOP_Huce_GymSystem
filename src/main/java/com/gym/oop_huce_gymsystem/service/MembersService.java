package com.gym.oop_huce_gymsystem.service;

import com.gym.oop_huce_gymsystem.dao.MembersDao;
import com.gym.oop_huce_gymsystem.model.Members;
import java.sql.SQLException;
import java.util.List;

public class MembersService {
    private final MembersDao membersDao;

    // Khởi tạo MembersService với MembersDao
    public MembersService() {
        this.membersDao = new MembersDao();
    }

//    // Thêm một hội viên mới
//    public void addMember(Members member) throws Exception {
//        // Kiểm tra dữ liệu đầu vào
//        if (member.getFullName() == null || member.getFullName().trim().isEmpty()) {
//            throw new Exception("Tên hội viên không được để trống.");
//        }
//        if (member.getPhone() == null || !member.getPhone().matches("\\d{10}") || member.getPhone().charAt(0) != '0') {
//            throw new Exception("Số điện thoại không hợp lệ (phải có 10 chữ số và bắt đầu bằng 0).");
//        }
//        if (member.getCardCode() == null || member.getCardCode().trim().isEmpty()) {
//            throw new Exception("Mã thẻ không được để trống.");
//        }
//        if (member.getGender() == null || (!member.getGender().equalsIgnoreCase("Nam") && !member.getGender().equalsIgnoreCase("Nữ"))) {
//            throw new Exception("Giới tính không hợp lệ (phải là 'Nam' hoặc 'Nữ').");
//        }
//        if (member.getEmail() == null || !member.getEmail().matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
//            throw new Exception("Email không hợp lệ.");
//        }
//
//        // Kiểm tra số điện thoại đã tồn tại
//        if (membersDao.isPhoneExists(member.getPhone())) {
//            throw new Exception("Số điện thoại đã tồn tại trong hệ thống.");
//        }
//
//        try {
//            membersDao.addMember(member);
//        } catch (SQLException e) {
//            throw new Exception("Lỗi khi thêm hội viên: " + e.getMessage());
//        }
//    }

    public int addMember(Members member) throws Exception {
        if (member.getFullName() == null || member.getFullName().trim().isEmpty()) {
            throw new Exception("Tên hội viên không được để trống.");
        }
        if (member.getPhone() == null || !member.getPhone().matches("\\d{10}") || member.getPhone().charAt(0) != '0') {
            throw new Exception("Số điện thoại không hợp lệ (phải có 10 chữ số và bắt đầu bằng 0).");
        }
        if (member.getCardCode() == null || member.getCardCode().trim().isEmpty()) {
            throw new Exception("Mã thẻ không được để trống.");
        }
        if (member.getGender() == null || (!member.getGender().equalsIgnoreCase("Nam") && !member.getGender().equalsIgnoreCase("Nữ"))) {
            throw new Exception("Giới tính không hợp lệ (phải là 'Nam' hoặc 'Nữ').");
        }
        if (member.getEmail() == null || !member.getEmail().matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            throw new Exception("Email không hợp lệ.");
        }

        if (membersDao.isPhoneExists(member.getPhone())) {
            throw new Exception("Số điện thoại đã tồn tại trong hệ thống.");
        }

        try {
            return membersDao.addMember(member);
        } catch (SQLException e) {
            throw new Exception("Lỗi khi thêm hội viên: " + e.getMessage());
        }
    }

    // Lấy danh sách tất cả hội viên
    public List<Members> getAllMembers() throws Exception {
        try {
            List<Members> members = membersDao.getAllMembers();
            System.out.println("MembersService: Trả về " + members.size() + " hội viên.");
            return members;
        } catch (SQLException e) {
            throw new Exception("Lỗi khi lấy danh sách hội viên: " + e.getMessage());
        }
    }

    // Cập nhật thông tin hội viên
    public void updateMember(Members member) throws Exception {
        // Kiểm tra dữ liệu đầu vào
        if (member.getFullName() == null || member.getFullName().trim().isEmpty()) {
            throw new Exception("Tên hội viên không được để trống.");
        }
        if (member.getPhone() == null || !member.getPhone().matches("\\d{10}") || member.getPhone().charAt(0) != '0') {
            throw new Exception("Số điện thoại không hợp lệ (phải có 10 chữ số và bắt đầu bằng 0).");
        }
        if (member.getCardCode() == null || member.getCardCode().trim().isEmpty()) {
            throw new Exception("Mã thẻ không được để trống.");
        }
        if (member.getGender() == null || (!member.getGender().equalsIgnoreCase("Nam") && !member.getGender().equalsIgnoreCase("Nữ"))) {
            throw new Exception("Giới tính không hợp lệ (phải là 'Nam' hoặc 'Nữ').");
        }
        if (member.getEmail() == null || !member.getEmail().matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            throw new Exception("Email không hợp lệ.");
        }

        try {
            membersDao.updateMember(member);
        } catch (SQLException e) {
            throw new Exception("Lỗi khi cập nhật hội viên: " + e.getMessage());
        }
    }

    // Xóa một hội viên
    public void deleteMember(int memberId) throws Exception {
        try {
            membersDao.deleteMember(memberId);
        } catch (SQLException e) {
            throw new Exception("Không thể xóa hội viên: " + e.getMessage());
        }
    }
}