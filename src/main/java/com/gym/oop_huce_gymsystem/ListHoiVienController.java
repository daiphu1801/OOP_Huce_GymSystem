package com.gym.oop_huce_gymsystem;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

public class ListHoiVienController implements Initializable {

    @FXML
    private TableView<Member> memberTable;

    @FXML
    private TableColumn<Member, String> memberIdColumn;

    @FXML
    private TableColumn<Member, String> fullNameColumn;

    @FXML
    private TableColumn<Member, String> phoneNumberColumn;

    @FXML
    private TableColumn<Member, String> packageTypeColumn;

    @FXML
    private TableColumn<Member, String> cardTypeColumn;

    @FXML
    private TableColumn<Member, String> registerDateColumn;

    @FXML
    private TableColumn<Member, String> cardStatusColumn;

    @FXML
    private TextField searchField;

    @FXML
    private Button addMemberButton;

    @FXML
    private Button deleteMemberButton;

    private final MemberDAO memberDAO = MemberDAO.getInstance();
    private ObservableList<Member> memberList;
    private final ScenceController scenceController = new ScenceController();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Setup table columns
        memberIdColumn.setCellValueFactory(new PropertyValueFactory<>("memberId"));
        fullNameColumn.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        phoneNumberColumn.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        packageTypeColumn.setCellValueFactory(new PropertyValueFactory<>("packageType"));
        cardTypeColumn.setCellValueFactory(new PropertyValueFactory<>("cardType"));
        registerDateColumn.setCellValueFactory(cellData ->
                javafx.beans.binding.Bindings.createStringBinding(
                        () -> cellData.getValue().getFormattedRegisterDate()));
        cardStatusColumn.setCellValueFactory(new PropertyValueFactory<>("cardStatus"));

        // Update all card statuses to reflect current date
        memberDAO.updateAllCardStatuses();

        // Load initial data
        loadMemberData();

        // Setup search event
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            searchMembers(newValue);
        });

        // Setup delete member event
        deleteMemberButton.setOnAction(this::handleDeleteMember);

        // Double click on a member will open details view
        memberTable.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                Member selectedMember = memberTable.getSelectionModel().getSelectedItem();
                if (selectedMember != null) {
                    try {
                        openMemberDetails(selectedMember);
                    } catch (IOException e) {
                        e.printStackTrace();
                        showAlert(AlertType.ERROR, "Lỗi", "Không thể mở chi tiết hội viên: " + e.getMessage());
                    }
                }
            }
        });
    }

    // Open member details view (placeholder for future implementation)
    private void openMemberDetails(Member member) throws IOException {
        // This would typically set the selected member in a shared service
        // and then navigate to the details view
        switchToHoiVienFullInfo(new ActionEvent());
    }

    // Load member data from DAO
    private void loadMemberData() {
        List<Member> members = memberDAO.getAllMembers();
        memberList = FXCollections.observableArrayList(members);
        memberTable.setItems(memberList);
    }

    // Search members by keyword
    private void searchMembers(String keyword) {
        List<Member> searchResults = memberDAO.searchMembers(keyword);
        memberList.clear();
        memberList.addAll(searchResults);
    }

    // Handle delete member event
    @FXML
    private void handleDeleteMember(ActionEvent event) {
        Member selectedMember = memberTable.getSelectionModel().getSelectedItem();

        if (selectedMember == null) {
            showAlert(AlertType.WARNING, "Chưa chọn hội viên", "Vui lòng chọn hội viên cần xóa từ bảng.");
            return;
        }

        // Confirm deletion
        Alert confirmAlert = new Alert(AlertType.CONFIRMATION);
        confirmAlert.setTitle("Xác nhận xóa");
        confirmAlert.setHeaderText(null);
        confirmAlert.setContentText("Bạn có chắc chắn muốn xóa hội viên " + selectedMember.getFullName() + " không?");

        confirmAlert.showAndWait().ifPresent(response -> {
            if (response == javafx.scene.control.ButtonType.OK) {
                // Delete member from DAO
                boolean deleted = memberDAO.deleteMember(selectedMember.getMemberId());

                if (deleted) {
                    // Update table
                    memberList.remove(selectedMember);
                    showAlert(AlertType.INFORMATION, "Xóa thành công",
                            "Đã xóa hội viên " + selectedMember.getFullName() + " khỏi hệ thống.");
                } else {
                    showAlert(AlertType.ERROR, "Lỗi", "Không thể xóa hội viên.");
                }
            }
        });
    }

    private void showAlert(AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    // Method to refresh data after adding a new member
    public void refreshData() {
        memberDAO.updateAllCardStatuses();
        loadMemberData();
    }

    @FXML
    public void switchToHelloView(ActionEvent event) throws IOException {
        setupScene("hello-view.fxml", event);
    }

    @FXML
    public void switchToHoiVienFullInfo(ActionEvent event) throws IOException {
        setupScene("hoivien_full_info.fxml", event);
    }

    @FXML
    public void switchToListHoiVien(ActionEvent event) throws IOException {
        setupScene("list_hoivien.fxml", event);
    }

    @FXML
    public void switchToRegister(ActionEvent event) throws IOException {
        setupScene("register.fxml", event);
    }

    @FXML
    public void switchToThietBi(ActionEvent event) throws IOException {
        setupScene("thiet_bi.fxml", event);
    }

    @FXML
    public void switchToThietBiInfoFull(ActionEvent event) throws IOException {
        setupScene("thietbi_info_full.fxml", event);
    }

    @FXML
    public void switchToThietBiRegis(ActionEvent event) throws IOException {
        setupScene("thietbi_regis.fxml", event);
    }

    @FXML
    public void switchToThongKe(ActionEvent event) throws IOException {
        setupScene("thongke.fxml", event);
    }

    @FXML
    public void switchToPTList(ActionEvent event) throws IOException {
        setupScene("ptList.fxml", event);
    }

    @FXML
    public void SwitchTohelloview(ActionEvent event) throws IOException {
        switchToHelloView(event);
    }

    @FXML
    public void SwitchTohoivienfullinfo(ActionEvent event) throws IOException {
        switchToHoiVienFullInfo(event);
    }

    @FXML
    public void SwitchTolisthoivien(ActionEvent event) throws IOException {
        switchToListHoiVien(event);
    }

    @FXML
    public void SwitchToregister(ActionEvent event) throws IOException {
        switchToRegister(event);
    }

    @FXML
    public void SwitchTothiet_bi(ActionEvent event) throws IOException {
        switchToThietBi(event);
    }

    @FXML
    public void SwitchTothietbi_info_full(ActionEvent event) throws IOException {
        switchToThietBiInfoFull(event);
    }

    @FXML
    public void SwitchTothietbi_regis(ActionEvent event) throws IOException {
        switchToThietBiRegis(event);
    }

    @FXML
    public void SwitchTothongke(ActionEvent event) throws IOException {
        switchToThongKe(event);
    }

    @FXML
    public void SwitchTotrainerList(ActionEvent event) throws IOException {
        switchToPTList(event);
    }

    private void setupScene(String fxmlFile, ActionEvent event) throws IOException {
        scenceController.setupScene(fxmlFile, event);
    }
}