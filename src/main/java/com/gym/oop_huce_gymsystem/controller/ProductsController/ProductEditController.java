package com.gym.oop_huce_gymsystem.controller.ProductsController;

import com.gym.oop_huce_gymsystem.model.Products;
import com.gym.oop_huce_gymsystem.ScenceController;
import com.gym.oop_huce_gymsystem.service.ProductsService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ProductEditController implements Initializable {

    @FXML private TextField productIdField;
    @FXML private TextField nameField;
    @FXML private TextField priceField;
    @FXML private TextField quantityField;
    @FXML private TextField quantity_soldField;
    @FXML private Button backButton;
    @FXML private Button saveButton;

    private final ScenceController scenceController;
    private final ProductsService productsService;
    private int productId;
    private boolean isInitialized;

    public ProductEditController() {
        this.scenceController = new ScenceController();
        this.productsService = new ProductsService();
        this.isInitialized = false;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (productIdField == null) {
            System.out.println("[ProductEditController] Lỗi: productIdField không được inject từ FXML.");
        } else {
            System.out.println("[ProductEditController] productIdField đã được inject thành công.");
        }

        isInitialized = true;
        if (productId != 0) {
            loadProductData();
        }
    }

    public void setProductId(int productId) {
        this.productId = productId;
        if (isInitialized && productIdField != null) {
            loadProductData();
        } else {
            System.out.println("[ProductEditController] Chờ initialize hoàn tất để load dữ liệu với productId: " + productId);
        }
    }

    private void loadProductData() {
        try {
            Products product = productsService.getProductById(productId);
            if (product != null) {
                productIdField.setText(String.valueOf(product.getProductId()));
                nameField.setText(product.getName() != null ? product.getName() : "");
                priceField.setText(product.getPrice() != null ? product.getPrice() : "");
                quantityField.setText(String.valueOf(product.getQuantity()));
                quantity_soldField.setText(String.valueOf(product.getQuantitySold()));
            } else {
                showAlert(Alert.AlertType.WARNING, "Cảnh báo", "Không tìm thấy sản phẩm với ID: " + productId);
                clearFields();
            }
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Lỗi khi lấy dữ liệu sản phẩm: " + e.getMessage());
            e.printStackTrace();
            clearFields();
        }
    }

    private void clearFields() {
        productIdField.setText("");
        nameField.setText("");
        priceField.setText("");
        quantityField.setText("");
        quantity_soldField.setText("");
    }

    @FXML
    private void saveChanges(ActionEvent event) {
        try {
            // Thu thập dữ liệu từ TextField
            String name = nameField.getText().trim();
            String price = priceField.getText().trim();
            String quantityStr = quantityField.getText().trim();
            String quantitySoldStr = quantity_soldField.getText().trim();

            // Kiểm tra cơ bản để tránh gửi dữ liệu rỗng
            if (name.isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "Cảnh báo", "Tên sản phẩm không được để trống.");
                return;
            }

            // Chuyển đổi quantity và quantitySold
            int quantity;
            try {
                quantity = Integer.parseInt(quantityStr);
            } catch (NumberFormatException e) {
                showAlert(Alert.AlertType.WARNING, "Cảnh báo", "Số lượng sản phẩm phải là một số nguyên hợp lệ.");
                return;
            }

            int quantitySold;
            try {
                quantitySold = Integer.parseInt(quantitySoldStr);
            } catch (NumberFormatException e) {
                showAlert(Alert.AlertType.WARNING, "Cảnh báo", "Số lượng đã bán phải là một số nguyên hợp lệ.");
                return;
            }

            // Tạo đối tượng Products
            Products updatedProduct = new Products(
                    productId,
                    name,
                    price,
                    quantity,
                    quantitySold
            );

            // Gọi service để lưu
            productsService.updateProduct(updatedProduct);
            showAlert(Alert.AlertType.INFORMATION, "Thành công", "Cập nhật sản phẩm thành công!");
            scenceController.SwitchtoProduct(event); // Quay lại danh sách sản phẩm
        } catch (IllegalArgumentException e) {
            showAlert(Alert.AlertType.WARNING, "Cảnh báo", e.getMessage());
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Lỗi khi lưu sản phẩm: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
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
    public void switchToProductRegis(ActionEvent event) throws IOException {
        scenceController.SwitchtoProductRegis(event);
    }

    @FXML
    public void switchToPTRegis(ActionEvent event) throws IOException {
        scenceController.SwitchToPT_Regis(event);
    }
}