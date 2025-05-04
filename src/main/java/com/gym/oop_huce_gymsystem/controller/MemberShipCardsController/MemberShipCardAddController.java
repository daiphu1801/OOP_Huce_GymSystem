package com.gym.oop_huce_gymsystem.controller.MemberShipCardsController;

import com.gym.oop_huce_gymsystem.ScenceController;
import com.gym.oop_huce_gymsystem.model.MemberShipCards;
import com.gym.oop_huce_gymsystem.service.MemberShipCardsService;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import java.io.IOException;
import java.time.LocalDate;

public class MemberShipCardAddController {

    private final ScenceController scenceController = new ScenceController();
    private MemberShipCardsService memberShipCardsService;

    @FXML
    private TextField priceCard;
    @FXML
    private ComboBox<String> loaiTheComboBox;
    @FXML
    private ComboBox<String> trainingPackageComboBox;
    @FXML
    private DatePicker regisDatePicker;

    @FXML
    public void initialize() {
        // Khởi tạo service
        memberShipCardsService = new MemberShipCardsService(new com.gym.oop_huce_gymsystem.dao.MemberShipCardsDao());

        // Điền dữ liệu cho ComboBox cardType
        loaiTheComboBox.setItems(FXCollections.observableArrayList("Standard", "Premium", "VIP"));
        loaiTheComboBox.setPromptText("Chọn loại thẻ");

        // Điền dữ liệu cho ComboBox trainingPackage
        trainingPackageComboBox.setItems(FXCollections.observableArrayList("1 tháng", "3 tháng", "6 tháng", "12 tháng", "Khác..."));
        trainingPackageComboBox.setPromptText("Chọn gói tập");
    }

    @FXML
    private void handleSubmit(ActionEvent event) {
        try {
            // Lấy giá trị từ các trường nhập liệu
            String priceStr = priceCard.getText();
            String cardType = loaiTheComboBox.getValue();
            String trainingPackage = trainingPackageComboBox.getValue();
            LocalDate registrationDate = regisDatePicker.getValue();

            // Kiểm tra các trường bắt buộc
            if (priceStr.isEmpty() || cardType == null || trainingPackage == null || registrationDate == null) {
                showAlert(Alert.AlertType.ERROR, "Lỗi", "Vui lòng điền đầy đủ thông tin!");
                return;
            }

            // Parse giá thẻ
            double price;
            try {
                price = Double.parseDouble(priceStr.replace(",", "").replace("đ", ""));
            } catch (NumberFormatException e) {
                showAlert(Alert.AlertType.ERROR, "Lỗi", "Giá thẻ phải là số hợp lệ.");
                return;
            }

            // Tính ngày hết hạn dựa trên trainingPackage
            LocalDate expiryDate = calculateExpiryDate(registrationDate, trainingPackage);
            if (expiryDate == null) {
                showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể tính ngày hết hạn cho gói tập này.");
                return;
            }

            // Tạo đối tượng MemberShipCards (không cần cardId)
            MemberShipCards card = new MemberShipCards(null, price, trainingPackage, cardType, registrationDate, expiryDate);

            // Lưu thẻ thành viên
            memberShipCardsService.addMemberShipCard(card);

            // Hiển thị thông báo thành công và quay lại danh sách thẻ
            showAlert(Alert.AlertType.INFORMATION, "Thành công", "Thêm thẻ thành viên thành công!");
            switchToMemberCard(event);

        } catch (IllegalArgumentException e) {
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Lỗi khi thêm thẻ thành viên: " + e.getMessage());
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Lỗi hệ thống: " + e.getMessage());
        }
    }

    // Hàm tính expiryDate dựa trên trainingPackage
    private LocalDate calculateExpiryDate(LocalDate registrationDate, String trainingPackage) {
        switch (trainingPackage) {
            case "1 tháng":
                return registrationDate.plusMonths(1);
            case "3 tháng":
                return registrationDate.plusMonths(3);
            case "6 tháng":
                return registrationDate.plusMonths(6);
            case "12 tháng":
                return registrationDate.plusMonths(12); // Tương đương 1 năm
            case "Khác...":
                // Giả định gói "Khác..." là 24 tháng, tính theo năm (2 năm)
                return registrationDate.plusYears(2);
            default:
                return null;
        }
    }

    // Helper method to show alerts
    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    public void switchHome(javafx.scene.input.MouseEvent event) throws IOException {
        ActionEvent actionEvent = new ActionEvent(event.getSource(), event.getTarget());
        scenceController.switchToHelloView(actionEvent);
    }

    @FXML
    public void SwitchTohelloview(ActionEvent event) throws IOException {
        scenceController.switchToHelloView(event);
    }

    @FXML
    public void SwitchTolisthoivien(ActionEvent event) throws IOException {
        scenceController.switchToListHoiVien(event);
    }

    @FXML
    public void SwitchToregister(ActionEvent event) throws IOException {
        scenceController.switchToRegister(event);
    }

    @FXML
    public void SwitchTothiet_bi(ActionEvent event) throws IOException {
        scenceController.switchToThietBi(event);
    }

    @FXML
    public void SwitchTothietbi_regis(ActionEvent event) throws IOException {
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
    public void switchToMemberCard(ActionEvent event) throws IOException {
        scenceController.switchToMemberCardList(event);
    }

    @FXML
    public void SwitchToCardAdd(ActionEvent event) throws IOException {
        scenceController.SwitchToCardAdd(event);
    }

    @FXML
    public void SwitchtoProduct(ActionEvent event) throws IOException {
        scenceController.SwitchtoProduct(event);
    }

    @FXML
    public void SwitchtoProductRegis(ActionEvent event) throws IOException {
        scenceController.SwitchtoProductRegis(event);
    }

    @FXML
    public void SwitchToPT_Regis(ActionEvent event) throws IOException {
        scenceController.SwitchToPT_Regis(event);
    }
}