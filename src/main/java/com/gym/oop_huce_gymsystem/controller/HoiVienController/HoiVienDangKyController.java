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
//import javafx.scene.control.Alert;
//import javafx.scene.control.Button;
//import javafx.scene.control.ComboBox;
//import javafx.scene.control.DatePicker;
//import javafx.scene.control.TextField;
//
//import java.io.IOException;
//import java.net.URL;
//import java.time.LocalDate;
//import java.util.ResourceBundle;
//
//public class HoiVienDangKyController implements Initializable {
//
//    @FXML
//    private TextField tenHoiVienField;
//
//    @FXML
//    private TextField soDienThoaiField;
//
//    @FXML
//    private ComboBox<String> loaiTheComboBox;
//
//    @FXML
//    private ComboBox<String> goiTapComboBox;
//
//    @FXML
//    private DatePicker ngayDangKyPicker;
//
//    @FXML
//    private Button confirmButton;
//
//    @FXML
//    private Button backButton;
//
//    private final MembersService membersService = new MembersService();
//    private final ScenceController scenceController = new ScenceController();
//
//
//    @Override
//    public void initialize(URL location, ResourceBundle resources) {
//        // Thiết lập các giá trị cho ComboBox loại thẻ
//        ObservableList<String> cardTypes = FXCollections.observableArrayList(
//                "Thẻ thường", "Thẻ VIP", "Thẻ Premium");
//        loaiTheComboBox.setItems(cardTypes);
//        loaiTheComboBox.setValue(cardTypes.get(0));
//
//        // Thiết lập các giá trị cho ComboBox gói tập
//        ObservableList<String> packageTypes = FXCollections.observableArrayList(
//                "1 tháng", "3 tháng", "6 tháng", "12 tháng");
//        goiTapComboBox.setItems(packageTypes);
//        goiTapComboBox.setValue(packageTypes.get(0));
//
//        // Ngày đăng ký mặc định là ngày hiện tại
//        ngayDangKyPicker.setValue(LocalDate.now());
//    }
//
//    @FXML
//    public void dangKyHoiVien(ActionEvent event) {
//        try {
//            String fullName = tenHoiVienField.getText().trim();
//            String phoneNumber = soDienThoaiField.getText().trim();
//            String cardType = loaiTheComboBox.getValue();
//            String packageType = goiTapComboBox.getValue();
//            LocalDate registerDate = ngayDangKyPicker.getValue();
//
//            // Tạo đối tượng Members
//            Members newMember = new Members();
//            newMember.setName(fullName);
//            newMember.setPhone(phoneNumber);
//            newMember.setMembershipType(cardType);
//            newMember.setTrainingPackage(packageType);
//            newMember.setRegistrationDate(registerDate);
//
//            // Gọi service để thêm
//            membersService.addMember(newMember);
//
//            showAlert(Alert.AlertType.INFORMATION, "Thành công",
//                    "Thêm hội viên thành công! ID: " + newMember.getPhone());
//
//            scenceController.switchToListHoiVien(event);
////            confirmButton.setDisable(true);
//        } catch (Exception e) {
//            showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể thêm hội viên: " + e.getMessage());
//            e.printStackTrace();
//        }
//    }
//
//
//    private void showAlert(Alert.AlertType type, String title, String content) {
//        Alert alert = new Alert(type);
//        alert.setTitle(title);
//        alert.setHeaderText(null);
//        alert.setContentText(content);
//        alert.showAndWait();
//    }
//
//    @FXML
//    public void switchHome(javafx.scene.input.MouseEvent event) throws IOException {
//        scenceController.switchToHelloView(new ActionEvent(event.getSource(), event.getTarget()));
//    }
//
//    // Các phương thức chuyển scene khác
//    @FXML public void switchToHoiVienFullInfo(ActionEvent event) throws IOException {
//        scenceController.switchToHoiVienFullInfo(event);
//    }
//    @FXML public void SwitchTolisthoivien(ActionEvent event) throws IOException {
//        scenceController.switchToListHoiVien(event);
//    }
//    @FXML public void switchToRegister(ActionEvent event) throws IOException {
//        scenceController.switchToRegister(event);
//    }
//    @FXML public void SwitchTothiet_bi(ActionEvent event) throws IOException {
//        scenceController.switchToThietBi(event);
//    }
//    @FXML public void switchToThietBiInfoFull(ActionEvent event) throws IOException {
//        scenceController.switchToThietBiInfoFull(event);
//    }
//    @FXML public void switchToThietBiRegis(ActionEvent event) throws IOException {
//        scenceController.switchToThietBiRegis(event);
//    }
//    @FXML public void SwitchTothongke(ActionEvent event) throws IOException {
//        scenceController.switchToThongKe(event);
//    }
//    @FXML public void SwitchTotrainerList(ActionEvent event) throws IOException {
//        scenceController.SwitchTotrainerList(event);
//    }
//    @FXML public void switchToMemberCardList(ActionEvent event) throws IOException {
//        scenceController.switchToMemberCardList(event);
//    }
//    @FXML public void switchToCardAdd(ActionEvent event) throws IOException {
//        scenceController.SwitchToCardAdd(event);
//    }
//    @FXML public void switchToProduct(ActionEvent event) throws IOException {
//        scenceController.SwitchtoProduct(event);
//    }
//    @FXML public void switchToProductRegis(ActionEvent event) throws IOException {
//        scenceController.SwitchtoProductRegis(event);
//    }
//    @FXML public void switchToPTRegis(ActionEvent event) throws IOException {
//        scenceController.SwitchToPT_Regis(event);
//    }
//}
