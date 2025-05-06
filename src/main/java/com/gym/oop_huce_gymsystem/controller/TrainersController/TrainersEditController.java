package com.gym.oop_huce_gymsystem.controller.TrainersController;

import com.gym.oop_huce_gymsystem.ScenceController;
import com.gym.oop_huce_gymsystem.model.Trainers;
import com.gym.oop_huce_gymsystem.service.TrainersService;
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

public class TrainersEditController implements Initializable {

    @FXML private TextField nameField;
    @FXML private ComboBox<String> genderComboBox;
    @FXML private TextField phoneField;
    @FXML private TextField emailField;
    @FXML private TextField specializationField;
    @FXML private Button backButton;
    @FXML private Button saveButton;

    private final ScenceController scenceController;
    private final TrainersService trainersService;
    private int trainerId;
    private boolean isInitialized;

    public TrainersEditController() {
        this.scenceController = new ScenceController();
        this.trainersService = new TrainersService();
        this.isInitialized = false;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (nameField == null) {
            System.out.println("[TrainerEditController] Lỗi: nameField không được inject từ FXML.");
        } else {
            System.out.println("[TrainerEditController] nameField đã được inject thành công.");
        }

        isInitialized = true;
        if (trainerId != 0) {
            loadTrainerData();
        }
    }

    public void setTrainerId(int trainerId) {
        this.trainerId = trainerId;
        if (isInitialized && nameField != null) {
            loadTrainerData();
        } else {
            System.out.println("[TrainerEditController] Chờ initialize hoàn tất để load dữ liệu với trainerId: " + trainerId);
        }
    }

    private void loadTrainerData() {
        try {
            Trainers trainer = trainersService.getTrainerById(trainerId);
            if (trainer != null) {
                nameField.setText(trainer.getName() != null ? trainer.getName() : "");
                genderComboBox.setValue(trainer.getGender() != null ? trainer.getGender() : ""); // Set giá trị cho ComboBox
                phoneField.setText(trainer.getPhone() != null ? trainer.getPhone() : "");
                emailField.setText(trainer.getEmail() != null ? trainer.getEmail() : "");
                specializationField.setText(trainer.getSpecialization() != null ? trainer.getSpecialization() : "");
            } else {
                showAlert(Alert.AlertType.WARNING, "Cảnh báo", "Không tìm thấy huấn luyện viên với ID: " + trainerId);
                clearFields();
            }
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Lỗi khi lấy dữ liệu huấn luyện viên: " + e.getMessage());
            e.printStackTrace();
            clearFields();
        }
    }

    private void clearFields() {
        nameField.setText("");
        genderComboBox.setValue("");
        phoneField.setText("");
        emailField.setText("");
        specializationField.setText("");
    }

    @FXML
    private void saveChanges(ActionEvent event) {
        try {
            // Thu thập dữ liệu từ TextField và ComboBox
            String name = nameField.getText().trim();
            String gender = genderComboBox.getValue() != null ? genderComboBox.getValue() : ""; // Lấy giá trị từ ComboBox
            String phone = phoneField.getText().trim();
            String email = emailField.getText().trim();
            String specialization = specializationField.getText().trim();

            // Tạo đối tượng Trainers
            Trainers updatedTrainer = new Trainers(
                    trainerId,
                    name,
                    gender,
                    phone,
                    email,
                    specialization
            );

            // Gọi service để lưu
            trainersService.updateTrainers(updatedTrainer);
            showAlert(Alert.AlertType.INFORMATION, "Thành công", "Cập nhật huấn luyện viên thành công!");
            scenceController.SwitchTotrainerList(event); // Quay lại danh sách huấn luyện viên
        } catch (IllegalArgumentException e) {
            showAlert(Alert.AlertType.WARNING, "Cảnh báo", e.getMessage());
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Lỗi khi lưu huấn luyện viên: " + e.getMessage());
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