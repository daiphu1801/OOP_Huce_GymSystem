package com.gym.oop_huce_gymsystem;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class RegisterController implements Initializable {

    @FXML
    private TextField tenHoiVienField;

    @FXML
    private TextField soDienThoaiField;

    @FXML
    private ComboBox<String> loaiTheComboBox;

    @FXML
    private ComboBox<String> goiTapComboBox;

    @FXML
    private DatePicker ngayDangKyPicker;

    @FXML
    private Button confirmButton;

    @FXML
    private Button backButton;

    private final MemberDAO memberDAO = MemberDAO.getInstance();
    private final ScenceController scenceController = new ScenceController();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Thiết lập các giá trị cho ComboBox loại thẻ
        ObservableList<String> cardTypes = FXCollections.observableArrayList(
                "Thẻ thường", "Thẻ VIP", "Thẻ Premium");
        loaiTheComboBox.setItems(cardTypes);
        loaiTheComboBox.setValue("Thẻ thường"); // Giá trị mặc định

        // Thiết lập các giá trị cho ComboBox gói tập
        ObservableList<String> packageTypes = FXCollections.observableArrayList(
                "1 tháng", "3 tháng", "6 tháng", "12 tháng");
        goiTapComboBox.setItems(packageTypes);
        goiTapComboBox.setValue("1 tháng"); // Giá trị mặc định

        // Thiết lập giá trị mặc định cho DatePicker là ngày hiện tại
        ngayDangKyPicker.setValue(LocalDate.now());
    }

    @FXML
    public void dangKyHoiVien(ActionEvent event) {
        try {
            // Kiểm tra dữ liệu đầu vào
            if (!validateInput()) {
                return;
            }

            // Lấy dữ liệu từ form
            String fullName = tenHoiVienField.getText().trim();
            String phoneNumber = soDienThoaiField.getText().trim();
            String cardType = loaiTheComboBox.getValue();
            String packageType = goiTapComboBox.getValue();
            LocalDate registerDate = ngayDangKyPicker.getValue();

            // Tạo đối tượng thành viên mới
            Member newMember = new Member(fullName, phoneNumber, packageType, cardType, registerDate);

            // Thêm thành viên vào cơ sở dữ liệu
            memberDAO.addMember(newMember);

            // Hiển thị thông báo thành công
            showAlert(AlertType.INFORMATION, "Thêm hội viên thành công",
                    "Mã thẻ của hội viên là: " + newMember.getMemberId());

            // Quay lại màn hình danh sách hội viên
            scenceController.SwitchTolisthoivien(event);

        } catch (Exception e) {
            showAlert(AlertType.ERROR, "Lỗi", "Không thể thêm hội viên: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private boolean validateInput() {
        StringBuilder errorMessage = new StringBuilder();

        if (tenHoiVienField.getText() == null || tenHoiVienField.getText().trim().isEmpty()) {
            errorMessage.append("Họ và tên không được để trống\n");
        }

        if (soDienThoaiField.getText() == null || soDienThoaiField.getText().trim().isEmpty()) {
            errorMessage.append("Số điện thoại không được để trống\n");
        } else if (!soDienThoaiField.getText().matches("\\d{10,11}")) {
            errorMessage.append("Số điện thoại phải có 10-11 chữ số\n");
        }

        if (loaiTheComboBox.getValue() == null) {
            errorMessage.append("Vui lòng chọn loại thẻ\n");
        }

        if (goiTapComboBox.getValue() == null) {
            errorMessage.append("Vui lòng chọn gói tập\n");
        }

        if (ngayDangKyPicker.getValue() == null) {
            errorMessage.append("Vui lòng chọn ngày đăng ký\n");
        }

        if (errorMessage.length() > 0) {
            showAlert(AlertType.ERROR, "Lỗi nhập liệu", errorMessage.toString());
            return false;
        }

        return true;
    }

    private void showAlert(AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    public void switchToHelloView(ActionEvent event) throws IOException {
        setupScene("hello-view.fxml", event);
    }

    @FXML
    public void switchToHoiVienFullInfo(ActionEvent event) throws IOException {
        setupScene("hoivien_full_info.fxml", event);
    }

    @FXML
    public void switchToListHoiVien(ActionEvent event) throws IOException {
        setupScene("list_hoivien.fxml", event);
    }

    @FXML
    public void switchToRegister(ActionEvent event) throws IOException {
        setupScene("register.fxml", event);
    }

    @FXML
    public void switchToThietBi(ActionEvent event) throws IOException {
        setupScene("thiet_bi.fxml", event);
    }

    @FXML
    public void switchToThietBiInfoFull(ActionEvent event) throws IOException {
        setupScene("thietbi_info_full.fxml", event);
    }

    @FXML
    public void switchToThietBiRegis(ActionEvent event) throws IOException {
        setupScene("thietbi_regis.fxml", event);
    }

    @FXML
    public void switchToThongKe(ActionEvent event) throws IOException {
        setupScene("thongke.fxml", event);
    }

    // Keep the original methods for backward compatibility
    // but delegate to the new methods to maintain consistent behavior
    @FXML
    public void SwitchTohelloview(ActionEvent event) throws IOException {
        switchToHelloView(event);
    }

    @FXML
    public void SwitchTohoivienfullinfo(ActionEvent event) throws IOException {
        switchToHoiVienFullInfo(event);
    }

    @FXML
    public void SwitchTolisthoivien(ActionEvent event) throws IOException {
        switchToListHoiVien(event);
    }

    @FXML
    public void SwitchToregister(ActionEvent event) throws IOException {
        switchToRegister(event);
    }

    @FXML
    public void SwitchTothiet_bi(ActionEvent event) throws IOException {
        switchToThietBi(event);
    }

    @FXML
    public void SwitchTothietbi_info_full(ActionEvent event) throws IOException {
        switchToThietBiInfoFull(event);
    }

    @FXML
    public void SwitchTothietbi_regis(ActionEvent event) throws IOException {
        switchToThietBiRegis(event);
    }

    @FXML
    public void SwitchTothongke(ActionEvent event) throws IOException {
        switchToThongKe(event);
    }

    private void setupScene(String fxmlFile, ActionEvent event) throws IOException {
        scenceController.setupScene(fxmlFile, event);
    }
}