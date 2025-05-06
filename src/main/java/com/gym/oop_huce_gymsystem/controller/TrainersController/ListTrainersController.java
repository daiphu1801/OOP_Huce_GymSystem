package com.gym.oop_huce_gymsystem.controller.TrainersController;

import com.gym.oop_huce_gymsystem.ScenceController;
import com.gym.oop_huce_gymsystem.model.Trainers;
import com.gym.oop_huce_gymsystem.service.TrainersService;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ListTrainersController implements Initializable {

    @FXML private TableView<Trainers> trainerTable;
    @FXML private TableColumn<Trainers, Integer> trainerIdColumn;
    @FXML private TableColumn<Trainers, String> nameColumn;
    @FXML private TableColumn<Trainers, String> genderColumn;
    @FXML private TableColumn<Trainers, String> phoneColumn;
    @FXML private TableColumn<Trainers, String> emailColumn;
    @FXML private TableColumn<Trainers, String> specializationColumn;

    @FXML private TextField searchField;

    private final TrainersService trainersService;
    private ObservableList<Trainers> trainerList;
    private final ScenceController scenceController;

    public ListTrainersController() {
        this.trainersService = new TrainersService();
        this.scenceController = new ScenceController();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Thiết lập kích thước tối thiểu cho các cột
        trainerIdColumn.setMinWidth(100);
        nameColumn.setMinWidth(190);
        genderColumn.setMinWidth(100);
        phoneColumn.setMinWidth(150);
        emailColumn.setMinWidth(200);
        specializationColumn.setMinWidth(190);

        // Thiết lập ánh xạ cột với thuộc tính của Trainers
        trainerIdColumn.setCellValueFactory(new PropertyValueFactory<>("trainerId"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        genderColumn.setCellValueFactory(new PropertyValueFactory<>("gender"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        specializationColumn.setCellValueFactory(new PropertyValueFactory<>("specialization"));

        // Thiết lập cell factory với text rõ ràng
        trainerIdColumn.setCellFactory(column -> createStringTableCell());
        nameColumn.setCellFactory(column -> createStringTableCell());
        genderColumn.setCellFactory(column -> createStringTableCell());
        phoneColumn.setCellFactory(column -> createStringTableCell());
        emailColumn.setCellFactory(column -> createStringTableCell());
        specializationColumn.setCellFactory(column -> createStringTableCell());

        // Tải dữ liệu ban đầu
        loadTrainerData();

        // Thiết lập tính năng tìm kiếm
        searchField.textProperty().addListener((obs, oldVal, newVal) -> filterTrainers(newVal));

        // Đảm bảo bảng được style đúng
        trainerTable.getStyleClass().add("memberTable");
    }

    @FXML
    public void handleDoubleClick(MouseEvent event) {
        if (event.getClickCount() == 2) {
            Trainers selected = trainerTable.getSelectionModel().getSelectedItem();
            if (selected == null) {
                System.out.println("[ListTrainersController] Lỗi: Không có huấn luyện viên nào được chọn để xem chi tiết.");
                showAlert(Alert.AlertType.WARNING, "Chưa chọn huấn luyện viên", "Vui lòng chọn huấn luyện viên để xem thông tin chi tiết.");
                return;
            }

            System.out.println("[ListTrainersController] Nhấp đúp vào huấn luyện viên: " + selected.getName() + ", ID: " + selected.getTrainerId());
            try {
                 //Giả định chuyển sang chi tiết huấn luyện viên (cần triển khai giao diện chi tiết)
                 scenceController.switchToTrainerDetail(new ActionEvent(event.getSource(), null), selected.getTrainerId());
//                showAlert(Alert.AlertType.INFORMATION, "Thông báo", "Chuyển đến chi tiết huấn luyện viên: " + selected.getName());
                System.out.println("[ListTrainersController] Đã chuyển sang chi tiết huấn luyện viên thành công.");
            } catch (Exception e) {
                System.out.println("[ListTrainersController] Lỗi khi chuyển sang chi tiết huấn luyện viên: " + e.getMessage());
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể mở giao diện chi tiết: " + e.getMessage());
            }
        }
    }

    // Helper method để tạo TableCell với style rõ ràng
    private <T> TableCell<Trainers, T> createStringTableCell() {
        return new TableCell<Trainers, T>() {
            @Override
            protected void updateItem(T item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(item.toString());
                }
            }
        };
    }

    private void loadTrainerData() {
        try {
            List<Trainers> trainers = trainersService.getAllTrainers();
            trainerList = FXCollections.observableArrayList(trainers);

            // Kiểm tra danh sách huấn luyện viên
            if (trainers.isEmpty()) {
                System.out.println("Danh sách huấn luyện viên trống!");
            } else {
                System.out.println("Đã tải " + trainers.size() + " huấn luyện viên.");
                for (Trainers trainer : trainers) {
                    System.out.println("Huấn luyện viên: " + trainer.getTrainerId() + ", " + trainer.getName() + ", " +
                            trainer.getGender() + ", " + trainer.getPhone() + ", " + trainer.getEmail() + ", " + trainer.getSpecialization());
                }
            }

            // Sử dụng Platform.runLater để đảm bảo UI được cập nhật trên thread JavaFX
            Platform.runLater(() -> {
                trainerTable.setItems(trainerList);
                trainerTable.refresh();

                // Debug: Kiểm tra số lượng dòng trong bảng
                System.out.println("Số dòng trong bảng: " + trainerTable.getItems().size());
            });

        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Không tải được dữ liệu: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void filterTrainers(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            trainerTable.setItems(trainerList);
        } else {
            String lowerKeyword = keyword.trim().toLowerCase();
            ObservableList<Trainers> filtered = trainerList.filtered(t ->
                    t.getName() != null && t.getName().toLowerCase().contains(lowerKeyword)
            );
            trainerTable.setItems(filtered);
        }
        trainerTable.refresh();
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    private void handleDeleteTrainer(ActionEvent event) {
        Trainers selected = trainerTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "Chưa chọn huấn luyện viên", "Vui lòng chọn huấn luyện viên cần xóa.");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Xác nhận xóa");
        confirm.setHeaderText(null);
        confirm.setContentText("Bạn có chắc muốn xóa huấn luyện viên " + selected.getName() + " (ID: " + selected.getTrainerId() + ")?");
        confirm.showAndWait().ifPresent(resp -> {
            if (resp == ButtonType.OK) {
                try {
                    trainersService.deleteTrainers(selected.getTrainerId());
                    trainerList.remove(selected);
                    trainerTable.refresh();
                    showAlert(Alert.AlertType.INFORMATION, "Thành công", "Đã xóa huấn luyện viên.");
                } catch (Exception e) {
                    showAlert(Alert.AlertType.ERROR, "Lỗi", "Xóa thất bại: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        });
    }

    @FXML
    public void switchHome(MouseEvent event) throws IOException {
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