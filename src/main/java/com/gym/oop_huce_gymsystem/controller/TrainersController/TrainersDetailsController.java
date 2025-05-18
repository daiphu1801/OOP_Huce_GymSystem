package com.gym.oop_huce_gymsystem.controller.TrainersController;

import com.gym.oop_huce_gymsystem.ScenceController;
import com.gym.oop_huce_gymsystem.model.Trainers;
import com.gym.oop_huce_gymsystem.service.TrainersService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class TrainersDetailsController implements Initializable {

    @FXML private Label trainerIdLabel;
    @FXML private Label nameLabel;
    @FXML private Label genderLabel;
    @FXML private Label phoneLabel;
    @FXML private Label emailLabel;
    @FXML private Label specializationLabel;

    private final ScenceController scenceController = new ScenceController();
    private final TrainersService trainersService = new TrainersService();
    private String trainerId; // ID của huấn luyện viên được truyền vào

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Phương thức này sẽ được gọi sau khi setTrainerId
    }

    // Phương thức để thiết lập trainerId và tải dữ liệu
    public void setTrainerId(String trainerId) {
        this.trainerId = trainerId;
        loadTrainerData();
    }

    private void loadTrainerData() {
        try {
            Trainers trainer = trainersService.getTrainerById(trainerId);
            if (trainer == null) {
                showAlert(Alert.AlertType.ERROR, "Lỗi", "Không tìm thấy huấn luyện viên với ID: " + trainerId);
                return;
            }

            // Hiển thị thông tin lên các Label
            trainerIdLabel.setText(String.valueOf(trainer.getTrainerId()));
            nameLabel.setText(trainer.getName());
            genderLabel.setText(trainer.getGender());
            phoneLabel.setText(trainer.getPhone());
            emailLabel.setText(trainer.getEmail());
            specializationLabel.setText(trainer.getSpecialization());

        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Không tải được dữ liệu: " + e.getMessage());
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
    public void SwitchTotrainerList(ActionEvent event) throws IOException {
        scenceController.SwitchTotrainerList(event);
    }

    @FXML
    public void SwitchTothiet_bi(ActionEvent event) throws IOException {
        scenceController.switchToThietBi(event);
    }

    @FXML
    public void SwitchtoProduct(ActionEvent event) throws IOException {
        scenceController.SwitchtoProduct(event);
    }

    @FXML
    public void SwitchTothongke(ActionEvent event) throws IOException {
        scenceController.switchToThongKe(event);
    }

    @FXML
    public void switchToMemberCard(ActionEvent event) throws IOException {
        scenceController.switchToMemberCardList(event);
    }

    @FXML
    public void switchToEditTrainer(ActionEvent event) throws IOException {
        // Giả định chuyển đến giao diện chỉnh sửa huấn luyện viên, truyền trainerId
        scenceController.switchToEditTrainer(event, trainerId);
    }
}