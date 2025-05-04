package com.gym.oop_huce_gymsystem.controller.MemberShipCardsController;

import com.gym.oop_huce_gymsystem.ScenceController;
import com.gym.oop_huce_gymsystem.dao.MemberShipCardsDao;
import com.gym.oop_huce_gymsystem.model.MemberShipCards;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class MemberShipCardDetailsController implements Initializable {

    @FXML private Label cardIdLabel;
    @FXML private Label priceLabel;
    @FXML private Label trainingPackageLabel;
    @FXML private Label cardTypeLabel;
    @FXML private Label registrationDateLabel;
    @FXML private Label expiryDateLabel;
    @FXML private Button backButton;
    @FXML private Button editInfoButton;

    private final ScenceController scenceController;
    private final MemberShipCardsDao memberShipCardDao;
    private MemberShipCards currentCard;
    private String cardId;

    public MemberShipCardDetailsController() {
        this.scenceController = new ScenceController();
        this.memberShipCardDao = new MemberShipCardsDao();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (cardIdLabel == null) {
            System.out.println("[MemberShipCardsDetailsController] Lỗi: cardIdLabel không được inject từ FXML.");
        } else {
            System.out.println("[MemberShipCardsDetailsController] cardIdLabel đã được inject thành công.");
        }

        if (cardId != null && !cardId.isEmpty()) {
            loadCardData();
        }
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
        if (cardIdLabel != null) {
            loadCardData();
        } else {
            System.out.println("[MemberShipCardsDetailsController] Chờ initialize hoàn tất để load dữ liệu.");
        }
    }

    private void loadCardData() {
        try {
            this.currentCard = memberShipCardDao.getMemberShipCardById(cardId);
            if (currentCard != null) {
                cardIdLabel.setText(currentCard.getCardId());
                priceLabel.setText(String.valueOf(currentCard.getPrice()));
                trainingPackageLabel.setText(currentCard.getTrainingPackage());
                cardTypeLabel.setText(currentCard.getCardType());
                registrationDateLabel.setText(currentCard.getRegistrationDate().toString());
                expiryDateLabel.setText(currentCard.getExpiryDate().toString());
            } else {
                System.out.println("[MemberShipCardsDetailsController] Không tìm thấy thẻ thành viên với ID: " + cardId);
            }
        } catch (SQLException e) {
            System.out.println("[MemberShipCardsDetailsController] Lỗi khi lấy dữ liệu thẻ thành viên từ database: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    public void switchHome(javafx.scene.input.MouseEvent event) throws IOException {
        ActionEvent actionEvent = new ActionEvent(event.getSource(), event.getTarget());
        scenceController.switchToHelloView(actionEvent);
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
    public void SwitchtoProduct(ActionEvent event) throws IOException {
        scenceController.SwitchtoProduct(event);
    }

    @FXML
    public void SwitchtoProductRegis(ActionEvent event) throws IOException {
        scenceController.SwitchtoProductRegis(event);
    }

    @FXML
    public void SwitchToPT_Regis(ActionEvent event) throws IOException {
        scenceController.SwitchToPT_Regis(event);
    }

    @FXML
    public void SwitchToCardAdd(ActionEvent event) throws IOException {
        scenceController.SwitchToCardAdd(event);
    }

    @FXML
    public void switchToEdit(ActionEvent event) throws IOException {
        System.out.println("[MemberShipCardsDetailsController] Chuyển sang giao diện sửa thông tin cho thẻ: " + (currentCard != null ? currentCard.getCardId() : "N/A"));
        scenceController.switchToCardEdit(event, cardId);
    }
}