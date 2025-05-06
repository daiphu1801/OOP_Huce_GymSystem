package com.gym.oop_huce_gymsystem.controller.ProductsController;

import com.gym.oop_huce_gymsystem.model.Products;
import com.gym.oop_huce_gymsystem.ScenceController;
import com.gym.oop_huce_gymsystem.dao.ProductsDao;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ProductDetailsController implements Initializable {

    @FXML private Label productIdLabel;
    @FXML private Label nameLabel;
    @FXML private Label priceLabel;
    @FXML private Label quantityLabel;
    @FXML private Label quantitySoldLabel;
    @FXML private Button backButton;
    @FXML private Button editInfoButton;

    private final ScenceController scenceController;
    private final ProductsDao productsDao;
    private Products currentProduct;
    private int productId;

    public ProductDetailsController() {
        this.scenceController = new ScenceController();
        this.productsDao = new ProductsDao();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (productIdLabel == null) {
            System.out.println("[ProductFullInfoController] Lỗi: productIdLabel không được inject từ FXML.");
        } else {
            System.out.println("[ProductFullInfoController] productIdLabel đã được inject thành công.");
        }

        if (productId != 0) {
            loadProductData();
        }
    }

    public void setProductId(int productId) {
        this.productId = productId;
        if (productIdLabel != null) {
            loadProductData();
        } else {
            System.out.println("[ProductFullInfoController] Chờ initialize hoàn tất để load dữ liệu.");
        }
    }

    private void loadProductData() {
        try {
            this.currentProduct = productsDao.getProductById(productId);
            if (currentProduct != null) {
                productIdLabel.setText(String.valueOf(currentProduct.getProductId()));
                nameLabel.setText(currentProduct.getName());
                priceLabel.setText(String.valueOf(currentProduct.getPrice()));
                quantityLabel.setText(String.valueOf(currentProduct.getQuantity()));
                quantitySoldLabel.setText(String.valueOf(currentProduct.getQuantitySold()));
            } else {
                System.out.println("[ProductFullInfoController] Không tìm thấy sản phẩm với ID: " + productId);
            }
        } catch (SQLException e) {
            System.out.println("[ProductFullInfoController] Lỗi khi lấy dữ liệu sản phẩm từ database: " + e.getMessage());
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
    public void switchToProductRegis(ActionEvent event) throws IOException {
        scenceController.SwitchtoProductRegis(event);
    }

    @FXML
    public void switchToPTRegis(ActionEvent event) throws IOException {
        scenceController.SwitchToPT_Regis(event);
    }

    @FXML
    public void switchToEditProduct(ActionEvent event) throws IOException {
        System.out.println("[ProductFullInfoController] Chuyển sang giao diện sửa thông tin cho sản phẩm: " + (currentProduct != null ? currentProduct.getName() : "N/A"));
        scenceController.switchToProductEdit(event, productId);
    }
}