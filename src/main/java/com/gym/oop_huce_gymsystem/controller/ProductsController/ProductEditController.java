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

    @FXML private TextField nameField;
    @FXML private TextField priceField;
    @FXML private TextField quantityField;
    @FXML private TextField quantity_soldField;
    @FXML private Button backButton;
    @FXML private Button saveButton;

    private final ScenceController scenceController;
    private final ProductsService productsService;
    private String productId;
    private boolean isInitialized;

    public ProductEditController() {
        this.scenceController = new ScenceController();
        this.productsService = new ProductsService();
        this.isInitialized = false;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("[ProductEditController] Bắt đầu initialize...");
        if (nameField == null || priceField == null || quantityField == null || quantity_soldField == null) {
            System.out.println("[ProductEditController] Lỗi: Một hoặc nhiều TextField không được inject từ FXML.");
        } else {
            System.out.println("[ProductEditController] Tất cả TextField đã được inject thành công.");
        }
        isInitialized = true;
        if (productId != null) {
            loadProductData();
        }
    }

    public void setProductId(String productId) {
        this.productId = productId;
        System.out.println("[ProductEditController] Đặt productId: " + productId);
        if (isInitialized) {
            loadProductData();
        } else {
            System.out.println("[ProductEditController] Chờ initialize hoàn tất để load dữ liệu với productId: " + productId);
        }
    }

    private void loadProductData() {
        if (productId == null || productId.trim().isEmpty()) {
            System.out.println("[ProductEditController] Lỗi: productId không hợp lệ (null hoặc rỗng).");
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Không có ID sản phẩm để tải dữ liệu.");
            clearFields();
            return;
        }

        try {
            System.out.println("[ProductEditController] Tải dữ liệu cho productId: " + productId);
            Products product = productsService.getProductById(productId);
            if (product != null) {
                nameField.setText(product.getName() != null ? product.getName() : "");
                priceField.setText(String.valueOf(product.getPrice()));
                quantityField.setText(String.valueOf(product.getQuantity()));
                quantity_soldField.setText(String.valueOf(product.getQuantitySold()));
                System.out.println("[ProductEditController] Đã tải dữ liệu: " + product);
            } else {
                System.out.println("[ProductEditController] Không tìm thấy sản phẩm với ID: " + productId);
                showAlert(Alert.AlertType.WARNING, "Cảnh báo", "Không tìm thấy sản phẩm với ID: " + productId);
                clearFields();
            }
        } catch (SQLException e) {
            System.out.println("[ProductEditController] Lỗi SQL khi tải dữ liệu: " + e.getMessage());
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Lỗi khi lấy dữ liệu sản phẩm: " + e.getMessage());
            e.printStackTrace();
            clearFields();
        }
    }

    private void clearFields() {
        nameField.setText("");
        priceField.setText("");
        quantityField.setText("");
        quantity_soldField.setText("");
    }

    @FXML
    private void saveChanges(ActionEvent event) {
        try {
            String name = nameField.getText().trim();
            String priceStr = priceField.getText().trim();
            String quantityStr = quantityField.getText().trim();
            String quantitySoldStr = quantity_soldField.getText().trim();

            if (name.isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "Cảnh báo", "Tên sản phẩm không được để trống.");
                return;
            }
            if (name.length() > 100) {
                showAlert(Alert.AlertType.WARNING, "Cảnh báo", "Tên sản phẩm không được dài quá 100 ký tự.");
                return;
            }

            double price;
            try {
                price = Double.parseDouble(priceStr);
                if (price < 0) {
                    showAlert(Alert.AlertType.WARNING, "Cảnh báo", "Giá sản phẩm không được âm.");
                    return;
                }
                if (price > 1_000_000) {
                    showAlert(Alert.AlertType.WARNING, "Cảnh báo", "Giá sản phẩm không được vượt quá 1,000,000.");
                    return;
                }
            } catch (NumberFormatException e) {
                showAlert(Alert.AlertType.WARNING, "Cảnh báo", "Giá sản phẩm phải là một số hợp lệ.");
                return;
            }

            int quantity;
            try {
                quantity = Integer.parseInt(quantityStr);
                if (quantity < 0) {
                    showAlert(Alert.AlertType.WARNING, "Cảnh báo", "Số lượng sản phẩm không được âm.");
                    return;
                }
                if (quantity > 10_000) {
                    showAlert(Alert.AlertType.WARNING, "Cảnh báo", "Số lượng sản phẩm không được vượt quá 10,000.");
                    return;
                }
            } catch (NumberFormatException e) {
                showAlert(Alert.AlertType.WARNING, "Cảnh báo", "Số lượng sản phẩm phải là một số nguyên hợp lệ.");
                return;
            }

            int quantitySold;
            try {
                quantitySold = Integer.parseInt(quantitySoldStr);
                if (quantitySold < 0) {
                    showAlert(Alert.AlertType.WARNING, "Cảnh báo", "Số lượng đã bán không được âm.");
                    return;
                }
                if (quantitySold > quantity) {
                    showAlert(Alert.AlertType.WARNING, "Cảnh báo", "Số lượng đã bán không được lớn hơn số lượng tồn kho.");
                    return;
                }
                if (quantitySold > 10_000) {
                    showAlert(Alert.AlertType.WARNING, "Cảnh báo", "Số lượng đã bán không được vượt quá 10,000.");
                    return;
                }
            } catch (NumberFormatException e) {
                showAlert(Alert.AlertType.WARNING, "Cảnh báo", "Số lượng đã bán phải là một số nguyên hợp lệ.");
                return;
            }

            Products updatedProduct = new Products(
                    productId,
                    name,
                    price,
                    quantity,
                    quantitySold
            );

            productsService.updateProduct(updatedProduct);
            showAlert(Alert.AlertType.INFORMATION, "Thành công", "Cập nhật sản phẩm thành công!");
            scenceController.SwitchtoProduct(event);
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