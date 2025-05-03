package com.gym.oop_huce_gymsystem.controller.ProductsController;

import com.gym.oop_huce_gymsystem.ScenceController;
import com.gym.oop_huce_gymsystem.service.ProductsService;
import com.gym.oop_huce_gymsystem.model.Products;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ProductEditController implements Initializable {
    @FXML
    private TextField productIdField;
    @FXML private TextField nameField;
    @FXML private TextField priceField;
    @FXML private TextField quantityField;
    @FXML private TextField quantity_soldField;
    @FXML private Button backButton;
    @FXML private Button saveButton;

    private final ScenceController scenceController;
    private final ProductsService productsService;
    private int productId;

    public ProductEditController(ScenceController scenceController, ProductsService productsService) {
        this.scenceController = new ScenceController();
        this.productsService = new ProductsService();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Không cần khởi tạo các thành phần đặc biệt
    }

    public void setProductId(int productId) {
        this.productId = productId;
        loadProductData();
    }

    private void loadProductData() {
        try {
            Products product = productsService.getProductById(productId);
            if (product != null) {
                productIdField.setText(String.valueOf(product.getProductId()));
                nameField.setText(product.getName());
                priceField.setText(product.getPrice());
                quantityField.setText(String.valueOf(product.getQuantity()));
                quantity_soldField.setText(String.valueOf(product.getQuantitySold()));
            } else {
                showAlert(Alert.AlertType.ERROR, "Lỗi", "Không tìm thấy sản phẩm với ID: " + productId);
            }
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Không tải được dữ liệu: " + e.getMessage());
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            showAlert(Alert.AlertType.ERROR, "Lỗi", e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void backToList(ActionEvent event) throws IOException {
        scenceController.SwitchtoProduct(event);
    }

    @FXML
    private void saveChanges(ActionEvent event) {
        try {
            // Lấy giá trị từ text fields
            int id = Integer.parseInt(productIdField.getText());
            String name = nameField.getText();
            String price = priceField.getText();
            int quantity = Integer.parseInt(quantityField.getText());
            int quantitySold = Integer.parseInt(quantity_soldField.getText());

            // Tạo đối tượng sản phẩm
            Products updatedProduct = new Products(
                    id,
                    name,
                    price,
                    quantity,
                    quantitySold
            );

            // Cập nhật sản phẩm - việc xác thực sẽ được thực hiện trong service
            productsService.updateProduct(updatedProduct);
            showAlert(Alert.AlertType.INFORMATION, "Thành công", "Cập nhật thông tin sản phẩm thành công.");
            backToList(event);
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Vui lòng nhập đúng định dạng số.");
        } catch (IllegalArgumentException e) {
            showAlert(Alert.AlertType.ERROR, "Lỗi", e.getMessage());
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Cập nhật thất bại: " + e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể quay lại danh sách: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Cập nhật thất bại: " + e.getMessage());
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
}