package com.gym.oop_huce_gymsystem.controller.RevenuaController;

import com.gym.oop_huce_gymsystem.ScenceController;
import com.gym.oop_huce_gymsystem.service.RevenueService;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class RevenueController implements Initializable {

    @FXML
    private BarChart<String, Number> barChart;

    @FXML
    private CategoryAxis xAxis;

    @FXML
    private NumberAxis yAxis;

    @FXML
    private Button btnMonth;

    @FXML
    private Button btnQuarter;

    private final RevenueService revenueService;
    private final ScenceController scenceController;

    public RevenueController() throws SQLException {
        this.revenueService = new RevenueService();
        this.scenceController = new ScenceController();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("=== Khởi tạo RevenueController ===");

        // Kiểm tra các thành phần UI
        if (barChart == null) {
            System.out.println("LỖI: barChart không được khởi tạo từ FXML");
            return;
        }
        if (xAxis == null || yAxis == null) {
            System.out.println("LỖI: Trục x hoặc y không được khởi tạo từ FXML");
            return;
        }

        System.out.println("Tất cả thành phần UI đã được khởi tạo thành công");

        // Thiết lập sự kiện cho các nút
        btnMonth.setOnAction(event -> showMonthlyRevenue());
        btnQuarter.setOnAction(event -> showQuarterlyRevenue());

        // Thiết lập biểu đồ
        setupChart();

        // Hiển thị dữ liệu doanh thu tháng mặc định
        showMonthlyRevenue();
    }

    private void setupChart() {
        // Thiết lập thuộc tính cơ bản cho biểu đồ
        barChart.setTitle("Biểu đồ doanh thu");
        barChart.setLegendVisible(true);
        barChart.setAnimated(true);

        // Đặt khoảng cách giữa các nhóm cột
        barChart.setCategoryGap(10);
        barChart.setBarGap(3);

        System.out.println("Đã thiết lập biểu đồ thành công");
    }

    private void showMonthlyRevenue() {
        System.out.println("=== BẮT ĐẦU HIỂN THỊ DOANH THU THÁNG ===");

        if (barChart == null) {
            System.out.println("LỖI: barChart không khả dụng");
            return;
        }

        // Xóa dữ liệu cũ
        barChart.getData().clear();

        // Thiết lập nhãn trục
        xAxis.setLabel("Tháng");
        yAxis.setLabel("Doanh thu (Triệu VNĐ)");
        barChart.setTitle("Doanh thu theo tháng năm 2025");

        // Tạo các series dữ liệu
        XYChart.Series<String, Number> membershipSeries = new XYChart.Series<>();
        membershipSeries.setName("Doanh thu thẻ tập");

        XYChart.Series<String, Number> productSeries = new XYChart.Series<>();
        productSeries.setName("Doanh thu sản phẩm");

        try {
            // Lấy dữ liệu từ service
            System.out.println("Đang lấy dữ liệu doanh thu tháng từ service...");
            Map<String, Map<String, BigDecimal>> monthlyRevenue = revenueService.getMonthlyRevenue(2025);

            System.out.println("Dữ liệu thô từ service: " + monthlyRevenue);

            // Tạo dữ liệu đầy đủ cho 12 tháng
            Map<String, Map<String, BigDecimal>> fullMonthlyRevenue = createFullMonthlyData(monthlyRevenue);

            // Thêm dữ liệu vào series
            for (int month = 1; month <= 12; month++) {
                String period = "Tháng " + month;
                Map<String, BigDecimal> revenues = fullMonthlyRevenue.get(period);

                BigDecimal membershipAmount = revenues.getOrDefault("MEMBERSHIP", BigDecimal.ZERO);
                BigDecimal productAmount = revenues.getOrDefault("PRODUCT", BigDecimal.ZERO);

                System.out.println(String.format("%s - Membership: %.2f, Product: %.2f",
                        period, membershipAmount.doubleValue(), productAmount.doubleValue()));

                membershipSeries.getData().add(new XYChart.Data<>(period, membershipAmount.doubleValue()));
                productSeries.getData().add(new XYChart.Data<>(period, productAmount.doubleValue()));
            }

            // Thêm series vào biểu đồ
            barChart.getData().addAll(membershipSeries, productSeries);

            System.out.println("Đã thêm " + membershipSeries.getData().size() + " điểm dữ liệu cho membership");
            System.out.println("Đã thêm " + productSeries.getData().size() + " điểm dữ liệu cho product");
            System.out.println("Tổng số series trong biểu đồ: " + barChart.getData().size());

        } catch (SQLException e) {
            System.out.println("LỖI khi lấy dữ liệu doanh thu tháng: " + e.getMessage());
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Không tải được dữ liệu doanh thu: " + e.getMessage());
        }

        // Làm mới biểu đồ
        refreshChart();
        System.out.println("=== KẾT THÚC HIỂN THỊ DOANH THU THÁNG ===");
    }

    private void showQuarterlyRevenue() {
        System.out.println("=== BẮT ĐẦU HIỂN THỊ DOANH THU QUÝ ===");

        if (barChart == null) {
            System.out.println("LỖI: barChart không khả dụng");
            return;
        }

        // Xóa dữ liệu cũ
        barChart.getData().clear();

        // Thiết lập nhãn trục
        xAxis.setLabel("Quý");
        yAxis.setLabel("Doanh thu (Triệu VNĐ)");

        // Tạo các series dữ liệu
        XYChart.Series<String, Number> membershipSeries = new XYChart.Series<>();
        membershipSeries.setName("Doanh thu thẻ tập");

        XYChart.Series<String, Number> productSeries = new XYChart.Series<>();
        productSeries.setName("Doanh thu sản phẩm");

        try {
            // Lấy dữ liệu từ service
            System.out.println("Đang lấy dữ liệu doanh thu quý từ service...");
            Map<String, Map<String, BigDecimal>> quarterlyRevenue = revenueService.getQuarterlyRevenue(2025);

            System.out.println("Dữ liệu thô từ service: " + quarterlyRevenue);

            // Tạo dữ liệu đầy đủ cho 4 quý
            Map<String, Map<String, BigDecimal>> fullQuarterlyRevenue = createFullQuarterlyData(quarterlyRevenue);

            // Thêm dữ liệu vào series
            for (int quarter = 1; quarter <= 4; quarter++) {
                String period = "Quý " + quarter;
                Map<String, BigDecimal> revenues = fullQuarterlyRevenue.get(period);

                BigDecimal membershipAmount = revenues.getOrDefault("MEMBERSHIP", BigDecimal.ZERO);
                BigDecimal productAmount = revenues.getOrDefault("PRODUCT", BigDecimal.ZERO);

                System.out.println(String.format("%s - Membership: %.2f, Product: %.2f",
                        period, membershipAmount.doubleValue(), productAmount.doubleValue()));

                membershipSeries.getData().add(new XYChart.Data<>(period, membershipAmount.doubleValue()));
                productSeries.getData().add(new XYChart.Data<>(period, productAmount.doubleValue()));
            }

            // Thêm series vào biểu đồ
            barChart.getData().addAll(membershipSeries, productSeries);

            System.out.println("Đã thêm " + membershipSeries.getData().size() + " điểm dữ liệu cho membership");
            System.out.println("Đã thêm " + productSeries.getData().size() + " điểm dữ liệu cho product");
            System.out.println("Tổng số series trong biểu đồ: " + barChart.getData().size());

        } catch (SQLException e) {
            System.out.println("LỖI khi lấy dữ liệu doanh thu quý: " + e.getMessage());
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Không tải được dữ liệu doanh thu: " + e.getMessage());
        }

        // Làm mới biểu đồ
        refreshChart();
        System.out.println("=== KẾT THÚC HIỂN THỊ DOANH THU QUÝ ===");
    }

    private Map<String, Map<String, BigDecimal>> createFullMonthlyData(Map<String, Map<String, BigDecimal>> monthlyRevenue) {
        Map<String, Map<String, BigDecimal>> fullData = new LinkedHashMap<>();

        for (int month = 1; month <= 12; month++) {
            String period = "Tháng " + month;
            Map<String, BigDecimal> revenues = new LinkedHashMap<>();

            if (monthlyRevenue.containsKey(period)) {
                revenues.putAll(monthlyRevenue.get(period));
            }

            // Đảm bảo có cả 2 loại doanh thu
            revenues.putIfAbsent("MEMBERSHIP", BigDecimal.ZERO);
            revenues.putIfAbsent("PRODUCT", BigDecimal.ZERO);

            fullData.put(period, revenues);
        }

        return fullData;
    }

    private Map<String, Map<String, BigDecimal>> createFullQuarterlyData(Map<String, Map<String, BigDecimal>> quarterlyRevenue) {
        Map<String, Map<String, BigDecimal>> fullData = new LinkedHashMap<>();

        for (int quarter = 1; quarter <= 4; quarter++) {
            String period = "Quý " + quarter;
            Map<String, BigDecimal> revenues = new LinkedHashMap<>();

            if (quarterlyRevenue.containsKey(period)) {
                revenues.putAll(quarterlyRevenue.get(period));
            }

            // Đảm bảo có cả 2 loại doanh thu
            revenues.putIfAbsent("MEMBERSHIP", BigDecimal.ZERO);
            revenues.putIfAbsent("PRODUCT", BigDecimal.ZERO);

            fullData.put(period, revenues);
        }

        return fullData;
    }

    private void refreshChart() {
        Platform.runLater(() -> {
            barChart.requestLayout();
            System.out.println("Biểu đồ đã được làm mới - Visible: " + barChart.isVisible());
            System.out.println("Kích thước biểu đồ: " + barChart.getWidth() + "x" + barChart.getHeight());
        });
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    // Phương thức debug để kiểm tra dữ liệu
    @FXML
    private void debugData() {
        try {
            System.out.println("=== DEBUG DATA ===");
            Map<String, Map<String, BigDecimal>> monthlyData = revenueService.getMonthlyRevenue(2025);
            System.out.println("Dữ liệu tháng: " + monthlyData);

            Map<String, Map<String, BigDecimal>> quarterlyData = revenueService.getQuarterlyRevenue(2025);
            System.out.println("Dữ liệu quý: " + quarterlyData);
            System.out.println("=== END DEBUG ===");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Navigation methods
    @FXML
    public void switchHome(MouseEvent event) throws IOException {
        scenceController.switchHome(event);
    }

    @FXML
    public void SwitchTolisthoivien(ActionEvent event) throws IOException {
        scenceController.SwitchTolisthoivien(event);
    }

    @FXML
    public void switchToMemberCard(ActionEvent event) throws IOException {
        scenceController.switchToMemberCard(event);
    }

    @FXML
    public void SwitchTotrainerList(ActionEvent event) throws IOException {
        scenceController.SwitchTotrainerList(event);
    }

    @FXML
    public void SwitchTothiet_bi(ActionEvent event) throws IOException {
        scenceController.SwitchTothiet_bi(event);
    }

    @FXML
    public void SwitchtoProduct(ActionEvent event) throws IOException {
        scenceController.SwitchtoProduct(event);
    }

    @FXML
    public void SwitchTothongke(ActionEvent event) throws IOException {
        scenceController.SwitchTothongke(event);
    }
}