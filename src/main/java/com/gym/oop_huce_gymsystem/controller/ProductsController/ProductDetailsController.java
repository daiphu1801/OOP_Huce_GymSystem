package com.gym.oop_huce_gymsystem.controller.ProductsController;

import com.gym.oop_huce_gymsystem.model.Products;
import com.gym.oop_huce_gymsystem.model.SaleTransaction;
import com.gym.oop_huce_gymsystem.ScenceController;
import com.gym.oop_huce_gymsystem.dao.ProductsDao;
import com.gym.oop_huce_gymsystem.dao.SaleTransactionDao;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.application.Platform;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;

public class ProductDetailsController implements Initializable {

    @FXML private Label productIdLabel;
    @FXML private Label nameLabel;
    @FXML private Label priceLabel;
    @FXML private Label quantityLabel;
    @FXML private Label quantitySoldLabel;
    @FXML private Button backButton;
    @FXML private Button editInfoButton;

    // Bảng hiển thị lịch sử giao dịch
    @FXML private TableView<SaleTransaction> transactionTable;
    @FXML private TableColumn<SaleTransaction, Integer> transactionIdColumn;
    @FXML private TableColumn<SaleTransaction, Integer> quantitySoldColumn;
    @FXML private TableColumn<SaleTransaction, LocalDateTime> transactionDateColumn;

    private final ScenceController scenceController;
    private final ProductsDao productsDao;
    private final SaleTransactionDao saleTransactionDao;
    private Products currentProduct;
    private String productId;

    public ProductDetailsController() {
        this.scenceController = new ScenceController();
        this.productsDao = new ProductsDao();
        this.saleTransactionDao = new SaleTransactionDao();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("[ProductDetailsController] Bắt đầu initialize...");

        // Kiểm tra các component đã được inject
        checkFXMLInjection();

        // Thiết lập bảng giao dịch
        setupTransactionTable();

        // Load dữ liệu nếu productId đã được set
        if (productId != null) {
            System.out.println("[ProductDetailsController] ProductId đã có: " + productId + ", bắt đầu load dữ liệu...");
            loadProductData();
        } else {
            System.out.println("[ProductDetailsController] ProductId chưa được set, chờ setProductId() được gọi.");
        }
    }

    // Kiểm tra FXML injection
    private void checkFXMLInjection() {
        System.out.println("[ProductDetailsController] Kiểm tra FXML injection:");
        System.out.println("  - productIdLabel: " + (productIdLabel != null ? "OK" : "NULL"));
        System.out.println("  - nameLabel: " + (nameLabel != null ? "OK" : "NULL"));
        System.out.println("  - priceLabel: " + (priceLabel != null ? "OK" : "NULL"));
        System.out.println("  - quantityLabel: " + (quantityLabel != null ? "OK" : "NULL"));
        System.out.println("  - quantitySoldLabel: " + (quantitySoldLabel != null ? "OK" : "NULL"));
        System.out.println("  - transactionTable: " + (transactionTable != null ? "OK" : "NULL"));
        System.out.println("  - transactionIdColumn: " + (transactionIdColumn != null ? "OK" : "NULL"));
        System.out.println("  - quantitySoldColumn: " + (quantitySoldColumn != null ? "OK" : "NULL"));
        System.out.println("  - transactionDateColumn: " + (transactionDateColumn != null ? "OK" : "NULL"));
    }

