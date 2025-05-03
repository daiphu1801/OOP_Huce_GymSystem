//package com.gym.oop_huce_gymsystem.controller.HoiVienController;
//
//import com.gym.oop_huce_gymsystem.model.Members;
//import com.gym.oop_huce_gymsystem.service.MembersService;
//import com.gym.oop_huce_gymsystem.ScenceController;
//import javafx.collections.FXCollections;
//import javafx.collections.ObservableList;
//import javafx.event.ActionEvent;
//import javafx.fxml.FXML;
//import javafx.fxml.Initializable;
//import javafx.scene.control.*;
//import javafx.scene.control.cell.PropertyValueFactory;
//import javafx.application.Platform;
//
//import javafx.scene.input.MouseEvent;
//import java.io.IOException;
//import java.net.URL;
//import java.time.LocalDate;
//import java.time.format.DateTimeFormatter;
//import java.util.List;
//import java.util.ResourceBundle;
//
//public class ListHoiVienController implements Initializable {
//
//    @FXML private TableView<Members> memberTable;
//    @FXML private TableColumn<Members, Integer> memberIdColumn;
//    @FXML private TableColumn<Members, String> fullNameColumn;
//    @FXML private TableColumn<Members, String> phoneNumberColumn;
//    @FXML private TableColumn<Members, String> genderColumn;
//    @FXML private TableColumn<Members, String> emailColumn;
//    @FXML private TextField searchField;
//
//    private final MembersService memberService;
//    private ObservableList<Members> memberList;
//    private final ScenceController scenceController;
//
//    public ListHoiVienController() {
//        this.memberService = new MembersService();
//        this.scenceController = new ScenceController();
//    }
//
//    @Override
//    public void initialize(URL location, ResourceBundle resources) {
//        // Thiết lập kích thước tối thiểu cho các cột
//        memberIdColumn.setMinWidth(50);
//        fullNameColumn.setMinWidth(150);
//        phoneNumberColumn.setMinWidth(100);
//        genderColumn.setMinWidth(120);
//        emailColumn.setMinWidth(100);
//
//        // Thiết lập ánh xạ cột với thuộc tính của Members
//        memberIdColumn.setCellValueFactory(new PropertyValueFactory<>("memberId"));
//        fullNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
//        phoneNumberColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
//        genderColumn.setCellValueFactory(new PropertyValueFactory<>("gender"));
//        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
//
//
//        // Thiết lập cell factory với text màu trắng rõ ràng
//        memberIdColumn.setCellFactory(column -> createStringTableCell());
//        fullNameColumn.setCellFactory(column -> createStringTableCell());
//        phoneNumberColumn.setCellFactory(column -> createStringTableCell());
//        genderColumn.setCellFactory(column -> createStringTableCell());
//        emailColumn.setCellFactory(column -> createStringTableCell());
//
//        // Tải dữ liệu ban đầu
//        loadMemberData();
//
//        // Thiết lập tính năng tìm kiếm
//        searchField.textProperty().addListener((obs, oldVal, newVal) -> filterMembers(newVal));
//
//        // Đảm bảo bảng được style đúng
//        memberTable.getStyleClass().add("memberTable");
//    }
//
//    @FXML
//    public void handleDoubleClick(MouseEvent event) {
//        // Kiểm tra nhấp đúp (2 lần nhấp)
//        if (event.getClickCount() == 2) {
//            Members selected = memberTable.getSelectionModel().getSelectedItem();
//            if (selected == null) {
//                System.out.println("[ListHoiVienController] Lỗi: Không có hội viên nào được chọn để xem chi tiết.");
//                showAlert(Alert.AlertType.WARNING, "Chưa chọn hội viên", "Vui lòng chọn hội viên để xem thông tin chi tiết.");
//                return;
//            }
//
//            System.out.println("[ListHoiVienController] Nhấp đúp vào hội viên: " + selected.getName() + ", SĐT: " + selected.getPhone());
//            try {
//                // Chỉ truyền memberId thay vì toàn bộ đối tượng Members
//                HoiVienFullInfoController controller = scenceController.switchToHoiVienFullInfo(new ActionEvent(event.getSource(), null), selected.getMemberId());
//                System.out.println("[ListHoiVienController] Đã chuyển sang HoiVienFullInfoController thành công.");
//            } catch (Exception e) {
//                System.out.println("[ListHoiVienController] Lỗi khi chuyển sang HoiVienFullInfoController: " + e.getMessage());
//                e.printStackTrace();
//                showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể mở giao diện chi tiết: " + e.getMessage());
//            }
//        }
//    }
//
//
//    // Helper method để tạo TableCell với style rõ ràng
//    private <T> TableCell<Members, T> createStringTableCell() {
//        return new TableCell<Members, T>() {
//            @Override
//            protected void updateItem(T item, boolean empty) {
//                super.updateItem(item, empty);
//
//                if (empty || item == null) {
//                    setText(null);
//                    setStyle("");
//                } else {
//                    setText(item.toString());
////                    setStyle("-fx-text-fill: white !important;");
//                }
//            }
//        };
//    }
//
//    private void loadMemberData() {
//        try {
//            List<Members> members = memberService.getAllMembers();
//            memberList = FXCollections.observableArrayList(members);
//
//            // Kiểm tra danh sách thành viên
//            if (members.isEmpty()) {
//                System.out.println("Danh sách hội viên trống!");
//            } else {
//                System.out.println("Đã tải " + members.size() + " hội viên.");
//                for (Members member : members) {
//                    System.out.println("Hội viên: " + member.getMemberId() + ", " + member.getName() + ", " +
//                            member.getPhone() + ", " + member.getMembershipType() + ", " +
//                            member.getTrainingPackage() + ", " + member.getRegistrationDate());
//                }
//            }
//
//            // Sử dụng Platform.runLater để đảm bảo UI được cập nhật trên thread JavaFX
//            Platform.runLater(() -> {
//                memberTable.setItems(memberList);
//                memberTable.refresh();
//
//                // Debug: Kiểm tra số lượng dòng trong bảng
//                System.out.println("Số dòng trong bảng: " + memberTable.getItems().size());
//            });
//
//        } catch (Exception e) {
//            showAlert(Alert.AlertType.ERROR, "Lỗi", "Không tải được dữ liệu: " + e.getMessage());
//            e.printStackTrace();
//        }
//    }
//
//    private void filterMembers(String keyword) {
//        if (keyword == null || keyword.trim().isEmpty()) {
//            memberTable.setItems(memberList);
//        } else {
//            String lowerKeyword = keyword.trim().toLowerCase();
//            ObservableList<Members> filtered = memberList.filtered(m ->
//                    (m.getName() != null && m.getName().toLowerCase().contains(lowerKeyword)) ||
//                            (m.getPhone() != null && m.getPhone().contains(lowerKeyword)) ||
//                            (m.getMembershipType() != null && m.getMembershipType().toLowerCase().contains(lowerKeyword)) ||
//                            (m.getTrainingPackage() != null && m.getTrainingPackage().toLowerCase().contains(lowerKeyword))
//            );
//            memberTable.setItems(filtered);
//        }
//        memberTable.refresh();
//    }
//
//    @FXML
//    private void handleDeleteMember(ActionEvent event) {
//        Members selected = memberTable.getSelectionModel().getSelectedItem();
//        if (selected == null) {
//            showAlert(Alert.AlertType.WARNING, "Chưa chọn hội viên", "Vui lòng chọn hội viên cần xóa.");
//            return;
//        }
//
//        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
//        confirm.setTitle("Xác nhận xóa");
//        confirm.setHeaderText(null);
//        confirm.setContentText("Bạn có chắc muốn xóa hội viên " + selected.getName() + " (" + selected.getPhone() + ")?");
//        confirm.showAndWait().ifPresent(resp -> {
//            if (resp == ButtonType.OK) {
//                try {
//                    memberService.deleteMember(selected.getMemberId());
//                    memberList.remove(selected);
//                    memberTable.refresh();
//                    showAlert(Alert.AlertType.INFORMATION, "Thành công", "Đã xóa hội viên.");
//                } catch (Exception e) {
//                    showAlert(Alert.AlertType.ERROR, "Lỗi", "Xóa thất bại: " + e.getMessage());
//                    e.printStackTrace();
//                }
//            }
//        });
//    }
//
//    private void showAlert(Alert.AlertType type, String title, String content) {
//        Alert alert = new Alert(type);
//        alert.setTitle(title);
//        alert.setHeaderText(null);
//        alert.setContentText(content);
//        alert.showAndWait();
//    }
//
//    // Các phương thức chuyển scene giữ nguyên như cũ
//    @FXML
//    public void switchHome (javafx.scene.input.MouseEvent event) throws IOException {
//        ActionEvent actionEvent = new ActionEvent(event.getSource(), event.getTarget());
//        scenceController.switchToHelloView(actionEvent);
//    }
//    @FXML
//    public void SwitchToregister(ActionEvent event) throws IOException {
//        scenceController.switchToRegister(event);
//    }
//
//    @FXML
//    public void switchToHelloView(ActionEvent event) throws IOException {
//        scenceController.switchToHelloView(event);
//    }
//
//    @FXML
//    public void switchToHoiVienFullInfo(ActionEvent event,int memberId) throws IOException {
//        scenceController.switchToHoiVienFullInfo(event,memberId);
//    }
//
//    @FXML
//    public void SwitchTolisthoivien(ActionEvent event) throws IOException {
//        scenceController.switchToListHoiVien(event);
//    }
//
//    @FXML
//    public void SwitchTothiet_bi(ActionEvent event) throws IOException {
//        scenceController.switchToThietBi(event);
//    }
//
//    @FXML
//    public void SwitchTothongke(ActionEvent event) throws IOException {
//        scenceController.switchToThongKe(event);
//    }
//
//    @FXML
//    public void SwitchTotrainerList(ActionEvent event) throws IOException {
//        scenceController.SwitchTotrainerList(event);
//    }
//
//    @FXML
//    public void switchToMemberCard(ActionEvent event) throws IOException {
//        scenceController.switchToMemberCardList(event);
//    }
//
//    @FXML
//    public void switchToCardAdd(ActionEvent event) throws IOException {
//        scenceController.SwitchToCardAdd(event);
//    }
//
//    @FXML
//    public void SwitchtoProduct(ActionEvent event) throws IOException {
//        scenceController.SwitchtoProduct(event);
//    }
//
//    @FXML
//    public void switchToProductRegis(ActionEvent event) throws IOException {
//        scenceController.SwitchtoProductRegis(event);
//    }
//
//    @FXML
//    public void switchToPTRegis(ActionEvent event) throws IOException {
//        scenceController.SwitchToPT_Regis(event);
//    }
//}