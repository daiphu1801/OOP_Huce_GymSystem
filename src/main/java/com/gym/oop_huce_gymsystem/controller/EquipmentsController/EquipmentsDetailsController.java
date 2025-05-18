package com.gym.oop_huce_gymsystem.controller.EquipmentsController;

import com.gym.oop_huce_gymsystem.ScenceController;
import com.gym.oop_huce_gymsystem.model.Equipments;
import com.gym.oop_huce_gymsystem.service.EquipmentsService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class EquipmentsDetailsController implements Initializable {

    @FXML private Label equipmentIdLabel;
    @FXML private Label nameLabel;
    @FXML private Label quantityLabel;
    @FXML private Label statusLabel;
    @FXML private Label purchaseDateLabel;
    @FXML private Button backButton;
    @FXML private Button editInfoButton;

    private final ScenceController scenceController;
    private final EquipmentsService equipmentsService;
    private String equipmentId;
    private boolean isInitialized;

    public EquipmentsDetailsController() {
        this.scenceController = new ScenceController();
        this.equipmentsService = new EquipmentsService();
        this.isInitialized = false;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (equipmentIdLabel == null) {
            System.out.println("[EquipmentDetailsController] Lỗi: equipmentIdLabel không được inject từ FXML.");
        } else {
            System.out.println("[EquipmentDetailsController] equipmentIdLabel đã được inject thành công.");
        }

        isInitialized = true;
        if (equipmentId != null) {
            loadEquipmentData();
        }
    }

    public void setEquipmentId(String equipmentId) {
        this.equipmentId = equipmentId;
        if (isInitialized && equipmentIdLabel != null) {
            loadEquipmentData();
        } else {
            System.out.println("[EquipmentDetailsController] Chờ initialize hoàn tất để load dữ liệu với equipmentId: " + equipmentId);
        }
    }

    private void loadEquipmentData() {
        try {
            Equipments equipment = equipmentsService.getEquipmentById(equipmentId);
            if (equipment != null) {
                equipmentIdLabel.setText(String.valueOf(equipment.getEquipmentId()));
                nameLabel.setText(equipment.getName() != null ? equipment.getName() : "");
                quantityLabel.setText(String.valueOf(equipment.getQuantity()));
                statusLabel.setText(equipment.getStatus() != null ? equipment.getStatus() : "");
                purchaseDateLabel.setText(equipment.getPurchase_Date() != null ? equipment.getPurchase_Date().toString() : "");
            } else {
                showAlert(Alert.AlertType.WARNING, "Cảnh báo", "Không tìm thấy thiết bị với ID: " + equipmentId);
                clearFields();
            }
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Lỗi khi lấy dữ liệu thiết bị: " + e.getMessage());
            e.printStackTrace();
            clearFields();
        }
    }

    private void clearFields() {
        equipmentIdLabel.setText("");
        nameLabel.setText("");
        quantityLabel.setText("");
        statusLabel.setText("");
        purchaseDateLabel.setText("");
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
    private void switchToEditEquipment(ActionEvent event) throws IOException {
        scenceController.switchToEquipmentEdit(event, String.valueOf(equipmentId));
    }
}