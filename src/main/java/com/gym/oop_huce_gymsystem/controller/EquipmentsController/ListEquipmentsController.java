package com.gym.oop_huce_gymsystem.controller.EquipmentsController;

import com.gym.oop_huce_gymsystem.ScenceController;
import com.gym.oop_huce_gymsystem.model.Equipments;
import com.gym.oop_huce_gymsystem.service.EquipmentsService;
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

public class ListEquipmentsController implements Initializable {

    @FXML private TableView<Equipments> equipmentTable;
    @FXML private TableColumn<Equipments, Integer> equipmentIdColumn;
    @FXML private TableColumn<Equipments, String> nameColumn;
    @FXML private TableColumn<Equipments, Integer> quantityColumn;
    @FXML private TableColumn<Equipments, String> statusColumn;

    @FXML private TextField searchField;
    @FXML private Button addEquipment;
    @FXML private Button deleteEquipment;

    private final EquipmentsService equipmentsService;
    private ObservableList<Equipments> equipmentList;
    private final ScenceController scenceController;

    public ListEquipmentsController() {
        this.equipmentsService = new EquipmentsService();
        this.scenceController = new ScenceController();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Thiết lập kích thước tối thiểu cho các cột
        equipmentIdColumn.setMinWidth(100);
        nameColumn.setMinWidth(190);
        quantityColumn.setMinWidth(100);
        statusColumn.setMinWidth(200);

        // Thiết lập ánh xạ cột với thuộc tính của Equipments
        equipmentIdColumn.setCellValueFactory(new PropertyValueFactory<>("equipmentId"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        // Thiết lập cell factory với text rõ ràng
        equipmentIdColumn.setCellFactory(column -> createStringTableCell());
        nameColumn.setCellFactory(column -> createStringTableCell());
        quantityColumn.setCellFactory(column -> createStringTableCell());
        statusColumn.setCellFactory(column -> createStringTableCell());

        // Tải dữ liệu ban đầu
        loadEquipmentData();

        // Thiết lập tính năng tìm kiếm
        searchField.textProperty().addListener((obs, oldVal, newVal) -> filterEquipments(newVal));

        // Đảm bảo bảng được style đúng
        equipmentTable.getStyleClass().add("memberTable");
    }


    @FXML
    private void handleDeleteEquipment(ActionEvent event) {
        Equipments selected = equipmentTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "Chưa chọn thiết bị", "Vui lòng chọn thiết bị cần xóa.");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Xác nhận xóa");
        confirm.setHeaderText(null);
        confirm.setContentText("Bạn có chắc muốn xóa thiết bị " + selected.getName() + " (ID: " + selected.getEquipmentId() + ")?");
        confirm.showAndWait().ifPresent(resp -> {
            if (resp == ButtonType.OK) {
                try {
                    equipmentsService.deleteEquipment(selected.getEquipmentId());
                    equipmentList.remove(selected);
                    equipmentTable.refresh();
                    showAlert(Alert.AlertType.INFORMATION, "Thành công", "Đã xóa thiết bị.");
                } catch (Exception e) {
                    showAlert(Alert.AlertType.ERROR, "Lỗi", "Xóa thất bại: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        });
    }



    @FXML
    public void handleDoubleClick(MouseEvent event) {
        if (event.getClickCount() == 2) {
            Equipments selected = equipmentTable.getSelectionModel().getSelectedItem();
            if (selected == null) {
                System.out.println("[ListEquipmentsController] Lỗi: Không có thiết bị nào được chọn để xem chi tiết.");
                showAlert(Alert.AlertType.WARNING, "Chưa chọn thiết bị", "Vui lòng chọn thiết bị để xem thông tin chi tiết.");
                return;
            }

            System.out.println("[ListEquipmentsController] Nhấp đúp vào thiết bị: " + selected.getName() + ", ID: " + selected.getEquipmentId());
            try {
                scenceController.switchToEquipmentDetails(new ActionEvent(event.getSource(), null), selected.getEquipmentId());
                System.out.println("[ListEquipmentsController] Đã chuyển sang chi tiết thiết bị thành công.");
            } catch (Exception e) {
                System.out.println("[ListEquipmentsController] Lỗi khi chuyển sang chi tiết thiết bị: " + e.getMessage());
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể mở giao diện chi tiết: " + e.getMessage());
            }
        }
    }

    // Helper method để tạo TableCell với style rõ ràng
    private <T> TableCell<Equipments, T> createStringTableCell() {
        return new TableCell<Equipments, T>() {
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

    private void loadEquipmentData() {
        try {
            List<Equipments> equipments = equipmentsService.getAllEquipments();
            equipmentList = FXCollections.observableArrayList(equipments);

            if (equipments.isEmpty()) {
                System.out.println("Danh sách thiết bị trống!");
            } else {
                System.out.println("Đã tải " + equipments.size() + " thiết bị.");
                for (Equipments equipment : equipments) {
                    System.out.println("Thiết bị: " + equipment.getEquipmentId() + ", " + equipment.getName() + ", " +
                            equipment.getQuantity() + ", " + equipment.getStatus());
                }
            }

            Platform.runLater(() -> {
                equipmentTable.setItems(equipmentList);
                equipmentTable.refresh();
                System.out.println("Số dòng trong bảng: " + equipmentTable.getItems().size());
            });

        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Không tải được dữ liệu: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void filterEquipments(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            equipmentTable.setItems(equipmentList);
        } else {
            String lowerKeyword = keyword.trim().toLowerCase();
            ObservableList<Equipments> filtered = equipmentList.filtered(e ->
                    e.getName() != null && e.getName().toLowerCase().contains(lowerKeyword) ||
                            String.valueOf(e.getEquipmentId()).contains(lowerKeyword)
            );
            equipmentTable.setItems(filtered);
        }
        equipmentTable.refresh();
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    private void SwitchTothietbi_regis(ActionEvent event) throws IOException {
        scenceController.SwitchTothietbi_regis(event);
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