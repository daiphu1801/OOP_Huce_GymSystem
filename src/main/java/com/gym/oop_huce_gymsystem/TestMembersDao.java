package com.gym.oop_huce_gymsystem;

import com.gym.oop_huce_gymsystem.dao.MembersDao;
import com.gym.oop_huce_gymsystem.model.Members;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.sql.SQLException;

public class TestMembersDao {
    public static void main(String[] args) {
        MembersDao dao = new MembersDao();
        try {
            // 1. Test thêm hội viên
            Members newMember = new Members("Nguyễn Văn A", "0123456789", "VIP",
                    LocalDate.now());
            dao.addMember(newMember);
            System.out.println("✅ Thêm thành viên thành công.");

            // 2. Test lấy tất cả hội viên
            List<Members> membersList = dao.getAllMembers();
            System.out.println("✅ Danh sách thành viên:");
            for (Members m : membersList) {
                System.out.println(m);
            }

            // 3. Test lấy thành viên theo ID
            int idTest = membersList.get(0).getMemberId(); // lấy ID đầu tiên
            Members member = dao.getMemberById(idTest);
            System.out.println("✅ Thông tin hội viên ID = " + idTest + ": " + member);

            // 4. Test cập nhật thành viên
            member.setName("Nguyễn Văn B");
            member.setPhone("0999888777");
            dao.updateMember(member);
            System.out.println("✅ Cập nhật thành viên thành công.");

//            // 5. Test xóa thành viên
//            dao.deleteMember(member.getMemberId());
//            System.out.println("✅ Xóa thành viên thành công.");

        } catch (Exception e) {
            System.err.println("❌ Lỗi: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
