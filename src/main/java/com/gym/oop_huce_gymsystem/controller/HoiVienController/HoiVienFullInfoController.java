package com.gym.oop_huce_gymsystem.controller.HoiVienController;

import com.gym.oop_huce_gymsystem.model.Members;
import com.gym.oop_huce_gymsystem.ScenceController;
import com.gym.oop_huce_gymsystem.dao.MembersDao;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Button;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class HoiVienFullInfoController implements Initializable {

    @FXML private Label memberIdLabel;
    @FXML private Label fullNameLabel;
    @FXML private Label phoneNumberLabel;
    @FXML private Label membershipTypeLabel;
    @FXML private Label trainingPackageLabel;
    @FXML private Button backButton;
    @FXML private Button editInfoButton;

    private final ScenceController scenceController;
    private final MembersDao membersDao;
    private Members currentMember;
    private int memberId;

    public HoiVienFullInfoController() {
        this.scenceController = new ScenceController();
        this.membersDao = new MembersDao();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Kiểm tra xem các thành phần có được inject không
        if (memberIdLabel == null) {
            System.out.println("[HoiVienFullInfoController] Lỗi: memberIdLabel không được inject từ FXML.");
        } else {
            System.out.println("[HoiVienFullInfoController] memberIdLabel đã được inject thành công.");
        }

        // Load dữ liệu sau khi các thành phần giao diện đã được inject
        if (memberId != 0) {
            loadMemberData();
        }
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
        // Load dữ liệu sau khi các thành phần giao diện đã được inject
        if (memberIdLabel != null) {
            loadMemberData();
        } else {
            System.out.println("[HoiVienFullInfoController] Chờ initialize hoàn tất để load dữ liệu.");
        }
    }

    private void loadMemberData() {
        try {
            this.currentMember = membersDao.getMemberById(memberId);
            if (currentMember != null) {
                memberIdLabel.setText(String.valueOf(currentMember.getMemberId()));
                fullNameLabel.setText(currentMember.getName());
                phoneNumberLabel.setText(currentMember.getPhone());
                membershipTypeLabel.setText(currentMember.getMembershipType());
                trainingPackageLabel.setText(currentMember.getTrainingPackage());

                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                LocalDate registrationDate = currentMember.getRegistrationDate();

            } else {
                System.out.println("[HoiVienFullInfoController] Không tìm thấy hội viên với ID: " + memberId);
            }
        } catch (SQLException e) {
            System.out.println("[HoiVienFullInfoController] Lỗi khi lấy dữ liệu hội viên từ database: " + e.getMessage());
            e.printStackTrace();
        }
    }


    @FXML
    public void backToList(ActionEvent event) throws IOException {
        scenceController.switchToListHoiVien(event);
    }

    @FXML
    public void switchToEdit(ActionEvent event) throws IOException {
        System.out.println("[HoiVienFullInfoController] Chuyển sang giao diện sửa thông tin cho: " + (currentMember != null ? currentMember.getName() : "N/A"));
    }
}
