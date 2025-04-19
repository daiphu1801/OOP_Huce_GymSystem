package com.gym.oop_huce_gymsystem.service;

import com.gym.oop_huce_gymsystem.dao.*;
import com.gym.oop_huce_gymsystem.model.Members;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;


public class MembersService {
    private final MembersDao membersDao;

    public MembersService() {
        this.membersDao =  new MembersDao();
    }

    public void addMember(Members member) throws Exception {
        // Kiểm tra dữ liệu
        if (member.getName() == null || member.getName().trim().isEmpty()) {
            throw new Exception("Tên hội viên không được để trống.");
        }
        if (member.getPhone() == null || !member.getPhone().matches("\\d{10}") || member.getPhone().length() != 10 || member.getPhone().charAt(0) != '0'){
            throw new Exception("Số điện thoại không hợp lệ (phải có 10 chữ số) hoặc là phải là chữ số không ở đầu.");
        }
        if (member.getMembershipType() == null) {
            throw new Exception("Loại gói tập không được để trống");
        }
        try {
            membersDao.addMember(member);
        } catch (SQLException e) {
            throw new Exception(e.getMessage());
        }
    }

    public List<Members> getAllMembers() throws Exception {
        try {
            return membersDao.getAllMembers();
        } catch (SQLException e) {
            throw new Exception("Không thể lấy danh sách hội viên: " + e.getMessage());
        }
    }

    public void updateMember(Members member) throws Exception {
        // Kiểm tra dữ liệu (tương tự addMember)
        if (member.getName() == null || member.getName().trim().isEmpty()) {
            throw new Exception("Tên hội viên không được để trống.");
        }
        if (member.getPhone() == null || !member.getPhone().matches("\\d{11}")) {
            throw new Exception("Số điện thoại không hợp lệ (phải có 11 chữ số).");
        }
        if (member.getMembershipType() == null) {
            throw new Exception("Loại gói tập không được để trống");
        }

        try {
            membersDao.updateMember(member);
        } catch (SQLException e) {
            throw new Exception(e.getMessage());
        }
    }

    public void deleteMember(int memberId) throws Exception {
        try {
            membersDao.deleteMember(memberId);
        } catch (SQLException e) {
            throw new Exception("Không thể xóa hội viên: " + e.getMessage());
        }
    }
}
