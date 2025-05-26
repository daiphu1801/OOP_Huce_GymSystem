package com.gym.oop_huce_gymsystem.controller.ProductsController;

import com.gym.oop_huce_gymsystem.ScenceController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import com.gym.oop_huce_gymsystem.model.Products;
import com.gym.oop_huce_gymsystem.service.ProductsService;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;

public class ThemProduct_Controller {

    private final ScenceController scenceController = new ScenceController();

    @FXML
    private TextField productField;
    @FXML
    private TextField priceField;
    @FXML
    private TextField quantityField;

    private ProductsService productsService;

    @FXML
    public void initialize() {
        productsService = new ProductsService();

        // Thêm placeholder text để hướng dẫn
        productField.setPromptText("Nhập tên sản phẩm");
        priceField.setPromptText("Nhập giá sản phẩm (VND)");
        quantityField.setPromptText("Nhập số lượng ban đầu");

        // Focus vào trường đầu tiên
        productField.requestFocus();
    }

    @FXML
    private void handleSubmit(ActionEvent event) {
        try {
            // Get input values
            String name = productField.getText().trim();
            String priceStr = priceField.getText().trim();
            String quantityStr = quantityField.getText().trim();

            // Validation cho tên sản phẩm
            if (name.isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "Cảnh báo", "Tên sản phẩm không được để trống.");
                productField.requestFocus();
                return;
            }
            if (name.length() > 100) {
                showAlert(Alert.AlertType.WARNING, "Cảnh báo", "Tên sản phẩm không được dài quá 100 ký tự.");
                productField.requestFocus();
                return;
            }

            // Validation cho giá
            double price;
            try {
                if (priceStr.isEmpty()) {
                    showAlert(Alert.AlertType.WARNING, "Cảnh báo", "Giá sản phẩm không được để trống.");
                    priceField.requestFocus();
                    return;
                }
                price = Double.parseDouble(priceStr);
                if (price < 0) {
                    showAlert(Alert.AlertType.WARNING, "Cảnh báo", "Giá sản phẩm không được âm.");
                    priceField.requestFocus();
                    return;
                }
            } catch (NumberFormatException e) {
                showAlert(Alert.AlertType.WARNING, "Cảnh báo", "Giá sản phẩm phải là một số hợp lệ.");
                priceField.requestFocus();
                return;
            }

            // Validation cho số lượng
            int quantity;
            try {
                if (quantityStr.isEmpty()) {
                    showAlert(Alert.AlertType.WARNING, "Cảnh báo", "Số lượng sản phẩm không được để trống.");
                    quantityField.requestFocus();
                    return;
                }
                quantity = Integer.parseInt(quantityStr);
                if (quantity < 0) {
                    showAlert(Alert.AlertType.WARNING, "Cảnh báo", "Số lượng sản phẩm không được âm.");
                    quantityField.requestFocus();
                    return;
                }
                if (quantity > 10_000) {
                    showAlert(Alert.AlertType.WARNING, "Cảnh báo", "Số lượng sản phẩm không được vượt quá 10,000.");
                    quantityField.requestFocus();
                    return;
                }
            } catch (NumberFormatException e) {
                showAlert(Alert.AlertType.WARNING, "Cảnh báo", "Số lượng sản phẩm phải là một số nguyên hợp lệ.");
                quantityField.requestFocus();
                return;
            }

            // Số lượng đã bán luôn là 0 khi thêm mới sản phẩm
            int quantitySold = 0;

            // Tạo sản phẩm mới
            Products product = new Products(name, price, quantity, quantitySold);

            // Lưu vào database
            productsService.addProduct(product);

            // Hiển thị thông báo thành công
            String successMessage = String.format(
                    "Thêm sản phẩm thành công!\n\n" +
                            "Thông tin sản phẩm:\n" +
                            "• Tên: %s\n" +
                            "• Giá: %.0f VND\n" +
                            "• Số lượng: %d\n" +
                            "• Đã bán: %d",
                    name, price, quantity, quantitySold
            );

            showAlert(Alert.AlertType.INFORMATION, "Thành công", successMessage);

            // Xóa các trường sau khi thêm thành công
            clearFields();

            // Chuyển về danh sách sản phẩm
            SwitchtoProduct(event);

        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Lỗi khi thêm sản phẩm: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void clearFields() {
        productField.clear();
        priceField.clear();
        quantityField.clear();
        // Focus lại vào trường đầu tiên
        productField.requestFocus();
    }

    // Helper method to load FXML files
    private void loadFXML(String fxmlFile) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/gym/oop_huce_gymsystem/fxml/" + fxmlFile));
        Parent root = loader.load();
        Stage stage = (Stage) productField.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    // Helper method to show alerts
    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    private void SwitchTothietbi_regis(ActionEvent event) throws IOException {
        scenceController.SwitchTothietbi_regis(event);
    }

    @FXML
    public void switchHome(MouseEvent event) throws IOException {
        ActionEvent actionEvent = new ActionEvent(event.getSource(), event.getTarget());
        scenceController.switchToHelloView(actionEvent);
    }

    @FXML
    public void SwitchTolisthoivien(ActionEvent event) throws IOException {
        scenceController.switchToListHoiVien(event);
    }

    @FXML
    public void SwitchTotrainerList(ActionEvent event) throws IOException {
        scenceController.SwitchTotrainerList(event);
    }

    @FXML
    public void SwitchTothiet_bi(ActionEvent event) throws IOException {
        scenceController.switchToThietBi(event);
    }

    @FXML
    public void SwitchtoProduct(ActionEvent event) throws IOException {
        scenceController.SwitchtoProduct(event);
    }

    @FXML
    public void SwitchTothongke(ActionEvent event) throws IOException {
        scenceController.switchToThongKe(event);
    }

    @FXML
    public void SwitchToPT_Regis(ActionEvent event) throws IOException {
        scenceController.SwitchToPT_Regis(event);
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
    public void SwitchtoProductRegis(ActionEvent event) throws IOException {
        scenceController.SwitchtoProductRegis(event);
    }
}