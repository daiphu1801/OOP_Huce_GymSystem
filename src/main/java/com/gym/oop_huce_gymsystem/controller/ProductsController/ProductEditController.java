package com.gym.oop_huce_gymsystem.controller.ProductsController;

import com.gym.oop_huce_gymsystem.model.Products;
import com.gym.oop_huce_gymsystem.ScenceController;
import com.gym.oop_huce_gymsystem.dao.ProductsDao;
import com.gym.oop_huce_gymsystem.service.ProductsService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import com.gym.oop_huce_gymsystem.dao.SaleTransactionDao;
import com.gym.oop_huce_gymsystem.model.SaleTransaction;
import java.time.LocalDateTime;

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
    private final ProductsDao productsDao;
    private final ProductsService productsService;
    private final SaleTransactionDao saleTransactionDao;
    private String productId;
    private Products originalProduct; // Lưu thông tin sản phẩm gốc

    public ProductEditController() {
        this.scenceController = new ScenceController();
        this.productsDao = new ProductsDao();
        this.productsService = new ProductsService();
        this.saleTransactionDao = new SaleTransactionDao();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Thiết lập prompt text
        if (quantity_soldField != null) {
            quantity_soldField.setPromptText("Nhập số lượng bán thêm");
        }
    }

    // Thiết lập productId và tải dữ liệu
    public void setProductId(String productId) {
        this.productId = productId;
        loadProductData();
    }

    // Tải dữ liệu sản phẩm từ cơ sở dữ liệu
    private void loadProductData() {
        try {
            originalProduct = productsDao.getProductById(productId);
            if (originalProduct != null) {
                // Hiển thị thông tin hiện tại để chỉnh sửa
                nameField.setText(originalProduct.getName());
                priceField.setText(String.valueOf(originalProduct.getPrice()));
                quantityField.setText(String.valueOf(originalProduct.getQuantity()));

                // Ô số lượng đã bán luôn mặc định là 0 (để nhập số bán thêm)
                quantity_soldField.setText("0");
            } else {
                showAlert(Alert.AlertType.ERROR, "Lỗi", "Không tìm thấy sản phẩm với ID: " + productId);
            }
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Không tải được dữ liệu: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Xử lý trở về danh sách sản phẩm
    @FXML
    private void SwitchtoProduct(ActionEvent event) throws IOException {
        scenceController.SwitchtoProduct(event);
    }

    // Xử lý lưu thay đổi
    @FXML
    private void saveChanges(ActionEvent event) {
        try {
            if (originalProduct == null) {
                showAlert(Alert.AlertType.ERROR, "Lỗi", "Không tìm thấy thông tin sản phẩm gốc.");
                return;
            }

            // Lấy dữ liệu từ form
            String name = nameField.getText().trim();
            String priceStr = priceField.getText().trim();
            String newQuantityStr = quantityField.getText().trim();
            String additionalSoldStr = quantity_soldField.getText().trim();

            // Validation cho tên sản phẩm
            if (name.isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "Cảnh báo", "Tên sản phẩm không được để trống.");
                return;
            }

            // Validation cho giá
            double price;
            try {
                price = Double.parseDouble(priceStr);
                if (price < 0) {
                    showAlert(Alert.AlertType.WARNING, "Cảnh báo", "Giá sản phẩm không được âm.");
                    return;
                }
            } catch (NumberFormatException e) {
                showAlert(Alert.AlertType.WARNING, "Cảnh báo", "Giá sản phẩm phải là một số hợp lệ.");
                return;
            }

            // Validation cho số lượng tồn kho mới
            int newQuantity;
            try {
                newQuantity = Integer.parseInt(newQuantityStr);
                if (newQuantity < 0) {
                    showAlert(Alert.AlertType.WARNING, "Cảnh báo", "Số lượng tồn kho không được âm.");
                    return;
                }
            } catch (NumberFormatException e) {
                showAlert(Alert.AlertType.WARNING, "Cảnh báo", "Số lượng tồn kho phải là một số nguyên hợp lệ.");
                return;
            }

            // Validation cho số lượng bán thêm
            int additionalSold;
            try {
                additionalSold = Integer.parseInt(additionalSoldStr);
                if (additionalSold < 0) {
                    showAlert(Alert.AlertType.WARNING, "Cảnh báo", "Số lượng bán thêm không được âm.");
                    return;
                }
            } catch (NumberFormatException e) {
                showAlert(Alert.AlertType.WARNING, "Cảnh báo", "Số lượng bán thêm phải là một số nguyên hợp lệ.");
                return;
            }

            // Kiểm tra xem có đủ hàng để bán không
            if (additionalSold > newQuantity) {
                showAlert(Alert.AlertType.WARNING, "Cảnh báo",
                        String.format("Số lượng bán thêm (%d) không thể lớn hơn số lượng tồn kho hiện tại (%d).",
                                additionalSold, newQuantity));
                return;
            }

            // Nếu có bán thêm sản phẩm, lưu giao dịch trước
            if (additionalSold > 0) {
                SaleTransaction transaction = new SaleTransaction(
                        productId,
                        additionalSold,
                        LocalDateTime.now()
                );
                saleTransactionDao.addSaleTransaction(transaction);
                System.out.println("[ProductEditController] Đã lưu giao dịch bán hàng: " + additionalSold + " sản phẩm");
            }

            // Tính toán số liệu cuối cùng
            int currentQuantitySold = originalProduct.getQuantitySold(); // Số đã bán trước đó
            int finalQuantitySold = currentQuantitySold + additionalSold; // Tổng số đã bán
            int finalQuantity = newQuantity - additionalSold; // Số tồn kho sau khi bán thêm

            // Tạo đối tượng Products với dữ liệu mới
            Products updatedProduct = new Products(
                    productId,
                    name,
                    price,
                    finalQuantity,     // Số lượng tồn kho cuối cùng
                    finalQuantitySold  // Tổng số lượng đã bán
            );

            // Cập nhật thông tin qua ProductsService
            productsService.updateProduct(updatedProduct);

            // Hiển thị thông báo thành công
            String message = "Cập nhật sản phẩm thành công.";
            if (additionalSold > 0) {
                message += String.format("\nĐã bán thêm %d sản phẩm.", additionalSold);
                message += String.format("\nSố lượng tồn kho còn lại: %d", finalQuantity);
                message += String.format("\nTổng số đã bán: %d", finalQuantitySold);
            }

            showAlert(Alert.AlertType.INFORMATION, "Thành công", message);
            SwitchtoProduct(event);

        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Cập nhật thất bại: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Hiển thị thông báo
    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    // Các method chuyển scene khác
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

    @FXML
    private void SwitchTothietbi_regis(ActionEvent event) throws IOException {
        scenceController.SwitchTothietbi_regis(event);
    }
}