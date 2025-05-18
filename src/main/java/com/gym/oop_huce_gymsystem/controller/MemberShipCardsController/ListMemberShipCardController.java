package com.gym.oop_huce_gymsystem.controller.MemberShipCardsController;

import com.gym.oop_huce_gymsystem.ScenceController;
import com.gym.oop_huce_gymsystem.model.MemberShipCards;
import com.gym.oop_huce_gymsystem.service.MemberShipCardsService;
import javafx.application.Platform;
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
import java.text.DecimalFormat;
import java.util.List;
import java.util.ResourceBundle;

public class ListMemberShipCardController implements Initializable {

    @FXML private TableView<MemberShipCards> memberCardTable;
    @FXML private TableColumn<MemberShipCards, String> cardIDColumn;
    @FXML private TableColumn<MemberShipCards, Double> priceColumn;
    @FXML private TableColumn<MemberShipCards, String> trainingPackageColumn;
    @FXML private TableColumn<MemberShipCards, String> cardTypeColumn;
    @FXML private TableColumn<MemberShipCards, String> cardRegisDateColumn;
    @FXML private TableColumn<MemberShipCards, String> cardExpireDateColumn;
    @FXML private TextField searchField;

    private final MemberShipCardsService memberShipCardsService;
    private ObservableList<MemberShipCards> memberCardList;
    private final ScenceController scenceController;

