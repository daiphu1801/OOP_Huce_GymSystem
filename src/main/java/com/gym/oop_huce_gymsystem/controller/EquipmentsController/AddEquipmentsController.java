package com.gym.oop_huce_gymsystem.controller.EquipmentsController;

import com.gym.oop_huce_gymsystem.ScenceController;
import com.gym.oop_huce_gymsystem.model.Equipments;
import com.gym.oop_huce_gymsystem.service.EquipmentsService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class AddEquipmentsController implements Initializable {

    @FXML private TextField nameField;
    @FXML private ComboBox<String> statusComboBox;
    @FXML private TextField quantityField;
    @FXML private DatePicker purchaseDatePicker;
    @FXML private Button backButton;
    @FXML private Button saveChanges;

    private final ScenceController scenceController;
    private final EquipmentsService equipmentsService;

    public AddEquipmentsController() {
        this.scenceController = new ScenceController();
        this.equipmentsService = new EquipmentsService();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (nameField == null || statusComboBox == null || quantityField == null || purchaseDatePicker == null) {
            System.out.println("[AddEquipmentsController] Lỗi: Một hoặc nhiều thành phần không được inject từ FXML.");
        } else {
            System.out.println("[AddEquipmentsController] Các thành phần đã được inject thành công.");
        }
        statusComboBox.setValue("Chọn trạng thái"); // Hiển thị mặc định
    }

    @FXML
    private void saveChanges(ActionEvent event) {
        try {
            String name = nameField.getText().trim();
            String status = statusComboBox.getValue() != null ? statusComboBox.getValue() : "";
            String quantityStr = quantityField.getText().trim();
            LocalDate purchaseDate = purchaseDatePicker.getValue();

            if (name.isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "Cảnh báo", "Tên thiết bị không được để trống.");
                return;
            }
            if (status.isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "Cảnh báo", "Trạng thái không được để trống.");
                return;
            }
            int quantity;
            try {
                quantity = Integer.parseInt(quantityStr);
                if (quantity <= 0) {
                    showAlert(Alert.AlertType.WARNING, "Cảnh báo", "Số lượng phải lớn hơn 0.");
                    return;
                }
            } catch (NumberFormatException e) {
                showAlert(Alert.AlertType.WARNING, "Cảnh báo", "Số lượng phải là một số nguyên hợp lệ.");
                return;
            }
            if (purchaseDate == null) {
                showAlert(Alert.AlertType.WARNING, "Cảnh báo", "Ngày nhập không được để trống.");
                return;
            }

            Equipments newEquipment = new Equipments(name, quantity, status, purchaseDate);
            equipmentsService.addEquipment(newEquipment);
            showAlert(Alert.AlertType.INFORMATION, "Thành công", "Thêm thiết bị thành công!");
            clearFields();
            scenceController.SwitchTothiet_bi(event); // Quay lại danh sách thiết bị
        } catch (IllegalArgumentException e) {
            showAlert(Alert.AlertType.WARNING, "Cảnh báo", e.getMessage());
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Lỗi khi thêm thiết bị: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void SwitchTothiet_bi(ActionEvent event) throws IOException {
        scenceController.switchToThietBi(event);
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
    public void SwitchTothongke(ActionEvent event) throws IOException {
        scenceController.switchToThongKe(event);
    }

    private void clearFields() {
        nameField.setText("");
        statusComboBox.setValue("");
        quantityField.setText("");
        purchaseDatePicker.setValue(null);
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}