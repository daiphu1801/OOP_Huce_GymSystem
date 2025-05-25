package com.gym.oop_huce_gymsystem.controller.HoiVienController;

import com.gym.oop_huce_gymsystem.ScenceController;
import com.gym.oop_huce_gymsystem.dao.MemberShipCardsDao;
import com.gym.oop_huce_gymsystem.dao.MembersDao;
import com.gym.oop_huce_gymsystem.model.Members;
import com.gym.oop_huce_gymsystem.service.MembersService;
import com.gym.oop_huce_gymsystem.util.DatabaseConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class HoiVienDangKyController implements Initializable {
    @FXML
    private TextField tenHoiVienField;
    @FXML
    private TextField soDienThoaiField;
    @FXML
    private Label cardCodeField;
    @FXML
    private ComboBox<String> genderComboBox;
    @FXML
    private TextField emailTextField;
    @FXML
    private Button confirmButton;
    @FXML
    private Button backButton;

    private final MembersService membersService = new MembersService();
    private final ScenceController scenceController = new ScenceController();
    private String cardId;
    private MembersDao membersDao = new MembersDao();
    private MemberShipCardsDao memberShipCardsDao = new MemberShipCardsDao();
    private boolean isMemberCreated = false; // Theo dõi trạng thái tạo hội viên

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ObservableList<String> genders = FXCollections.observableArrayList("Nam", "Nữ");
        genderComboBox.setItems(genders);
        genderComboBox.setValue(genders.get(0));

    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
        // Gán giá trị sau khi kiểm tra null
        if (cardCodeField != null) {
            cardCodeField.setText(cardId != null ? cardId : "Chưa có mã thẻ");
            System.out.println("Set cardId: " + cardId);
        }
    }

    @FXML
    public void dangKyHoiVien(ActionEvent event) {
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);

            String fullName = tenHoiVienField.getText().trim();
            String phoneNumber = soDienThoaiField.getText().trim();
            String cardCode = cardCodeField.getText() != null ? cardCodeField.getText().trim() : null;
            String gender = genderComboBox.getValue();
            String email = emailTextField.getText().trim();

            // Kiểm tra nếu cardCode rỗng hoặc không hợp lệ
            if (cardCode == null || cardCode.isEmpty() || cardCode.equals("Chưa có mã thẻ")) {
                showAlert(Alert.AlertType.WARNING, "Cảnh báo", "Bạn cần tạo thêm thẻ trước khi nhập hội viên!");
                return; // Dừng thực thi nếu không có mã thẻ
            }

            Members newMember = new Members();
            newMember.setFullName(fullName);
            newMember.setPhone(phoneNumber);
            newMember.setCardCode(cardCode);
            newMember.setGender(gender);
            newMember.setEmail(email);

            String memberId = membersService.addMember(newMember);
            if (memberId != null) {
                conn.commit();
                isMemberCreated = true; // Đánh dấu hội viên đã được tạo thành công
                showAlert(Alert.AlertType.INFORMATION, "Thành công",
                        "Thêm hội viên thành công! Số điện thoại: " + newMember.getPhone());
                scenceController.switchToListHoiVien(event);
            } else {
                throw new SQLException("Không thể lấy memberId sau khi thêm.");
            }
        } catch (Exception e) {
            if (conn != null) {
                try {
                    conn.rollback();
                    // Xóa thẻ vừa tạo nếu tạo hội viên thất bại
                    if (cardId != null && !isMemberCreated) {
                        memberShipCardsDao.deleteMemberShipCard(cardId);
                    }
                } catch (SQLException rollbackEx) {
                    showAlert(Alert.AlertType.ERROR, "Lỗi", "Lỗi khi rollback: " + rollbackEx.getMessage());
                }
            }
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể thêm hội viên: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void deleteCardIfNotUsed() throws SQLException {
        // Xóa thẻ nếu hội viên chưa được tạo
        if (cardId != null && !isMemberCreated) {
            System.out.println("Attempting to delete card: " + cardId + ", isMemberCreated: " + isMemberCreated);
            memberShipCardsDao.deleteMemberShipCard(cardId);
            System.out.println("Đã xóa thẻ do hội viên không được tạo: " + cardId);
        } else {
            System.out.println("Không xóa thẻ: cardId=" + cardId + ", isMemberCreated=" + isMemberCreated);
        }
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    // Chuyển về màn hình chính
    @FXML
    public void switchHome(javafx.scene.input.MouseEvent event) throws IOException {
        try {
            deleteCardIfNotUsed();
        } catch (SQLException e) {
            System.out.println("Lỗi khi xóa thẻ trong switchHome: " + e.getMessage());
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Lỗi khi xóa thẻ: " + e.getMessage());
        }
        scenceController.switchToHelloView(new ActionEvent(event.getSource(), event.getTarget()));
    }

    // Các phương thức chuyển scene khác
    @FXML
    public void switchToHoiVienFullInfo(ActionEvent event) throws IOException {
        try {
            deleteCardIfNotUsed();
        } catch (SQLException e) {
            System.out.println("Lỗi khi xóa thẻ trong switchToHoiVienFullInfo: " + e.getMessage());
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Lỗi khi xóa thẻ: " + e.getMessage());
        }
        scenceController.switchToHoiVienFullInfo(event);
    }

    @FXML
    public void SwitchTolisthoivien(ActionEvent event) throws IOException {
        try {
            deleteCardIfNotUsed();
        } catch (SQLException e) {
            System.out.println("Lỗi khi xóa thẻ trong SwitchTolisthoivien: " + e.getMessage());
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Lỗi khi xóa thẻ: " + e.getMessage());
        }
        scenceController.switchToListHoiVien(event);
    }

    @FXML
    public void switchToRegister(ActionEvent event, String CardId) throws IOException {
        try {
            deleteCardIfNotUsed();
        } catch (SQLException e) {
            System.out.println("Lỗi khi xóa thẻ trong switchToRegister: " + e.getMessage());
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Lỗi khi xóa thẻ: " + e.getMessage());
        }
        scenceController.switchToRegister(event, CardId);
    }

    @FXML
    public void SwitchTothiet_bi(ActionEvent event) throws IOException {
        try {
            deleteCardIfNotUsed();
        } catch (SQLException e) {
            System.out.println("Lỗi khi xóa thẻ trong SwitchTothiet_bi: " + e.getMessage());
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Lỗi khi xóa thẻ: " + e.getMessage());
        }
        scenceController.switchToThietBi(event);
    }

    @FXML
    public void switchToThietBiInfoFull(ActionEvent event) throws IOException {
        try {
            deleteCardIfNotUsed();
        } catch (SQLException e) {
            System.out.println("Lỗi khi xóa thẻ trong switchToThietBiInfoFull: " + e.getMessage());
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Lỗi khi xóa thẻ: " + e.getMessage());
        }
        scenceController.switchToThietBiInfoFull(event);
    }

    @FXML
    public void switchToThietBiRegis(ActionEvent event) throws IOException {
        try {
            deleteCardIfNotUsed();
        } catch (SQLException e) {
            System.out.println("Lỗi khi xóa thẻ trong switchToThietBiRegis: " + e.getMessage());
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Lỗi khi xóa thẻ: " + e.getMessage());
        }
        scenceController.switchToThietBiRegis(event);
    }

    @FXML
    public void SwitchTothongke(ActionEvent event) throws IOException {
        try {
            deleteCardIfNotUsed();
        } catch (SQLException e) {
            System.out.println("Lỗi khi xóa thẻ trong SwitchTothongke: " + e.getMessage());
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Lỗi khi xóa thẻ: " + e.getMessage());
        }
        scenceController.switchToThongKe(event);
    }

    @FXML
    public void SwitchTotrainerList(ActionEvent event) throws IOException {
        try {
            deleteCardIfNotUsed();
        } catch (SQLException e) {
            System.out.println("Lỗi khi xóa thẻ trong SwitchTotrainerList: " + e.getMessage());
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Lỗi khi xóa thẻ: " + e.getMessage());
        }
        scenceController.SwitchTotrainerList(event);
    }

    @FXML
    public void switchToMemberCardList(ActionEvent event) throws IOException {
        try {
            deleteCardIfNotUsed();
        } catch (SQLException e) {
            System.out.println("Lỗi khi xóa thẻ trong switchToMemberCardList: " + e.getMessage());
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Lỗi khi xóa thẻ: " + e.getMessage());
        }
        scenceController.switchToMemberCardList(event);
    }

    @FXML
    public void switchToCardAdd(ActionEvent event) throws IOException {
        try {
            deleteCardIfNotUsed();
        } catch (SQLException e) {
            System.out.println("Lỗi khi xóa thẻ trong switchToCardAdd: " + e.getMessage());
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Lỗi khi xóa thẻ: " + e.getMessage());
        }
        scenceController.SwitchToCardAdd(event);
    }

    @FXML
    public void switchToProduct(ActionEvent event) throws IOException {
        try {
            deleteCardIfNotUsed();
        } catch (SQLException e) {
            System.out.println("Lỗi khi xóa thẻ trong switchToProduct: " + e.getMessage());
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Lỗi khi xóa thẻ: " + e.getMessage());
        }
        scenceController.SwitchtoProduct(event);
    }

    @FXML
    public void switchToProductRegis(ActionEvent event) throws IOException {
        try {
            deleteCardIfNotUsed();
        } catch (SQLException e) {
            System.out.println("Lỗi khi xóa thẻ trong switchToProductRegis: " + e.getMessage());
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Lỗi khi xóa thẻ: " + e.getMessage());
        }
        scenceController.SwitchtoProductRegis(event);
    }

    @FXML
    public void switchToPTRegis(ActionEvent event) throws IOException {
        try {
            deleteCardIfNotUsed();
        } catch (SQLException e) {
            System.out.println("Lỗi khi xóa thẻ trong switchToPTRegis: " + e.getMessage());
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Lỗi khi xóa thẻ: " + e.getMessage());
        }
        scenceController.SwitchToPT_Regis(event);
    }
}