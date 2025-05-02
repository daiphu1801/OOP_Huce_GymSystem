package com.gym.oop_huce_gymsystem.controller;

import com.gym.oop_huce_gymsystem.model.Members;
import com.gym.oop_huce_gymsystem.service.MembersService;
import com.gym.oop_huce_gymsystem.ScenceController;
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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;

public class ListHoiVienController implements Initializable {

    @FXML private TableView<Members> memberTable;
    @FXML private TableColumn<Members, Integer> memberIdColumn;
    @FXML private TableColumn<Members, String> fullNameColumn;
    @FXML private TableColumn<Members, String> phoneNumberColumn;
    @FXML private TableColumn<Members, String> membershipTypeColumn;
    @FXML private TableColumn<Members, String> trainingPackageColumn;
    @FXML private TableColumn<Members, LocalDate> registrationDateColumn;
    @FXML private TextField searchField;

    private final MembersService memberService;
    private ObservableList<Members> memberList;
    private final ScenceController scenceController;

    public ListHoiVienController() {
        this.memberService = new MembersService();
        this.scenceController = new ScenceController();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Thiết lập ánh xạ cột với thuộc tính của Members
        memberIdColumn.setCellValueFactory(new PropertyValueFactory<>("memberId"));
        fullNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        phoneNumberColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
        membershipTypeColumn.setCellValueFactory(new PropertyValueFactory<>("membershipType"));
        trainingPackageColumn.setCellValueFactory(new PropertyValueFactory<>("trainingPackage"));
        registrationDateColumn.setCellValueFactory(new PropertyValueFactory<>("registrationDate"));

        // Định dạng cột ngày đăng ký
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        registrationDateColumn.setCellFactory(column -> new TableCell<Members, LocalDate>() {
            @Override
            protected void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.format(dtf));
                }
            }
        });

        // Đặt màu chữ để tránh trùng nền
        memberTable.setStyle("-fx-text-fill: white;");

        // Tải dữ liệu ban đầu
        loadMemberData();

        // Thiết lập tính năng tìm kiếm
        searchField.textProperty().addListener((obs, oldVal, newVal) -> filterMembers(newVal));
    }

    private void loadMemberData() {
        try {
            List<Members> members = memberService.getAllMembers();
            memberList = FXCollections.observableArrayList(members);
            memberTable.setItems(memberList);
            memberTable.refresh(); // Đảm bảo giao diện cập nhật
            System.out.println("Đã tải " + members.size() + " hội viên.");
            for (Members member : members) {
                System.out.println("Hội viên: " + member.getMemberId() + ", " + member.getName() + ", " +
                        member.getPhone() + ", " + member.getMembershipType() + ", " +
                        member.getTrainingPackage() + ", " + member.getRegistrationDate());
            }
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Không tải được dữ liệu: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void filterMembers(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            memberTable.setItems(memberList);
        } else {
            String lowerKeyword = keyword.trim().toLowerCase();
            ObservableList<Members> filtered = memberList.filtered(m ->
                    (m.getName() != null && m.getName().toLowerCase().contains(lowerKeyword)) ||
                            (m.getPhone() != null && m.getPhone().contains(lowerKeyword)) ||
                            (m.getMembershipType() != null && m.getMembershipType().toLowerCase().contains(lowerKeyword)) ||
                            (m.getTrainingPackage() != null && m.getTrainingPackage().toLowerCase().contains(lowerKeyword))
            );
            memberTable.setItems(filtered);
        }
    }

    @FXML
    private void handleDeleteMember(ActionEvent event) {
        Members selected = memberTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "Chưa chọn hội viên", "Vui lòng chọn hội viên cần xóa.");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Xác nhận xóa");
        confirm.setHeaderText(null);
        confirm.setContentText("Bạn có chắc muốn xóa hội viên " + selected.getName() + " (" + selected.getPhone() + ")?");
        confirm.showAndWait().ifPresent(resp -> {
            if (resp == ButtonType.OK) {
                try {
                    memberService.deleteMember(selected.getMemberId());
                    memberList.remove(selected);
                    memberTable.refresh();
                    showAlert(Alert.AlertType.INFORMATION, "Thành công", "Đã xóa hội viên.");
                } catch (Exception e) {
                    showAlert(Alert.AlertType.ERROR, "Lỗi", "Xóa thất bại: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        });
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    // Chuyển scene
    @FXML
    public void switchHome (javafx.scene.input.MouseEvent event) throws IOException {
        ActionEvent actionEvent = new ActionEvent(event.getSource(), event.getTarget());
        scenceController.switchToHelloView(actionEvent);
    }
    @FXML
    public void SwitchToregister(ActionEvent event) throws IOException {
        scenceController.switchToRegister(event);
    }

    @FXML
    public void switchToHelloView(ActionEvent event) throws IOException {
        scenceController.switchToHelloView(event);
    }

    @FXML
    public void switchToHoiVienFullInfo(ActionEvent event) throws IOException {
        scenceController.switchToHoiVienFullInfo(event);
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
    public void switchToCardAdd(ActionEvent event) throws IOException {
        scenceController.SwitchToCardAdd(event);
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
