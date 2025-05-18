package com.gym.oop_huce_gymsystem.controller.HoiVienController;

import com.gym.oop_huce_gymsystem.model.Members;
import com.gym.oop_huce_gymsystem.ScenceController;
import com.gym.oop_huce_gymsystem.dao.MembersDao;
import com.gym.oop_huce_gymsystem.service.MembersService;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class HoiVienEditController implements Initializable {

    @FXML private Label cardCodeField;
    @FXML private TextField fullNameField;
    @FXML private TextField phoneField;
    @FXML private ComboBox<String> genderComboBox;
    @FXML private TextField emailField;
    @FXML private Button backButton;
    @FXML private Button saveButton;

    private final ScenceController scenceController;
    private final MembersDao membersDao;
    private final MembersService membersService;
    private String memberId;

    // Khởi tạo controller
    public HoiVienEditController() {
        this.scenceController = new ScenceController();
        this.membersDao = new MembersDao();
        this.membersService = new MembersService();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Khởi tạo danh sách cho ComboBox giới tính
        genderComboBox.setItems(FXCollections.observableArrayList("Nam", "Nữ"));
    }

    // Thiết lập memberId và tải dữ liệu
    public void setMemberId(String memberId) {
        this.memberId = memberId;
        loadMemberData();
    }

    // Tải dữ liệu hội viên từ cơ sở dữ liệu
    private void loadMemberData() {
        try {
            Members member = membersDao.getMemberById(memberId);
            if (member != null) {
                cardCodeField.setText(member.getCardCode());
                fullNameField.setText(member.getFullName());
                phoneField.setText(member.getPhone());
                genderComboBox.setValue(member.getGender());
                emailField.setText(member.getEmail());
            } else {
                showAlert(Alert.AlertType.ERROR, "Lỗi", "Không tìm thấy hội viên với ID: " + memberId);
            }
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Không tải được dữ liệu: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Xử lý trở về danh sách hội viên
    @FXML
    private void backToList(ActionEvent event) throws IOException {
        scenceController.switchToListHoiVien(event);
    }

    // Xử lý lưu thay đổi
    @FXML
    private void saveChanges(ActionEvent event) {
        try {
            // Tạo đối tượng Members với dữ liệu mới
            Members updatedMember = new Members(
                    memberId,
                    cardCodeField.getText(),
                    fullNameField.getText(),
                    phoneField.getText(),
                    genderComboBox.getValue(),
                    emailField.getText()
            );

            // Cập nhật thông tin qua MembersService
            membersService.updateMember(updatedMember);
            showAlert(Alert.AlertType.INFORMATION, "Thành công", "Cập nhật thông tin hội viên thành công.");
            backToList(event);
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Mã hội viên phải là số nguyên hợp lệ.");
        } catch (Exception e) {
            if (e.getMessage().contains("Số điện thoại hoặc mã thẻ đã tồn tại")) {
                showAlert(Alert.AlertType.ERROR, "Lỗi", "Số điện thoại hoặc mã thẻ đã tồn tại trong hệ thống.");
            } else {
                showAlert(Alert.AlertType.ERROR, "Lỗi", "Cập nhật thất bại: " + e.getMessage());
            }
            e.printStackTrace();
        }
    }

    // Hiển thị thông báo
    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}