    public ListMemberShipCardController() {
        this.memberShipCardsService = new MemberShipCardsService(new com.gym.oop_huce_gymsystem.dao.MemberShipCardsDao());
        this.scenceController = new ScenceController();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Thiết lập kích thước tối thiểu cho các cột
        cardIDColumn.setMinWidth(100);
        priceColumn.setMinWidth(120); // Tăng độ rộng để chứa số định dạng
        trainingPackageColumn.setMinWidth(120);
        cardTypeColumn.setMinWidth(100);
        cardRegisDateColumn.setMinWidth(120);
        cardExpireDateColumn.setMinWidth(120);

        // Thiết lập ánh xạ cột với thuộc tính của MemberShipCards
        cardIDColumn.setCellValueFactory(new PropertyValueFactory<>("cardId"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        trainingPackageColumn.setCellValueFactory(new PropertyValueFactory<>("trainingPackage"));
        cardTypeColumn.setCellValueFactory(new PropertyValueFactory<>("cardType"));
        cardRegisDateColumn.setCellValueFactory(new PropertyValueFactory<>("registrationDate"));
        cardExpireDateColumn.setCellValueFactory(new PropertyValueFactory<>("expiryDate"));

        // Định dạng cột giá tiền
        priceColumn.setCellFactory(column -> {
            return new TableCell<MemberShipCards, Double>() {
                private final DecimalFormat decimalFormat = new DecimalFormat("#,###");

                @Override
                protected void updateItem(Double price, boolean empty) {
                    super.updateItem(price, empty);
                    if (empty || price == null) {
                        setText(null);
                    } else {
                        setText(decimalFormat.format(price) + " đ");
                    }
                }
            };
        });

        // Thiết lập cell factory với text màu trắng rõ ràng
        cardIDColumn.setCellFactory(column -> createStringTableCell());
        trainingPackageColumn.setCellFactory(column -> createStringTableCell());
        cardTypeColumn.setCellFactory(column -> createStringTableCell());
        cardRegisDateColumn.setCellFactory(column -> createStringTableCell());
        cardExpireDateColumn.setCellFactory(column -> createStringTableCell());

        // Tải dữ liệu ban đầu
        loadMemberCardData();

        // Thiết lập tính năng tìm kiếm
        searchField.textProperty().addListener((obs, oldVal, newVal) -> filterMemberCards(newVal));

        // Đảm bảo bảng được style đúng
        memberCardTable.getStyleClass().add("memberTable");
    }

    @FXML
    public void handleDoubleClick(MouseEvent event) {
        if (event.getClickCount() == 2) {
            MemberShipCards selected = memberCardTable.getSelectionModel().getSelectedItem();
            if (selected == null) {
                System.out.println("[ListMemberShipCardController] Lỗi: Không có thẻ thành viên nào được chọn để xem chi tiết.");
                showAlert(Alert.AlertType.WARNING, "Chưa chọn thẻ", "Vui lòng chọn thẻ để xem thông tin chi tiết.");
                return;
            }

            System.out.println("[ListMemberShipCardController] Nhấp đúp vào thẻ: " + selected.getCardId());
            try {
                scenceController.switchToMemberCardDetail(new ActionEvent(event.getSource(), null), selected.getCardId());
                System.out.println("[ListMemberShipCardController] Đã chuyển sang chi tiết thẻ thành công.");
            } catch (Exception e) {
                System.out.println("[ListMemberShipCardController] Lỗi khi chuyển sang chi tiết thẻ: " + e.getMessage());
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể mở giao diện chi tiết: " + e.getMessage());
            }
        }
    }

    private <T> TableCell<MemberShipCards, T> createStringTableCell() {
        return new TableCell<MemberShipCards, T>() {
            @Override
            protected void updateItem(T item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(item.toString());
                    setStyle("-fx-text-fill: white;"); // Đảm bảo text màu trắng
                }
            }
        };
    }

    private void loadMemberCardData() {
        try {
            List<MemberShipCards> cards = memberShipCardsService.getAllMemberShipCards();
            memberCardList = FXCollections.observableArrayList(cards);

            if (cards.isEmpty()) {
                System.out.println("Danh sách thẻ thành viên trống!");
            } else {
                System.out.println("Đã tải " + cards.size() + " thẻ thành viên.");
                for (MemberShipCards card : cards) {
                    System.out.println("Thẻ: " + card.getCardId() + ", " + card.getPrice() + ", " +
                            card.getTrainingPackage() + ", " + card.getCardType() + ", " +
                            card.getRegistrationDate() + ", " + card.getExpiryDate());
                }
            }

            Platform.runLater(() -> {
                memberCardTable.setItems(memberCardList);
                memberCardTable.refresh();
                System.out.println("Số dòng trong bảng: " + memberCardTable.getItems().size());
            });

        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Không tải được dữ liệu: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void filterMemberCards(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            memberCardTable.setItems(memberCardList);
        } else {
            String lowerKeyword = keyword.trim().toLowerCase();
            ObservableList<MemberShipCards> filtered = memberCardList.filtered(c ->
                    c.getCardId() != null && c.getCardId().toLowerCase().contains(lowerKeyword)
            );
            memberCardTable.setItems(filtered);
        }
        memberCardTable.refresh();
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    private void handleDeleteMemberShipCard(ActionEvent event) {
        MemberShipCards selected = memberCardTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "Chưa chọn thẻ", "Vui lòng chọn thẻ cần xóa.");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Xác nhận xóa");
        confirm.setHeaderText(null);
        confirm.setContentText("Bạn có chắc muốn xóa thẻ " + selected.getCardId() + "?");
        confirm.showAndWait().ifPresent(resp -> {
            if (resp == ButtonType.OK) {
                try {
                    memberShipCardsService.deleteMemberShipCard(selected.getCardId());
                    memberCardList.remove(selected);
                    memberCardTable.refresh();
                    showAlert(Alert.AlertType.INFORMATION, "Thành công", "Đã xóa thẻ thành viên.");
                } catch (Exception e) {
                    showAlert(Alert.AlertType.ERROR, "Lỗi", "Xóa thất bại: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        });
    }

    @FXML
    public void switchHome(javafx.scene.input.MouseEvent event) throws IOException {
        ActionEvent actionEvent = new ActionEvent(event.getSource(), event.getTarget());
        scenceController.switchToHelloView(actionEvent);
    }

    @FXML
    public void SwitchTohelloview(ActionEvent event) throws IOException {
        scenceController.switchToHelloView(event);
    }

    @FXML
    public void SwitchTohoivienfullinfo(ActionEvent event) throws IOException {
        scenceController.switchToHoiVienFullInfo(event);
    }

    @FXML
    public void SwitchTolisthoivien(ActionEvent event) throws IOException {
        scenceController.switchToListHoiVien(event);
    }

    @FXML
    public void SwitchToregister(ActionEvent event,String CardId) throws IOException {
        scenceController.switchToRegister(event,CardId);
    }

    @FXML
    public void SwitchTothiet_bi(ActionEvent event) throws IOException {
        scenceController.switchToThietBi(event);
    }

    @FXML
    public void SwitchTothietbi_info_full(ActionEvent event) throws IOException {
        scenceController.switchToThietBiInfoFull(event);
    }

    @FXML
    public void SwitchTothietbi_regis(ActionEvent event) throws IOException {
        scenceController.switchToThietBiRegis(event);
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
    public void SwitchToCardAdd(ActionEvent event) throws IOException {
        scenceController.SwitchToCardAdd(event);
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
}