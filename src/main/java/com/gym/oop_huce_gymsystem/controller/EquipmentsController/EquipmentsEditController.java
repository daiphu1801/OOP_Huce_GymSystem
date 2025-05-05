package com.gym.oop_huce_gymsystem.controller.EquipmentsController;

import com.gym.oop_huce_gymsystem.model.Equipments;
import com.gym.oop_huce_gymsystem.ScenceController;
import com.gym.oop_huce_gymsystem.service.EquipmentsService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class EquipmentsEditController implements Initializable {

    @FXML private TextField equipmentIdField;
    @FXML private TextField nameField;
    @FXML private TextField quantityField;
    @FXML private ComboBox<String> statusComboBox;
    @FXML private DatePicker purchaseDatePicker;
    @FXML private Button backButton;
    @FXML private Button saveChanges;

    private final ScenceController scenceController;
    private final EquipmentsService equipmentsService;
    private int equipmentId;
    private boolean isInitialized;

    public EquipmentsEditController() {
        this.scenceController = new ScenceController();
        this.equipmentsService = new EquipmentsService();
        this.isInitialized = false;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (equipmentIdField == null) {
            System.out.println("[EquipmentsEditController] Lỗi: equipmentIdField không được inject từ FXML.");
        } else {
            System.out.println("[EquipmentsEditController] equipmentIdField đã được inject thành công.");
        }

        isInitialized = true;
        if (equipmentId != 0) {
            loadEquipmentData();
        }
    }

    public void setEquipmentId(int equipmentId) {
        this.equipmentId = equipmentId;
        if (isInitialized && equipmentIdField != null) {
            loadEquipmentData();
        } else {
            System.out.println("[EquipmentsEditController] Chờ initialize hoàn tất để load dữ liệu với equipmentId: " + equipmentId);
        }
    }

    private void loadEquipmentData() {
        try {
            Equipments equipment = equipmentsService.getEquipmentById(equipmentId);
            if (equipment != null) {
                equipmentIdField.setText(String.valueOf(equipment.getEquipmentId()));
                nameField.setText(equipment.getName() != null ? equipment.getName() : "");
                quantityField.setText(String.valueOf(equipment.getQuantity()));
                statusComboBox.setValue(equipment.getStatus() != null ? equipment.getStatus() : "");
                purchaseDatePicker.setValue(equipment.getPurchase_Date());
            } else {
                showAlert(Alert.AlertType.WARNING, "Cảnh báo", "Không tìm thấy thiết bị với ID: " + equipmentId);
                clearFields();
            }
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Lỗi khi lấy dữ liệu thiết bị: " + e.getMessage());
            e.printStackTrace();
            clearFields();
        }
    }

    private void clearFields() {
        equipmentIdField.setText("");
        nameField.setText("");
        quantityField.setText("");
        statusComboBox.setValue(null);
        purchaseDatePicker.setValue(null);
    }

    @FXML
    private void saveChanges(ActionEvent event) {
        try {
            // Thu thập dữ liệu từ TextField, ComboBox và DatePicker
            String name = nameField.getText().trim();
            String quantityStr = quantityField.getText().trim();
            String status = statusComboBox.getValue();
            LocalDate purchaseDate = purchaseDatePicker.getValue();

            // Kiểm tra dữ liệu
            if (name.isEmpty()) {
                throw new IllegalArgumentException("Tên thiết bị không được để trống.");
            }

            // Chuyển đổi quantity
            int quantity = Integer.parseInt(quantityStr);
            if (quantity <= 0) {
                throw new IllegalArgumentException("Số lượng phải lớn hơn 0.");
            }

            // Kiểm tra status
            if (status == null || status.isEmpty()) {
                throw new IllegalArgumentException("Tình trạng không được để trống.");
            }

            // Kiểm tra purchaseDate
            if (purchaseDate == null) {
                throw new IllegalArgumentException("Ngày nhập không được để trống.");
            }

            // Tạo đối tượng Equipments
            Equipments updatedEquipment = new Equipments(
                    equipmentId,
                    name,
                    quantity,
                    status,
                    purchaseDate
            );

            // Gọi service để lưu
            equipmentsService.updateEquipment(updatedEquipment);
            showAlert(Alert.AlertType.INFORMATION, "Thành công", "Cập nhật thiết bị thành công!");
            scenceController.switchToThietBi(event); // Quay lại danh sách thiết bị
        } catch (IllegalArgumentException e) {
            showAlert(Alert.AlertType.WARNING, "Cảnh báo", e.getMessage());
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Lỗi khi lưu thiết bị: " + e.getMessage());
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
    public void switchToProductRegis(ActionEvent event) throws IOException {
        scenceController.SwitchtoProductRegis(event);
    }

    @FXML
    public void switchToPTRegis(ActionEvent event) throws IOException {
        scenceController.SwitchToPT_Regis(event);
    }
}