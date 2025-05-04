package com.gym.oop_huce_gymsystem.controller.ProductsController;

import com.gym.oop_huce_gymsystem.ScenceController;
import com.gym.oop_huce_gymsystem.dao.ProductsDao;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import com.gym.oop_huce_gymsystem.model.Products;
import com.gym.oop_huce_gymsystem.service.ProductsService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.awt.*;
import java.io.IOException;

public class ThemProduct_Controller {

    private final ScenceController scenceController = new ScenceController();

    @FXML
    private TextField productField;
    @FXML
    private TextField priceField;
    @FXML
    private TextField quantityField;
    @FXML
    private TextField quantity_soldField;

    private ProductsService productsService;

    @FXML
    public void initialize() {
        productsService = new ProductsService();
    }

    @FXML
    private void handleSubmit(ActionEvent event) {
        try {
            // Get input values
            String name = productField.getText();
            String price = priceField.getText();
            String quantityStr = quantityField.getText();
            String quantitySoldStr = quantity_soldField.getText();

            // Parse numeric fields
            double pricee = price.isEmpty() ? 0 : Double.parseDouble(price);
            int quantity = quantityStr.isEmpty() ? 0 : Integer.parseInt(quantityStr);
            int quantitySold = quantitySoldStr.isEmpty() ? 0 : Integer.parseInt(quantitySoldStr);

            // Create product and save
            Products product = new Products(name, pricee, quantity, quantitySold);
            productsService.addProduct(product);

            // Show success message and navigate back
            showAlert(Alert.AlertType.INFORMATION, "Thành công", "Thêm sản phẩm thành công!");
            SwitchtoProduct(event);

        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Số lượng hoặc số lượng đã bán phải là số nguyên hợp lệ.");
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Lỗi khi thêm sản phẩm: " + e.getMessage());
        }
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
    public void switchHome (javafx.scene.input.MouseEvent event) throws IOException {
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
    public void SwitchToregister(ActionEvent event) throws IOException {
        scenceController.switchToRegister(event);
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