    private void setupTransactionTable() {
        if (transactionTable == null) {
            System.out.println("[ProductDetailsController] Cảnh báo: transactionTable không được inject từ FXML.");
            return;
        }

        if (transactionIdColumn == null || quantitySoldColumn == null || transactionDateColumn == null) {
            System.out.println("[ProductDetailsController] Cảnh báo: Một hoặc nhiều cột của bảng không được inject từ FXML.");
            return;
        }

        try {
            System.out.println("[ProductDetailsController] Đang thiết lập bảng giao dịch...");

            // Thiết lập kích thước cột
            transactionIdColumn.setMinWidth(100);
            quantitySoldColumn.setMinWidth(120);
            transactionDateColumn.setMinWidth(180);

            // ĐÂY LÀ ĐIỂM QUAN TRỌNG: Kiểm tra và thiết lập đúng PropertyValueFactory
            System.out.println("[ProductDetailsController] Thiết lập PropertyValueFactory cho các cột...");

            // Thiết lập ánh xạ cột với thuộc tính của SaleTransaction
            transactionIdColumn.setCellValueFactory(new PropertyValueFactory<>("transactionId"));
            quantitySoldColumn.setCellValueFactory(new PropertyValueFactory<>("quantitySold"));
            transactionDateColumn.setCellValueFactory(new PropertyValueFactory<>("transactionDate"));

            System.out.println("[ProductDetailsController] Đã thiết lập ánh xạ cột thành công.");

            // Định dạng cột ngày tháng
            transactionDateColumn.setCellFactory(column -> {
                return new TableCell<SaleTransaction, LocalDateTime>() {
                    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

                    @Override
                    protected void updateItem(LocalDateTime item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty || item == null) {
                            setText(null);
                        } else {
                            setText(item.format(formatter));
                        }
                    }
                };
            });

            // Thiết lập cell factory cho các cột khác để đảm bảo hiển thị đúng
            transactionIdColumn.setCellFactory(column -> createTableCell());
            quantitySoldColumn.setCellFactory(column -> createTableCell());

            // Thiết lập chế độ selection cho bảng
            transactionTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

            System.out.println("[ProductDetailsController] Đã thiết lập bảng giao dịch thành công.");

        } catch (Exception e) {
            System.out.println("[ProductDetailsController] Lỗi khi thiết lập bảng giao dịch: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Helper method để tạo TableCell với style rõ ràng
    private <T> TableCell<SaleTransaction, T> createTableCell() {
        return new TableCell<SaleTransaction, T>() {
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

    public void setProductId(String productId) {
        System.out.println("[ProductDetailsController] setProductId được gọi với ID: " + productId);
        this.productId = productId;

        // Nếu initialize đã hoàn tất thì load dữ liệu ngay
        if (productIdLabel != null) {
            System.out.println("[ProductDetailsController] Initialize đã hoàn tất, bắt đầu load dữ liệu...");
            loadProductData();
        } else {
            System.out.println("[ProductDetailsController] Initialize chưa hoàn tất, sẽ load dữ liệu sau khi initialize xong.");
        }
    }

    private void loadProductData() {
        System.out.println("[ProductDetailsController] Bắt đầu loadProductData cho productId: " + productId);

        try {
            this.currentProduct = productsDao.getProductById(productId);
            if (currentProduct != null) {
                // Cập nhật UI trên JavaFX Application Thread
                Platform.runLater(() -> {
                    try {
                        // Kiểm tra labels trước khi set text
                        if (productIdLabel != null) {
                            productIdLabel.setText(String.valueOf(currentProduct.getProductId()));
                        }
                        if (nameLabel != null) {
                            nameLabel.setText(currentProduct.getName());
                        }
                        if (priceLabel != null) {
                            priceLabel.setText(String.format("%.0f đ", currentProduct.getPrice()));
                        }
                        if (quantityLabel != null) {
                            quantityLabel.setText(String.valueOf(currentProduct.getQuantity()));
                        }
                        if (quantitySoldLabel != null) {
                            quantitySoldLabel.setText(String.valueOf(currentProduct.getQuantitySold()));
                        }

                        System.out.println("[ProductDetailsController] Đã cập nhật thông tin sản phẩm trên UI: " + currentProduct.getName());

                        // Load lịch sử giao dịch
                        loadTransactionHistory();
                    } catch (Exception e) {
                        System.out.println("[ProductDetailsController] Lỗi khi cập nhật UI: " + e.getMessage());
                        e.printStackTrace();
                    }
                });
            } else {
                System.out.println("[ProductDetailsController] Không tìm thấy sản phẩm với ID: " + productId);
                Platform.runLater(() -> {
                    showAlert(Alert.AlertType.ERROR, "Lỗi", "Không tìm thấy sản phẩm với ID: " + productId);
                });
            }
        } catch (SQLException e) {
            System.out.println("[ProductDetailsController] Lỗi khi lấy dữ liệu sản phẩm từ database: " + e.getMessage());
            e.printStackTrace();
            Platform.runLater(() -> {
                showAlert(Alert.AlertType.ERROR, "Lỗi", "Không tải được dữ liệu sản phẩm: " + e.getMessage());
            });
        }
    }

    private void loadTransactionHistory() {
        System.out.println("[ProductDetailsController] Bắt đầu loadTransactionHistory cho productId: " + productId);

        if (transactionTable == null) {
            System.out.println("[ProductDetailsController] transactionTable là null, không thể load dữ liệu.");
            return;
        }

        try {
            List<SaleTransaction> transactions = saleTransactionDao.getTransactionsByProductId(productId);
            System.out.println("[ProductDetailsController] Lấy được " + transactions.size() + " giao dịch từ database.");

            // Log chi tiết các giao dịch để debug
            for (SaleTransaction transaction : transactions) {
                System.out.println(String.format("[ProductDetailsController] Giao dịch ID: %d, Sản phẩm: %s, Số lượng: %d, Ngày: %s",
                        transaction.getTransactionId(),
                        transaction.getProductId(),
                        transaction.getQuantitySold(),
                        transaction.getTransactionDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"))));
            }

            Platform.runLater(() -> {
                try {
                    // Tạo ObservableList và set vào bảng
                    ObservableList<SaleTransaction> transactionList = FXCollections.observableArrayList(transactions);
                    transactionTable.setItems(transactionList);

                    // Force refresh
                    transactionTable.refresh();

                    System.out.println("[ProductDetailsController] Đã set " + transactionList.size() + " giao dịch vào bảng.");
                    System.out.println("[ProductDetailsController] Số items trong bảng sau khi set: " + transactionTable.getItems().size());

                    // Debug thêm thông tin về bảng
                    System.out.println("[ProductDetailsController] Số cột trong bảng: " + transactionTable.getColumns().size());
                    if (!transactionTable.getColumns().isEmpty()) {
                        System.out.println("[ProductDetailsController] Tên các cột:");
                        for (int i = 0; i < transactionTable.getColumns().size(); i++) {
                            TableColumn<?, ?> col = transactionTable.getColumns().get(i);
                            System.out.println("  Cột " + i + ": " + col.getText());
                        }
                    }

                    // Nếu không có dữ liệu, hiển thị thông báo
                    if (transactions.isEmpty()) {
                        System.out.println("[ProductDetailsController] Không có giao dịch nào cho sản phẩm này.");
                        // Có thể thêm placeholder hoặc message vào bảng
                    }

                } catch (Exception e) {
                    System.out.println("[ProductDetailsController] Lỗi khi cập nhật bảng giao dịch: " + e.getMessage());
                    e.printStackTrace();
                }
            });

        } catch (SQLException e) {
            System.out.println("[ProductDetailsController] Lỗi khi load lịch sử giao dịch từ database: " + e.getMessage());
            e.printStackTrace();
            Platform.runLater(() -> {
                showAlert(Alert.AlertType.ERROR, "Lỗi", "Không tải được lịch sử giao dịch: " + e.getMessage());
            });
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

    // Phương thức refresh dữ liệu (có thể gọi từ bên ngoài)
    public void refreshData() {
        System.out.println("[ProductDetailsController] refreshData được gọi.");
        loadProductData();
    }

    // Method để test tạo dữ liệu dummy cho debug
    @FXML
    private void createTestTransaction() {
        if (transactionTable != null) {
            System.out.println("[ProductDetailsController] Tạo giao dịch test để debug...");
            ObservableList<SaleTransaction> testData = FXCollections.observableArrayList();
            testData.add(new SaleTransaction(1, "PD0001", 5, LocalDateTime.now()));
            testData.add(new SaleTransaction(2, "PD0001", 3, LocalDateTime.now().minusHours(1)));

            transactionTable.setItems(testData);
            transactionTable.refresh();

            System.out.println("[ProductDetailsController] Đã tạo " + testData.size() + " giao dịch test.");
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
        System.out.println("[ProductDetailsController] Chuyển sang giao diện sửa thông tin cho sản phẩm: " + (currentProduct != null ? currentProduct.getName() : "N/A"));
        scenceController.switchToProductEdit(event, productId);
    }
}