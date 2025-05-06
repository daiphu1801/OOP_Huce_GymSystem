package com.gym.oop_huce_gymsystem.controller.HoiVienController;

import com.gym.oop_huce_gymsystem.model.Members;
import com.gym.oop_huce_gymsystem.ScenceController;
import com.gym.oop_huce_gymsystem.dao.MembersDao;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class HoiVienEditController implements Initializable {

    @FXML private TextField memberIdField;
    @FXML private TextField nameField;
    @FXML private TextField phoneField;
    @FXML private ComboBox<String> membershipTypeComboBox;
    @FXML private ComboBox<String> trainingPackageComboBox;
    @FXML private DatePicker registrationDateField;
    @FXML private Button backButton;
    @FXML private Button saveButton;

    private final ScenceController scenceController;
    private final MembersDao membersDao;
    private int memberId;

    public HoiVienEditController() {
        this.scenceController = new ScenceController();
        this.membersDao = new MembersDao();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Khởi tạo danh sách cho ComboBox
        membershipTypeComboBox.setItems(FXCollections.observableArrayList("Thẻ thường", "Thẻ VIP", "Thẻ Premium"));
        trainingPackageComboBox.setItems(FXCollections.observableArrayList("1 tháng", "3 tháng", "6 tháng", "12 tháng"));
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
        loadMemberData();
    }

    private void loadMemberData() {
        try {
            Members member = membersDao.getMemberById(memberId);
            if (member != null) {
                memberIdField.setText(String.valueOf(member.getMemberId()));
                nameField.setText(member.getName());
                phoneField.setText(member.getPhone());
                membershipTypeComboBox.setValue(member.getMembershipType());
                trainingPackageComboBox.setValue(member.getTrainingPackage());
                registrationDateField.setValue(member.getRegistrationDate());
            } else {
                showAlert(Alert.AlertType.ERROR, "Lỗi", "Không tìm thấy hội viên với ID: " + memberId);
            }
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Không tải được dữ liệu: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void backToList(ActionEvent event) throws IOException {
        scenceController.switchToListHoiVien(event);
    }

    @FXML
    private void saveChanges(ActionEvent event) {
        try {
            // Kiểm tra nếu ngày không được chọn
            if (registrationDateField.getValue() == null) {
                showAlert(Alert.AlertType.ERROR, "Lỗi", "Vui lòng chọn ngày đăng ký.");
                return;
            }

            Members updatedMember = new Members(
                    Integer.parseInt(memberIdField.getText()),
                    nameField.getText(),
                    phoneField.getText(),
                    membershipTypeComboBox.getValue(),
                    trainingPackageComboBox.getValue(),
                    registrationDateField.getValue(),
                    null // created_at có thể để null hoặc lấy từ database
            );
            membersDao.updateMember(updatedMember);
            showAlert(Alert.AlertType.INFORMATION, "Thành công", "Cập nhật thông tin hội viên thành công.");
            backToList(event);
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Mã thẻ phải là số nguyên hợp lệ.");
        } catch (SQLException e) {
            if (e.getMessage().contains("Số điện thoại đã tồn tại trong hệ thống")) {
                showAlert(Alert.AlertType.ERROR, "Lỗi", "Số điện thoại đã tồn tại trong hệ thống.");
            } else {
                showAlert(Alert.AlertType.ERROR, "Lỗi", "Cập nhật thất bại: " + e.getMessage());
            }
            e.printStackTrace();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể quay lại danh sách: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}