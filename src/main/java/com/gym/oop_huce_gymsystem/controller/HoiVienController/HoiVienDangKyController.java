package com.gym.oop_huce_gymsystem.controller.HoiVienController;

import com.gym.oop_huce_gymsystem.model.Members;
import com.gym.oop_huce_gymsystem.service.MembersService;
import com.gym.oop_huce_gymsystem.ScenceController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class HoiVienDangKyController implements Initializable {

    @FXML
    private TextField tenHoiVienField;

    @FXML
    private TextField soDienThoaiField;

    @FXML
    private TextField cardCodeField;

    @FXML
    private ComboBox<String> genderComboBox;

    @FXML
    private TextField emailTextField;

    @FXML
    private Button confirmButton;

    @FXML
    private Button backButton;

    private final MembersService membersService = new MembersService();
    private final ScenceController scenceController = new ScenceController();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Thiết lập các giá trị cho ComboBox giới tính
        ObservableList<String> genders = FXCollections.observableArrayList("Nam", "Nữ");
        genderComboBox.setItems(genders);
        genderComboBox.setValue(genders.get(0));
    }

    // Xử lý đăng ký hội viên mới
    @FXML
    public void dangKyHoiVien(ActionEvent event) {
        try {
            String fullName = tenHoiVienField.getText().trim();
            String phoneNumber = soDienThoaiField.getText().trim();
            String cardCode = cardCodeField.getText().trim();
            String gender = genderComboBox.getValue();
            String email = emailTextField.getText().trim();

            // Tạo đối tượng Members
            Members newMember = new Members();
            newMember.setFullName(fullName);
            newMember.setPhone(phoneNumber);
            newMember.setCardCode(cardCode);
            newMember.setGender(gender);
            newMember.setEmail(email);

            // Gọi service để thêm hội viên
            membersService.addMember(newMember);

            showAlert(Alert.AlertType.INFORMATION, "Thành công",
                    "Thêm hội viên thành công! Số điện thoại: " + newMember.getPhone());

            scenceController.switchToListHoiVien(event);
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể thêm hội viên: " + e.getMessage());
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

    // Chuyển về màn hình chính
    @FXML
    public void switchHome(javafx.scene.input.MouseEvent event) throws IOException {
        scenceController.switchToHelloView(new ActionEvent(event.getSource(), event.getTarget()));
    }

    // Các phương thức chuyển scene khác
    @FXML
    public void switchToHoiVienFullInfo(ActionEvent event) throws IOException {
        scenceController.switchToHoiVienFullInfo(event);
    }

    @FXML
    public void SwitchTolisthoivien(ActionEvent event) throws IOException {
        scenceController.switchToListHoiVien(event);
    }

    @FXML
    public void switchToRegister(ActionEvent event) throws IOException {
        scenceController.switchToRegister(event);
    }

    @FXML
    public void SwitchTothiet_bi(ActionEvent event) throws IOException {
        scenceController.switchToThietBi(event);
    }

    @FXML
    public void switchToThietBiInfoFull(ActionEvent event) throws IOException {
        scenceController.switchToThietBiInfoFull(event);
    }

    @FXML
    public void switchToThietBiRegis(ActionEvent event) throws IOException {
        scenceController.switchToThietBiRegis(event);
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
    public void switchToMemberCardList(ActionEvent event) throws IOException {
        scenceController.switchToMemberCardList(event);
    }

    @FXML
    public void switchToCardAdd(ActionEvent event) throws IOException {
        scenceController.SwitchToCardAdd(event);
    }

    @FXML
    public void switchToProduct(ActionEvent event) throws IOException {
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
}