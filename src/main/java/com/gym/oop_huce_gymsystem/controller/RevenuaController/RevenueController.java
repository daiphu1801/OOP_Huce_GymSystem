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
import java.util.HashMap;
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
        System.out.println("Khởi tạo RevenueController");
        if (barChart == null) {
            System.out.println("Lỗi: barChart không được khởi tạo");
        } else {
            System.out.println("barChart được khởi tạo thành công");
        }
        btnMonth.setOnAction(event -> showMonthlyRevenue());
        btnQuarter.setOnAction(event -> showQuarterlyRevenue());
        showMonthlyRevenue();
    }

    private void showMonthlyRevenue() {
        System.out.println("Hiển thị doanh thu tháng");
        if (barChart == null) {
            System.out.println("Lỗi: barChart không khả dụng");
            return;
        }
        barChart.getData().clear();
        xAxis.setLabel("Tháng");
        yAxis.setLabel("Triệu (VNĐ)");

        XYChart.Series<String, Number> membershipSeries = new XYChart.Series<>();
        membershipSeries.setName("Doanh thu thẻ tập");
        XYChart.Series<String, Number> productSeries = new XYChart.Series<>();
        productSeries.setName("Doanh thu sản phẩm");

        Map<String, Map<String, BigDecimal>> monthlyRevenue;
        try {
            monthlyRevenue = revenueService.getMonthlyRevenue(2025);
            System.out.println("Dữ liệu doanh thu tháng: " + monthlyRevenue);
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Không tải được dữ liệu doanh thu: " + e.getMessage());
            e.printStackTrace();
            monthlyRevenue = new HashMap<>();
        }

        // Đảm bảo luôn có 12 tháng, ngay cả khi không có dữ liệu
        Map<String, Map<String, BigDecimal>> fullMonthlyRevenue = new HashMap<>();
        for (int month = 1; month <= 12; month++) {
            String period = "Tháng " + month;
            fullMonthlyRevenue.put(period, monthlyRevenue.getOrDefault(period, new HashMap<>()));
            fullMonthlyRevenue.get(period).putIfAbsent("MEMBERSHIP", BigDecimal.ZERO);
            fullMonthlyRevenue.get(period).putIfAbsent("PRODUCT", BigDecimal.ZERO);
        }

        // Vẽ dữ liệu lên biểu đồ
        for (Map.Entry<String, Map<String, BigDecimal>> entry : fullMonthlyRevenue.entrySet()) {
            String period = entry.getKey();
            Map<String, BigDecimal> revenues = entry.getValue();
            System.out.println("Vẽ dữ liệu cho " + period + ": " + revenues);
            membershipSeries.getData().add(new XYChart.Data<>(period,
                    revenues.getOrDefault("MEMBERSHIP", BigDecimal.ZERO).doubleValue()));
            productSeries.getData().add(new XYChart.Data<>(period,
                    revenues.getOrDefault("PRODUCT", BigDecimal.ZERO).doubleValue()));
        }
        System.out.println("Số lượng dữ liệu membershipSeries: " + membershipSeries.getData().size());
        System.out.println("Số lượng dữ liệu productSeries: " + productSeries.getData().size());

        barChart.getData().addAll(membershipSeries, productSeries);
        System.out.println("Số lượng series trong barChart: " + barChart.getData().size());

        Platform.runLater(() -> {
            barChart.requestLayout();
            System.out.println("barChart sau khi làm mới - Visible: " + barChart.isVisible());
            System.out.println("barChart kích thước: " + barChart.getWidth() + "x" + barChart.getHeight());
        });
    }

    private void showQuarterlyRevenue() {
        System.out.println("Hiển thị doanh thu quý");
        if (barChart == null) {
            System.out.println("Lỗi: barChart không khả dụng");
            return;
        }
        barChart.getData().clear();
        xAxis.setLabel("Quý");
        yAxis.setLabel("Triệu (VNĐ)");

        XYChart.Series<String, Number> membershipSeries = new XYChart.Series<>();
        membershipSeries.setName("Doanh thu thẻ tập");
        XYChart.Series<String, Number> productSeries = new XYChart.Series<>();
        productSeries.setName("Doanh thu sản phẩm");

        Map<String, Map<String, BigDecimal>> quarterlyRevenue;
        try {
            quarterlyRevenue = revenueService.getQuarterlyRevenue(2025);
            System.out.println("Dữ liệu doanh thu quý: " + quarterlyRevenue);
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Không tải được dữ liệu doanh thu: " + e.getMessage());
            e.printStackTrace();
            quarterlyRevenue = new HashMap<>();
        }

        // Đảm bảo luôn có 4 quý, ngay cả khi không có dữ liệu
        Map<String, Map<String, BigDecimal>> fullQuarterlyRevenue = new HashMap<>();
        for (int quarter = 1; quarter <= 4; quarter++) {
            String period = "Quý " + quarter;
            fullQuarterlyRevenue.put(period, quarterlyRevenue.getOrDefault(period, new HashMap<>()));
            fullQuarterlyRevenue.get(period).putIfAbsent("MEMBERSHIP", BigDecimal.ZERO);
            fullQuarterlyRevenue.get(period).putIfAbsent("PRODUCT", BigDecimal.ZERO);
        }

        // Vẽ dữ liệu lên biểu đồ
        for (Map.Entry<String, Map<String, BigDecimal>> entry : fullQuarterlyRevenue.entrySet()) {
            String period = entry.getKey();
            Map<String, BigDecimal> revenues = entry.getValue();
            System.out.println("Vẽ dữ liệu cho " + period + ": " + revenues);
            membershipSeries.getData().add(new XYChart.Data<>(period,
                    revenues.getOrDefault("MEMBERSHIP", BigDecimal.ZERO).doubleValue()));
            productSeries.getData().add(new XYChart.Data<>(period,
                    revenues.getOrDefault("PRODUCT", BigDecimal.ZERO).doubleValue()));
        }
        System.out.println("Số lượng dữ liệu membershipSeries: " + membershipSeries.getData().size());
        System.out.println("Số lượng dữ liệu productSeries: " + productSeries.getData().size());

        barChart.getData().addAll(membershipSeries, productSeries);
        System.out.println("Số lượng series trong barChart: " + barChart.getData().size());

        Platform.runLater(() -> {
            barChart.requestLayout();
            System.out.println("barChart sau khi làm mới - Visible: " + barChart.isVisible());
            System.out.println("barChart kích thước: " + barChart.getWidth() + "x" + barChart.getHeight());
        });
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    public void switchHome(MouseEvent event) throws IOException {
        System.out.println("Chuyển về màn hình chính");
        scenceController.switchHome(event);
    }

    @FXML
    public void SwitchTolisthoivien(ActionEvent event) throws IOException {
        System.out.println("Chuyển sang danh sách hội viên");
        scenceController.SwitchTolisthoivien(event);
    }

    @FXML
    public void switchToMemberCard(ActionEvent event) throws IOException {
        System.out.println("Chuyển sang danh sách thẻ tập");
        scenceController.switchToMemberCard(event);
    }

    @FXML
    public void SwitchTotrainerList(ActionEvent event) throws IOException {
        System.out.println("Chuyển sang danh sách huấn luyện viên");
        scenceController.SwitchTotrainerList(event);
    }

    @FXML
    public void SwitchTothiet_bi(ActionEvent event) throws IOException {
        System.out.println("Chuyển sang danh sách thiết bị");
        scenceController.SwitchTothiet_bi(event);
    }

    @FXML
    public void SwitchtoProduct(ActionEvent event) throws IOException {
        System.out.println("Chuyển sang danh sách sản phẩm");
        scenceController.SwitchtoProduct(event);
    }

    @FXML
    public void SwitchTothongke(ActionEvent event) throws IOException {
        System.out.println("Chuyển sang thống kê");
        scenceController.SwitchTothongke(event);
    }
}