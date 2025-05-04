package com.gym.oop_huce_gymsystem.controller.MemberShipCardsController;

import com.gym.oop_huce_gymsystem.ScenceController;
import com.gym.oop_huce_gymsystem.model.MemberShipCards;
import com.gym.oop_huce_gymsystem.service.MemberShipCardsService;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class MemberShipCardEditController implements Initializable {

    @FXML private TextField priceField;
    @FXML private ComboBox<String> trainingPackageComboBox;
    @FXML private ComboBox<String> cardTypeComboBox;
    @FXML private DatePicker registrationDateField;
    @FXML private DatePicker expiryDateField;
    @FXML private Button backButton;
    @FXML private Button saveButton;

    private final ScenceController scenceController;
    private final MemberShipCardsService memberShipCardsService;
    private String cardId;
    private boolean isInitialized;

    public MemberShipCardEditController() {
        this.scenceController = new ScenceController();
        this.memberShipCardsService = new MemberShipCardsService(new com.gym.oop_huce_gymsystem.dao.MemberShipCardsDao());
        this.isInitialized = false;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (priceField == null || trainingPackageComboBox == null || cardTypeComboBox == null ||
                registrationDateField == null || expiryDateField == null) {
            System.out.println("[MemberShipCardEditController] Lỗi: Một hoặc nhiều trường không được inject từ FXML.");
        } else {
            System.out.println("[MemberShipCardEditController] Các trường đã được inject thành công.");
        }

        // Điền giá trị mặc định cho ComboBox
        trainingPackageComboBox.setItems(FXCollections.observableArrayList("1 tháng", "3 tháng", "6 tháng", "12 tháng", "Khác..."));
        cardTypeComboBox.setItems(FXCollections.observableArrayList("Standard", "Premium", "VIP"));

        isInitialized = true;
        if (cardId != null && !cardId.isEmpty()) {
            loadCardData();
        }
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
        if (isInitialized && priceField != null) {
            loadCardData();
        } else {
            System.out.println("[MemberShipCardEditController] Chờ initialize hoàn tất để load dữ liệu với cardId: " + cardId);
        }
    }

    private void loadCardData() {
        try {
            MemberShipCards card = memberShipCardsService.getMemberShipCardById(cardId);
            if (card != null) {
                priceField.setText(String.valueOf(card.getPrice()));
                trainingPackageComboBox.setValue(card.getTrainingPackage());
                cardTypeComboBox.setValue(card.getCardType());
                registrationDateField.setValue(card.getRegistrationDate());
                expiryDateField.setValue(card.getExpiryDate());
            } else {
                showAlert(Alert.AlertType.WARNING, "Cảnh báo", "Không tìm thấy thẻ với ID: " + cardId);
                clearFields();
            }
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Lỗi khi lấy dữ liệu thẻ: " + e.getMessage());
            e.printStackTrace();
            clearFields();
        }
    }

    private void clearFields() {
        priceField.setText("");
        trainingPackageComboBox.setValue(null);
        cardTypeComboBox.setValue(null);
        registrationDateField.setValue(null);
        expiryDateField.setValue(null);
    }

    @FXML
    private void saveChanges(ActionEvent event) {
        try {
            // Thu thập dữ liệu từ các trường
            String priceStr = priceField.getText().trim();
            String trainingPackage = trainingPackageComboBox.getValue();
            String cardType = cardTypeComboBox.getValue();
            LocalDate regDate = registrationDateField.getValue();
            LocalDate expDate = expiryDateField.getValue();

            // Kiểm tra cơ bản để tránh dữ liệu rỗng
            if (priceStr.isEmpty() || trainingPackage == null || cardType == null ||
                    regDate == null || expDate == null) {
                showAlert(Alert.AlertType.WARNING, "Cảnh báo", "Tất cả các trường phải được điền.");
                return;
            }

            // Chuyển đổi giá và kiểm tra
            double price;
            try {
                price = Double.parseDouble(priceStr);
                if (price <= 0) throw new NumberFormatException();
            } catch (NumberFormatException e) {
                showAlert(Alert.AlertType.WARNING, "Cảnh báo", "Giá thẻ phải là một số dương hợp lệ.");
                return;
            }

            // Kiểm tra ngày hợp lệ
            if (expDate.isBefore(regDate)) {
                showAlert(Alert.AlertType.WARNING, "Cảnh báo", "Ngày hết hạn phải sau ngày đăng ký.");
                return;
            }

            // Tạo đối tượng MemberShipCards
            MemberShipCards updatedCard = new MemberShipCards(cardId, price, trainingPackage, cardType, regDate, expDate);

            // Gọi service để lưu
            memberShipCardsService.updateMemberShipCard(updatedCard);
            showAlert(Alert.AlertType.INFORMATION, "Thành công", "Cập nhật thẻ thành công!");
            scenceController.switchToMemberCardDetail(event, cardId); // Quay lại chi tiết thẻ
        } catch (IllegalArgumentException e) {
            showAlert(Alert.AlertType.WARNING, "Cảnh báo", e.getMessage());
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Lỗi khi lưu thẻ: " + e.getMessage());
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

    @FXML
    public void switchHome(javafx.scene.input.MouseEvent event) throws IOException {
        ActionEvent actionEvent = new ActionEvent(event.getSource(), event.getTarget());
        scenceController.switchToHelloView(actionEvent);
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

    @FXML
    public void SwitchToCardAdd(ActionEvent event) throws IOException {
        scenceController.SwitchToCardAdd(event);
    }
}