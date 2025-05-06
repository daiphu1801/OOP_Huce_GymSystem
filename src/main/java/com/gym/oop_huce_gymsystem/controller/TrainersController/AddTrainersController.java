package com.gym.oop_huce_gymsystem.controller.TrainersController;

import com.gym.oop_huce_gymsystem.ScenceController;
import com.gym.oop_huce_gymsystem.model.Trainers;
import com.gym.oop_huce_gymsystem.service.TrainersService;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class AddTrainersController {

    private final ScenceController scenceController = new ScenceController();
    private TrainersService trainersService;

    @FXML
    private TextField nameTrainerField;
    @FXML
    private ComboBox<String> genderComboBox;
    @FXML
    private TextField soDienThoaiField;
    @FXML
    private TextField emailField;
    @FXML
    private TextField specField;

    @FXML
    public void initialize() {
        trainersService = new TrainersService();

        // Khởi tạo ComboBox với các giá trị giới tính
        genderComboBox.setItems(FXCollections.observableArrayList("Nam", "Nữ", "Khác"));
        genderComboBox.setValue("Nam"); // Giá trị mặc định
    }

    @FXML
    private void handleSubmit(ActionEvent event) {
        try {
            // Lấy giá trị từ các trường
            String name = nameTrainerField.getText().trim();
            String gender = genderComboBox.getValue();
            String phone = soDienThoaiField.getText().trim();
            String email = emailField.getText().trim();
            String specialization = specField.getText().trim();

            // Tạo đối tượng Trainers
            Trainers trainer = new Trainers(name, gender, phone, email, specialization);

            // Thêm huấn luyện viên
            trainersService.addTrainers(trainer);

            // Hiển thị thông báo thành công và quay lại danh sách
            showAlert(Alert.AlertType.INFORMATION, "Thành công", "Thêm huấn luyện viên thành công!");
            SwitchTotrainerList(event);

        } catch (IllegalArgumentException e) {
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Dữ liệu không hợp lệ: " + e.getMessage());
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Lỗi khi thêm huấn luyện viên: " + e.getMessage());
        }
    }

    // Helper method to load FXML files
    private void loadFXML(String fxmlFile) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/gym/oop_huce_gymsystem/fxml/" + fxmlFile));
        Parent root = loader.load();
        Stage stage = (Stage) nameTrainerField.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
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
    public void SwitchToPT_Regis(ActionEvent event) throws IOException {
        scenceController.SwitchToPT_Regis(event);
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
    public void SwitchtoProductRegis(ActionEvent event) throws IOException {
        scenceController.SwitchtoProductRegis(event);
    }
}