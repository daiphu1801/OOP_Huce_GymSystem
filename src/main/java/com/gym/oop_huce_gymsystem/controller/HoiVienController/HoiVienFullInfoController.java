//package com.gym.oop_huce_gymsystem.controller.HoiVienController;
//
//import com.gym.oop_huce_gymsystem.model.Members;
//import com.gym.oop_huce_gymsystem.ScenceController;
//import com.gym.oop_huce_gymsystem.dao.MembersDao;
//import javafx.collections.FXCollections;
//import javafx.collections.ObservableList;
//import javafx.event.ActionEvent;
//import javafx.fxml.FXML;
//import javafx.fxml.Initializable;
//import javafx.scene.control.Label;
//import javafx.scene.control.TableColumn;
//import javafx.scene.control.TableView;
//import javafx.scene.control.Button;
//import javafx.scene.control.cell.PropertyValueFactory;
//
//import java.io.IOException;
//import java.net.URL;
//import java.sql.SQLException;
//import java.time.LocalDate;
//import java.time.format.DateTimeFormatter;
//import java.util.ResourceBundle;
//
//public class HoiVienFullInfoController implements Initializable {
//
//    @FXML private Label memberIdLabel;
//    @FXML private Label fullNameLabel;
//    @FXML private Label phoneNumberLabel;
//    @FXML private Label membershipTypeLabel;
//    @FXML private Label trainingPackageLabel;
////    @FXML private TableView<CheckInHistory> historyTable;
////    @FXML private TableColumn<CheckInHistory, LocalDate> dateCheckColumn;
////    @FXML private TableColumn<CheckInHistory, String> timeCheckinColumn;
//    @FXML private Button backButton;
//    @FXML private Button editInfoButton;
//
//    private final ScenceController scenceController;
//    private final MembersDao membersDao;
//    private Members currentMember;
//    private int memberId;
//
//    public HoiVienFullInfoController() {
//        this.scenceController = new ScenceController();
//        this.membersDao = new MembersDao();
//    }
//
//    @Override
//    public void initialize(URL location, ResourceBundle resources) {
//        if (memberIdLabel == null) {
//            System.out.println("[HoiVienFullInfoController] Lỗi: memberIdLabel không được inject từ FXML.");
//        } else {
//            System.out.println("[HoiVienFullInfoController] memberIdLabel đã được inject thành công.");
//        }
//
//        if (memberId != 0) {
//            loadMemberData();
//        }
//    }
//
//    public void setMemberId(int memberId) {
//        this.memberId = memberId;
//        if (memberIdLabel != null) {
//            loadMemberData();
//        } else {
//            System.out.println("[HoiVienFullInfoController] Chờ initialize hoàn tất để load dữ liệu.");
//        }
//    }
//
//    private void loadMemberData() {
//        try {
//            this.currentMember = membersDao.getMemberById(memberId);
//            if (currentMember != null) {
//                memberIdLabel.setText(String.valueOf(currentMember.getMemberId()));
//                fullNameLabel.setText(currentMember.getName());
//                phoneNumberLabel.setText(currentMember.getPhone());
//                membershipTypeLabel.setText(currentMember.getMembershipType());
//                trainingPackageLabel.setText(currentMember.getTrainingPackage());
//
//                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
//                LocalDate registrationDate = currentMember.getRegistrationDate();
//
////                loadCheckInHistory();
//            } else {
//                System.out.println("[HoiVienFullInfoController] Không tìm thấy hội viên với ID: " + memberId);
//            }
//        } catch (SQLException e) {
//            System.out.println("[HoiVienFullInfoController] Lỗi khi lấy dữ liệu hội viên từ database: " + e.getMessage());
//            e.printStackTrace();
//        }
//    }
//
////    private void loadCheckInHistory() {
////        ObservableList<CheckInHistory> historyList = FXCollections.observableArrayList();
////        historyList.add(new CheckInHistory(LocalDate.now(), "08:30"));
////        historyList.add(new CheckInHistory(LocalDate.now().minusDays(1), "07:15"));
////
////        dateCheckColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
////        timeCheckinColumn.setCellValueFactory(new PropertyValueFactory<>("time"));
////        historyTable.setItems(historyList);
////    }
//
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
//
//    @FXML
//    public void switchToEdit(ActionEvent event) throws IOException {
//        System.out.println("[HoiVienFullInfoController] Chuyển sang giao diện sửa thông tin cho: " + (currentMember != null ? currentMember.getName() : "N/A"));
//        HoiVienEditController controller = scenceController.switchToHoiVienEdit(event, memberId);
//    }
//}
//
////class CheckInHistory {
////    private LocalDate date;
////    private String time;
////
////    public CheckInHistory(LocalDate date, String time) {
////        this.date = date;
////        this.time = time;
////    }
////
////    public LocalDate getDate() {
////        return date;
////    }
////
////    public String getTime() {
////        return time;
////    }
////}