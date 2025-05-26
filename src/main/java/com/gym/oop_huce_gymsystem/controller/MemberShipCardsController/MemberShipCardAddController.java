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
        memberShipCardsService = new MemberShipCardsService(new com.gym.oop_huce_gymsystem.dao.MemberShipCardsDao());
        loaiTheComboBox.setItems(FXCollections.observableArrayList("Standard", "Premium", "VIP"));
        loaiTheComboBox.setPromptText("Chọn loại thẻ");
        trainingPackageComboBox.setItems(FXCollections.observableArrayList("1 tháng", "3 tháng", "6 tháng", "12 tháng", "Khác..."));
        trainingPackageComboBox.setPromptText("Chọn gói tập");
    }

    @FXML
    private void handleSubmit(ActionEvent event) {
        try {
            String priceStr = priceCard.getText();
            String cardType = loaiTheComboBox.getValue();
            String trainingPackage = trainingPackageComboBox.getValue();
            LocalDate registrationDate = regisDatePicker.getValue();

            if (priceStr.isEmpty() || cardType == null || trainingPackage == null || registrationDate == null) {
                showAlert(Alert.AlertType.ERROR, "Lỗi", "Vui lòng điền đầy đủ thông tin!");
                return;
            }

            double price = Double.parseDouble(priceStr.replace(",", "").replace("đ", ""));
            LocalDate expiryDate = calculateExpiryDate(registrationDate, trainingPackage);
            if (expiryDate == null) {
                showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể tính ngày hết hạn cho gói tập này.");
                return;
            }

            MemberShipCards card = new MemberShipCards(price, trainingPackage, cardType, registrationDate, expiryDate);
            String cardId = memberShipCardsService.addMemberShipCard(card);
            if (cardId == null) {
                throw new IllegalArgumentException("Không thể tạo mã thẻ thành viên!");
            }

            showAlert(Alert.AlertType.INFORMATION, "Thành công", "Thêm thẻ thành viên thành công!");
            scenceController.switchToRegister(event, cardId);

        } catch (IllegalArgumentException e) {
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Lỗi khi thêm thẻ thành viên: " + e.getMessage());
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Lỗi hệ thống: " + e.getMessage());
        }
    }

    private LocalDate calculateExpiryDate(LocalDate registrationDate, String trainingPackage) {
        switch (trainingPackage) {
            case "1 tháng": return registrationDate.plusMonths(1);
            case "3 tháng": return registrationDate.plusMonths(3);
            case "6 tháng": return registrationDate.plusMonths(6);
            case "12 tháng": return registrationDate.plusMonths(12);
            case "Khác...": return registrationDate.plusYears(2);
            default: return null;
        }
    }

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
    public void SwitchToregister(ActionEvent event,String CardId) throws IOException {
        scenceController.switchToRegister(event, CardId);
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