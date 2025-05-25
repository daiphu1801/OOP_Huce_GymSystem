package com.gym.oop_huce_gymsystem.controller.HoiVienController;

import com.gym.oop_huce_gymsystem.model.Checkins;
import com.gym.oop_huce_gymsystem.model.Members;
import com.gym.oop_huce_gymsystem.ScenceController;
import com.gym.oop_huce_gymsystem.dao.MembersDao;
import com.gym.oop_huce_gymsystem.service.CheckinsService;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.ResourceBundle;

public class HoiVienFullInfoController implements Initializable {

    @FXML private Label memberIdLabel;
    @FXML private Label cardCodeLabel;
    @FXML private Label fullNameLabel;
    @FXML private Label phoneNumberLabel;
    @FXML private Label genderLabel;
    @FXML private Label emailLabel;
    @FXML private Button backButton;
    @FXML private Button editInfoButton;
    @FXML private TableView<Checkins> historyTable;
    @FXML private TableColumn<Checkins, String> dateCheckColumn;
    @FXML private TableColumn<Checkins, String> timeCheckinColumn;
    @FXML private TextField dateSearch;

    private final ScenceController scenceController;
    private final MembersDao membersDao;
    private final CheckinsService checkinsService;
    private Members currentMember;
    private String memberId;

    // Khởi tạo controller
    public HoiVienFullInfoController() {
        this.scenceController = new ScenceController();
        this.membersDao = new MembersDao();
        this.checkinsService = new CheckinsService();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Kiểm tra các thành phần FXML
        if (memberIdLabel == null || fullNameLabel == null || phoneNumberLabel == null ||
                cardCodeLabel == null || genderLabel == null || emailLabel == null ||
                historyTable == null || dateCheckColumn == null || timeCheckinColumn == null || dateSearch == null) {
            System.out.println("[HoiVienFullInfoController] Lỗi: Một hoặc nhiều thành phần FXML không được inject.");
        } else {
            System.out.println("[HoiVienFullInfoController] Tất cả thành phần FXML đã được inject thành công.");
        }

        // Thiết lập TableView
        dateCheckColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getCheckinTime()
                        .format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))));
        timeCheckinColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getCheckinTime()
                        .format(DateTimeFormatter.ofPattern("HH:mm:ss"))));

        // Xử lý tìm kiếm theo ngày
        dateSearch.textProperty().addListener((obs, oldValue, newValue) -> {
            if (memberId != null) {
                loadCheckinHistory(newValue);
            }
        });

        // Tải dữ liệu nếu memberId đã được thiết lập
        if (memberId != null) {
            loadMemberData();
            loadCheckinHistory(null);
        }
    }

    // Thiết lập memberId và tải dữ liệu
    public void setMemberId(String memberId) {
        this.memberId = memberId;
        if (memberIdLabel != null && historyTable != null) {
            loadMemberData();
            loadCheckinHistory(null);
        } else {
            System.out.println("[HoiVienFullInfoController] Chờ initialize hoàn tất để load dữ liệu.");
        }
    }

    // Tải dữ liệu hội viên từ cơ sở dữ liệu
    private void loadMemberData() {
        try {
            this.currentMember = membersDao.getMemberById(memberId);
            if (currentMember != null) {
                memberIdLabel.setText(String.valueOf(currentMember.getMemberId()));
                cardCodeLabel.setText(currentMember.getCardCode() != null ? currentMember.getCardCode() : "N/A");
                fullNameLabel.setText(currentMember.getFullName() != null ? currentMember.getFullName() : "N/A");
                phoneNumberLabel.setText(currentMember.getPhone() != null ? currentMember.getPhone() : "N/A");
                genderLabel.setText(currentMember.getGender() != null ? currentMember.getGender() : "N/A");
                emailLabel.setText(currentMember.getEmail() != null ? currentMember.getEmail() : "N/A");
            } else {
                System.out.println("[HoiVienFullInfoController] Không tìm thấy hội viên với ID: " + memberId);
            }
        } catch (SQLException e) {
            System.out.println("[HoiVienFullInfoController] Lỗi khi lấy dữ liệu hội viên từ database: " + e.getMessage());
            showErrorAlert("Lỗi", "Không thể tải thông tin hội viên: " + e.getMessage());
        }
    }

    // Tải lịch sử check-in
    private void loadCheckinHistory(String date) {
        try {
            List<Checkins> checkins;
            if (date == null || date.trim().isEmpty()) {
                checkins = checkinsService.getCheckinsByMemberId(Integer.parseInt(memberId));
            } else {
                // Chuyển đổi định dạng ngày từ DD/MM/YYYY sang YYYY-MM-DD
                String formattedDate = convertDateFormat(date);
                checkins = checkinsService.getCheckinsByDate(Integer.parseInt(memberId), formattedDate);
            }
            ObservableList<Checkins> data = FXCollections.observableArrayList(checkins);
            historyTable.setItems(data);
            if (checkins.isEmpty()) {
                historyTable.setPlaceholder(new Label("Không có lịch sử check-in."));
            }
        } catch (SQLException e) {
            System.out.println("[HoiVienFullInfoController] Lỗi khi lấy lịch sử check-in: " + e.getMessage());
            showErrorAlert("Lỗi", "Không thể tải lịch sử check-in: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("[HoiVienFullInfoController] Lỗi định dạng ngày: " + e.getMessage());
            showErrorAlert("Lỗi", "Định dạng ngày không hợp lệ. Vui lòng nhập theo định dạng DD/MM/YYYY.");
        }
    }

    // Xử lý sự kiện check-in
    @FXML
    private void handleCheckin(ActionEvent event) {
        try {
            checkinsService.addCheckin(Integer.parseInt(memberId));
            loadCheckinHistory(null); // Tải lại lịch sử check-in
            showInfoAlert("Thành công", "Check-in thành công cho hội viên ID: " + memberId);
        } catch (SQLException e) {
            System.out.println("[HoiVienFullInfoController] Lỗi khi check-in: " + e.getMessage());
            showErrorAlert("Lỗi", "Không thể check-in: " + e.getMessage());
        }
    }

    // Chuyển đổi định dạng ngày từ DD/MM/YYYY sang YYYY-MM-DD
    private String convertDateFormat(String inputDate) {
        try {
            DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            return LocalDate.parse(inputDate, inputFormatter).format(outputFormatter);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Định dạng ngày không hợp lệ: " + inputDate);
        }
    }

    // Hiển thị thông báo lỗi
    private void showErrorAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Hiển thị thông báo thành công
    private void showInfoAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Các phương thức chuyển scene
    @FXML
    public void switchHome(javafx.scene.input.MouseEvent event) throws IOException {
        ActionEvent actionEvent = new ActionEvent(event.getSource(), event.getTarget());
        scenceController.switchToHelloView(actionEvent);
    }

    @FXML
    public void SwitchToregister(ActionEvent event, String CardId) throws IOException {
        scenceController.switchToRegister(event, CardId);
    }

    @FXML
    public void switchToHelloView(ActionEvent event) throws IOException {
        scenceController.switchToHelloView(event);
    }

    @FXML
    public void switchToHoiVienFullInfo(ActionEvent event, String memberId) throws IOException {
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

    @FXML
    public void switchToEdit(ActionEvent event) throws IOException {
        System.out.println("[HoiVienFullInfoController] Chuyển sang giao diện sửa thông tin cho: " +
                (currentMember != null ? currentMember.getFullName() : "N/A"));
        scenceController.switchToHoiVienEdit(event, String.valueOf(memberId));
    }
}