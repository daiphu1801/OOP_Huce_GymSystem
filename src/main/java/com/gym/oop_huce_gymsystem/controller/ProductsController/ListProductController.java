package com.gym.oop_huce_gymsystem.controller.ProductsController;

import com.gym.oop_huce_gymsystem.model.Products;
import com.gym.oop_huce_gymsystem.service.ProductsService;
import com.gym.oop_huce_gymsystem.ScenceController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.application.Platform;
import javafx.scene.input.MouseEvent;
import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.List;
import java.util.ResourceBundle;

public class ListProductController implements Initializable {

    @FXML private TableView<Products> productTable;
    @FXML private TableColumn<Products, Integer> productListId;
    @FXML private TableColumn<Products, String> productListName;
    @FXML private TableColumn<Products, Double> productListPrice;
    @FXML private TableColumn<Products, Integer> productListQuantity;
    @FXML private TableColumn<Products, Integer> productListQuantity_sold;

    @FXML private TextField searchField;

    private final ProductsService productsService;
    private ObservableList<Products> productList;
    private final ScenceController scenceController;

    public ListProductController() {
        this.productsService = new ProductsService();
        this.scenceController = new ScenceController();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Thiết lập kích thước tối thiểu cho các cột
        productListId.setMinWidth(100);
        productListName.setMinWidth(150);
        productListPrice.setMinWidth(100);
        productListQuantity.setMinWidth(120);
        productListQuantity_sold.setMinWidth(120);

        // Thiết lập ánh xạ cột với thuộc tính của Products
        productListId.setCellValueFactory(new PropertyValueFactory<>("productId"));
        productListName.setCellValueFactory(new PropertyValueFactory<>("name"));
        productListPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        productListQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        productListQuantity_sold.setCellValueFactory(new PropertyValueFactory<>("quantitySold"));

        // Định dạng cột giá tiền
        productListPrice.setCellFactory(column -> {
            return new TableCell<Products, Double>() {
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

        // Thiết lập cell factory với text màu trắng rõ ràng cho các cột khác
        productListId.setCellFactory(column -> createStringTableCell());
        productListName.setCellFactory(column -> createStringTableCell());
        productListQuantity.setCellFactory(column -> createStringTableCell());
        productListQuantity_sold.setCellFactory(column -> createStringTableCell());

        // Tải dữ liệu ban đầu
        loadProductData();

        // Thiết lập tính năng tìm kiếm
        searchField.textProperty().addListener((obs, oldVal, newVal) -> filterProducts(newVal));

        // Đảm bảo bảng được style đúng
        productTable.getStyleClass().add("memberTable");
    }

    @FXML
    public void handleDoubleClick(MouseEvent event) {
        if (event.getClickCount() == 2) {
            Products selected = productTable.getSelectionModel().getSelectedItem();
            if (selected == null) {
                System.out.println("[ListProductController] Lỗi: Không có sản phẩm nào được chọn để xem chi tiết.");
                showAlert(Alert.AlertType.WARNING, "Chưa chọn sản phẩm", "Vui lòng chọn sản phẩm để xem thông tin chi tiết.");
                return;
            }

            System.out.println("[ListProductController] Nhấp đúp vào sản phẩm: " + selected.getName() + ", ID: " + selected.getProductId());
            try {
                scenceController.switchToProductDetail(new ActionEvent(event.getSource(), null), selected.getProductId());
                System.out.println("[ListProductController] Đã chuyển sang chi tiết sản phẩm thành công.");
            } catch (Exception e) {
                System.out.println("[ListProductController] Lỗi khi chuyển sang chi tiết sản phẩm: " + e.getMessage());
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể mở giao diện chi tiết: " + e.getMessage());
            }
        }
    }

    // Helper method để tạo TableCell với style rõ ràng
    private <T> TableCell<Products, T> createStringTableCell() {
        return new TableCell<Products, T>() {
            @Override
            protected void updateItem(T item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(item.toString());
                }
            }
        };
    }

    private void loadProductData() {
        try {
            List<Products> products = productsService.getAllProducts();
            productList = FXCollections.observableArrayList(products);

            if (products.isEmpty()) {
                System.out.println("Danh sách sản phẩm trống!");
            } else {
                System.out.println("Đã tải " + products.size() + " sản phẩm.");
                for (Products product : products) {
                    System.out.println("Sản phẩm: " + product.getProductId() + ", " + product.getName() + ", " +
                            product.getPrice() + ", " + product.getQuantity() + ", " + product.getQuantitySold());
                }
            }

            Platform.runLater(() -> {
                productTable.setItems(productList);
                productTable.refresh();
                System.out.println("Số dòng trong bảng: " + productTable.getItems().size());
            });

        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Không tải được dữ liệu: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void filterProducts(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            productTable.setItems(productList);
        } else {
            String lowerKeyword = keyword.trim().toLowerCase();
            ObservableList<Products> filtered = productList.filtered(p ->
                    p.getName() != null && p.getName().toLowerCase().contains(lowerKeyword)
            );
            productTable.setItems(filtered);
        }
        productTable.refresh();
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    private void handleDeleteProduct(ActionEvent event) {
        Products selected = productTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "Chưa chọn sản phẩm", "Vui lòng chọn sản phẩm cần xóa.");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Xác nhận xóa");
        confirm.setHeaderText(null);
        confirm.setContentText("Bạn có chắc muốn xóa sản phẩm " + selected.getName() + " (ID: " + selected.getProductId() + ")?");
        confirm.showAndWait().ifPresent(resp -> {
            if (resp == ButtonType.OK) {
                try {
                    productsService.deleteProduct(selected.getProductId());
                    productList.remove(selected);
                    productTable.refresh();
                    showAlert(Alert.AlertType.INFORMATION, "Thành công", "Đã xóa sản phẩm.");
                } catch (Exception e) {
                    showAlert(Alert.AlertType.ERROR, "Lỗi", "Xóa thất bại: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        });
    }

    @FXML
    public void switchHome(MouseEvent event) throws IOException {
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