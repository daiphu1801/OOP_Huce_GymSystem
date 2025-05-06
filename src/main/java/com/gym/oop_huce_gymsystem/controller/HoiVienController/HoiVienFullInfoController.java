package com.gym.oop_huce_gymsystem.controller.HoiVienController;

import com.gym.oop_huce_gymsystem.model.Members;
import com.gym.oop_huce_gymsystem.ScenceController;
import com.gym.oop_huce_gymsystem.dao.MembersDao;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class HoiVienFullInfoController implements Initializable {

    @FXML private Label memberIdLabel;
    @FXML private Label cardCodeLabel;
    @FXML private Label fullNameLabel;
    @FXML private Label phoneNumberLabel;
    @FXML private Label genderLabel;
    @FXML private Label emailLabel;
    @FXML private Button backButton;
    @FXML private Button editInfoButton;

    private final ScenceController scenceController;
    private final MembersDao membersDao;
    private Members currentMember;
    private int memberId;

    // Khởi tạo controller
    public HoiVienFullInfoController() {
        this.scenceController = new ScenceController();
        this.membersDao = new MembersDao();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (memberIdLabel == null || fullNameLabel == null || phoneNumberLabel == null ||
                cardCodeLabel == null || genderLabel == null || emailLabel == null) {
            System.out.println("[HoiVienFullInfoController] Lỗi: Một hoặc nhiều Label không được inject từ FXML.");
        } else {
            System.out.println("[HoiVienFullInfoController] Tất cả Label đã được inject thành công.");
        }

        if (memberId != 0) {
            loadMemberData();
        }
    }

    // Thiết lập memberId và tải dữ liệu
    public void setMemberId(int memberId) {
        this.memberId = memberId;
        if (memberIdLabel != null) {
            loadMemberData();
        } else {
            System.out.println("[HoiVienFullInfoController] Chờ initialize hoàn tất để load dữ liệu.");
        }
    }

    // Tải dữ liệu hội viên từ cơ sở dữ liệu
    private void loadMemberData() {
        try {
            this.currentMember = membersDao.getMemberById(memberId);
            if (currentMember != null) {
                memberIdLabel.setText(String.valueOf(currentMember.getMemberId()));
                cardCodeLabel.setText(currentMember.getCardCode() != null ? currentMember.getCardCode() : "N/A");
                fullNameLabel.setText(currentMember.getFullName() != null ? currentMember.getFullName() : "N/A");
                phoneNumberLabel.setText(currentMember.getPhone() != null ? currentMember.getPhone() : "N/A");
                genderLabel.setText(currentMember.getGender() != null ? currentMember.getGender() : "N/A");
                emailLabel.setText(currentMember.getEmail() != null ? currentMember.getEmail() : "N/A");
            } else {
                System.out.println("[HoiVienFullInfoController] Không tìm thấy hội viên với ID: " + memberId);
            }
        } catch (SQLException e) {
            System.out.println("[HoiVienFullInfoController] Lỗi khi lấy dữ liệu hội viên từ database: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Các phương thức chuyển scene
    @FXML
    public void switchHome(javafx.scene.input.MouseEvent event) throws IOException {
        ActionEvent actionEvent = new ActionEvent(event.getSource(), event.getTarget());
        scenceController.switchToHelloView(actionEvent);
    }

    @FXML
    public void SwitchToregister(ActionEvent event) throws IOException {
        scenceController.switchToRegister(event);
    }

    @FXML
    public void switchToHelloView(ActionEvent event) throws IOException {
        scenceController.switchToHelloView(event);
    }

    @FXML
    public void switchToHoiVienFullInfo(ActionEvent event, int memberId) throws IOException {
        scenceController.switchToHoiVienFullInfo(event, memberId);
    }

    @FXML
    public void SwitchTolisthoivien(ActionEvent event) throws IOException {
        scenceController.switchToListHoiVien(event);
    }

    @FXML
    public void SwitchTothiet_bi(ActionEvent event) throws IOException {
        scenceController.switchToThietBi(event);
    }

    @FXML
    public void SwitchTothongke(ActionEvent event) throws IOException {
        scenceController.switchToThongKe(event);
    }

    @FXML
    public void SwitchTotrainerList(ActionEvent event) throws IOException {
        scenceController.SwitchTotrainerList(event);
    }

    @FXML
    public void switchToMemberCard(ActionEvent event) throws IOException {
        scenceController.switchToMemberCardList(event);
    }

    @FXML
    public void switchToCardAdd(ActionEvent event) throws IOException {
        scenceController.SwitchToCardAdd(event);
    }

    @FXML
    public void SwitchtoProduct(ActionEvent event) throws IOException {
        scenceController.SwitchtoProduct(event);
    }

    @FXML
    public void switchToProductRegis(ActionEvent event) throws IOException {
        scenceController.SwitchtoProductRegis(event);
    }

    @FXML
    public void switchToPTRegis(ActionEvent event) throws IOException {
        scenceController.SwitchToPT_Regis(event);
    }

    // Chuyển sang giao diện chỉnh sửa thông tin
    @FXML
    public void switchToEdit(ActionEvent event) throws IOException {
        System.out.println("[HoiVienFullInfoController] Chuyển sang giao diện sửa thông tin cho: " +
                (currentMember != null ? currentMember.getFullName() : "N/A"));
        scenceController.switchToHoiVienEdit(event, memberId);
    }
}