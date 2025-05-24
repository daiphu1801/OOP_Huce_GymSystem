package com.gym.oop_huce_gymsystem.service;

import com.gym.oop_huce_gymsystem.dao.RevenueDao;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class RevenueService {
    private final RevenueDao revenueDAO;

    public RevenueService() throws SQLException {
        this.revenueDAO = new RevenueDao();
    }

    public Map<String, Map<String, BigDecimal>> getMonthlyRevenue(int year) throws SQLException {
        // Lấy doanh thu từ membership_cards
        Map<String, Map<String, BigDecimal>> membershipRevenue = revenueDAO.getMembershipRevenueByMonth(year);

        // Lấy doanh thu từ revenue (sản phẩm)
        Map<String, Map<String, BigDecimal>> productRevenue = revenueDAO.getProductRevenueByMonth(year);

        // Tổng hợp dữ liệu
        Map<String, Map<String, BigDecimal>> monthlyRevenue = new HashMap<>();
        // Tổng hợp doanh thu từ membership_cards
        for (Map.Entry<String, Map<String, BigDecimal>> entry : membershipRevenue.entrySet()) {
            String period = entry.getKey(); // Định dạng: "2025-01"
            Map<String, BigDecimal> revenues = entry.getValue();
            String displayPeriod = "Tháng " + Integer.parseInt(period.split("-")[1]);
            monthlyRevenue.computeIfAbsent(displayPeriod, k -> new HashMap<>());
            monthlyRevenue.get(displayPeriod).put("MEMBERSHIP", revenues.getOrDefault("MEMBERSHIP", BigDecimal.ZERO));
        }

        // Tổng hợp doanh thu từ revenue (sản phẩm)
        for (Map.Entry<String, Map<String, BigDecimal>> entry : productRevenue.entrySet()) {
            String period = entry.getKey(); // Định dạng: "2025-01"
            Map<String, BigDecimal> revenues = entry.getValue();
            String displayPeriod = "Tháng " + Integer.parseInt(period.split("-")[1]);
            monthlyRevenue.computeIfAbsent(displayPeriod, k -> new HashMap<>());
            monthlyRevenue.get(displayPeriod).put("PRODUCT", revenues.getOrDefault("PRODUCT", BigDecimal.ZERO));
        }

        return monthlyRevenue;
    }

    public Map<String, Map<String, BigDecimal>> getQuarterlyRevenue(int year) throws SQLException {
        Map<String, Map<String, BigDecimal>> monthlyRevenue = getMonthlyRevenue(year);
        Map<String, Map<String, BigDecimal>> quarterlyRevenue = new HashMap<>();

        // Nhóm dữ liệu theo quý
        for (Map.Entry<String, Map<String, BigDecimal>> entry : monthlyRevenue.entrySet()) {
            String monthKey = entry.getKey(); // Định dạng: "Tháng 1", "Tháng 2", ...
            Map<String, BigDecimal> revenues = entry.getValue();

            // Xác định quý từ tháng
            int month = Integer.parseInt(monthKey.split(" ")[1]);
            String quarterKey = "Quý " + ((month - 1) / 3 + 1);

            // Tổng hợp dữ liệu vào quý
            quarterlyRevenue.computeIfAbsent(quarterKey, k -> new HashMap<>());
            for (Map.Entry<String, BigDecimal> revenueEntry : revenues.entrySet()) {
                String sourceType = revenueEntry.getKey();
                BigDecimal amount = revenueEntry.getValue();
                quarterlyRevenue.get(quarterKey).merge(sourceType, amount, BigDecimal::add);
            }
        }

        return quarterlyRevenue;
    }
}