package com.gym.oop_huce_gymsystem.controller.HoiVienController;

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
import javafx.application.Platform;
import javafx.scene.input.MouseEvent;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ListHoiVienController implements Initializable {

    @FXML private TableView<Members> memberTable;
    @FXML private TableColumn<Members, Integer> memberIdColumn;
    @FXML private TableColumn<Members, String> cardIDColumn;
    @FXML private TableColumn<Members, String> fullNameColumn;
    @FXML private TableColumn<Members, String> phoneNumberColumn;
    @FXML private TableColumn<Members, String> genderColumn;
    @FXML private TableColumn<Members, String> emailColumn;
    @FXML private TextField searchField;

    private final MembersService memberService;
    private ObservableList<Members> memberList;
    private final ScenceController scenceController;

    // Khởi tạo controller
    public ListHoiVienController() {
        this.memberService = new MembersService();
        this.scenceController = new ScenceController();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Thiết lập kích thước tối thiểu cho các cột
        memberIdColumn.setMinWidth(50);
        cardIDColumn.setMinWidth(100);
        fullNameColumn.setMinWidth(150);
        phoneNumberColumn.setMinWidth(100);
        genderColumn.setMinWidth(120);
        emailColumn.setMinWidth(100);

        // Thiết lập ánh xạ cột với thuộc tính của Members
        memberIdColumn.setCellValueFactory(new PropertyValueFactory<>("memberId"));
        cardIDColumn.setCellValueFactory(new PropertyValueFactory<>("cardCode"));
        fullNameColumn.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        phoneNumberColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
        genderColumn.setCellValueFactory(new PropertyValueFactory<>("gender"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));

        // Thiết lập cell factory với text màu trắng rõ ràng
        memberIdColumn.setCellFactory(column -> createStringTableCell());
        cardIDColumn.setCellFactory(column -> createStringTableCell());
        fullNameColumn.setCellFactory(column -> createStringTableCell());
        phoneNumberColumn.setCellFactory(column -> createStringTableCell());
        genderColumn.setCellFactory(column -> createStringTableCell());
        emailColumn.setCellFactory(column -> createStringTableCell());

        // Tải dữ liệu ban đầu
        loadMemberData();

        // Thiết lập tính năng tìm kiếm
        searchField.textProperty().addListener((obs, oldVal, newVal) -> filterMembers(newVal));

        // Đảm bảo bảng được style đúng
        memberTable.getStyleClass().add("memberTable");
    }

    // Xử lý sự kiện nhấp đúp để xem chi tiết hội viên
    @FXML
    public void handleDoubleClick(MouseEvent event) {
        if (event.getClickCount() == 2) {
            Members selected = memberTable.getSelectionModel().getSelectedItem();
            if (selected == null) {
                System.out.println("[ListHoiVienController] Lỗi: Không có hội viên nào được chọn để xem chi tiết.");
                showAlert(Alert.AlertType.WARNING, "Chưa chọn hội viên", "Vui lòng chọn hội viên để xem thông tin chi tiết.");
                return;
            }

            System.out.println("[ListHoiVienController] Nhấp đúp vào hội viên: " + selected.getFullName() + ", SĐT: " + selected.getPhone());
            try {
                scenceController.switchToHoiVienFullInfo(new ActionEvent(event.getSource(), null), selected.getMemberId());
                System.out.println("[ListHoiVienController] Đã chuyển sang HoiVienFullInfoController thành công.");
            } catch (Exception e) {
                System.out.println("[ListHoiVienController] Lỗi khi chuyển sang HoiVienFullInfoController: " + e.getMessage());
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể mở giao diện chi tiết: " + e.getMessage());
            }
        }
    }

    // Tạo TableCell với style rõ ràng
    private <T> TableCell<Members, T> createStringTableCell() {
        return new TableCell<Members, T>() {
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

    // Tải dữ liệu hội viên từ service
    private void loadMemberData() {
        try {
            List<Members> members = memberService.getAllMembers();
            memberList = FXCollections.observableArrayList(members);

            if (members.isEmpty()) {
                System.out.println("Danh sách hội viên trống!");
            } else {
                System.out.println("Đã tải " + members.size() + " hội viên.");
                for (Members member : members) {
                    System.out.println("Hội viên: " + member.getMemberId() + ", " + member.getFullName() + ", " +
                            member.getPhone() + ", " + member.getCardCode() + ", " + member.getGender() + ", " + member.getEmail());
                }
            }

            Platform.runLater(() -> {
                memberTable.setItems(memberList);
                memberTable.refresh();
                System.out.println("Số dòng trong bảng: " + memberTable.getItems().size());
            });

        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Không tải được dữ liệu: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Lọc danh sách hội viên dựa trên từ khóa tìm kiếm
    private void filterMembers(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            memberTable.setItems(memberList);
        } else {
            String lowerKeyword = keyword.trim().toLowerCase();
            ObservableList<Members> filtered = memberList.filtered(m ->
                    (m.getFullName() != null && m.getFullName().toLowerCase().contains(lowerKeyword)) ||
                            (m.getPhone() != null && m.getPhone().contains(lowerKeyword)) ||
                            (m.getCardCode() != null && m.getCardCode().toLowerCase().contains(lowerKeyword)) ||
                            (m.getGender() != null && m.getGender().toLowerCase().contains(lowerKeyword)) ||
                            (m.getEmail() != null && m.getEmail().toLowerCase().contains(lowerKeyword))
            );
            memberTable.setItems(filtered);
        }
        memberTable.refresh();
    }

    // Xử lý xóa hội viên
    @FXML
    private void handleDeleteMember(ActionEvent event) {
        Members selected = memberTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "Chưa chọn hội viên", "Vui lòng chọn hội viên cần xóa.");
            return;
        }

        System.out.println("Bắt đầu xóa hội viên: " + selected.getFullName() + ", memberId: " + selected.getMemberId() + ", cardCode: " + selected.getCardCode());

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Xác nhận xóa");
        confirm.setHeaderText(null);
        confirm.setContentText("Bạn có chắc muốn xóa hội viên " + selected.getFullName() + " (" + selected.getPhone() + ")?");
        confirm.showAndWait().ifPresent(resp -> {
            if (resp == ButtonType.OK) {
                try {
                    memberService.deleteMember(selected.getMemberId());
                    memberList.remove(selected);
                    memberTable.refresh();
                    System.out.println("Xóa hội viên thành công: " + selected.getFullName());
                    showAlert(Alert.AlertType.INFORMATION, "Thành công", "Đã xóa hội viên.");
                } catch (Exception e) {
                    System.out.println("Lỗi khi xóa hội viên: " + e.getMessage());
                    showAlert(Alert.AlertType.ERROR, "Lỗi", "Xóa thất bại: " + e.getMessage());
                    e.printStackTrace();
                }
            } else {
                System.out.println("Hủy xóa hội viên: " + selected.getFullName());
            }
        });
    }

    // Hiển thị thông báo
    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    // Các phương thức chuyển scene
    @FXML
    public void switchHome(javafx.scene.input.MouseEvent event) throws IOException {
        ActionEvent actionEvent = new ActionEvent(event.getSource(), event.getTarget());
        scenceController.switchToHelloView(actionEvent);
    }

    @FXML
    public void SwitchToregister(ActionEvent event,String CardId) throws IOException {
        scenceController.switchToRegister(event,CardId);
    }

    @FXML
    public void switchToHelloView(ActionEvent event) throws IOException {
        scenceController.switchToHelloView(event);
    }

    @FXML
    public void switchToHoiVienFullInfo(ActionEvent event, int memberId) throws IOException {
        scenceController.switchToHoiVienFullInfo(event, memberId);
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