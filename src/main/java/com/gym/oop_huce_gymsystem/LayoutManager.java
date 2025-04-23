package com.gym.oop_huce_gymsystem;

import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;

public class LayoutManager {
    public static void adjustTableViewSize(TableView<?> tableView, VBox parentVBox) {
        if (tableView == null || parentVBox == null) return;

        // Lấy kích thước màn hình
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();

        // Tính toán chiều cao khả dụng cho TableView
        // Trừ đi khoảng 100px cho các phần header và footer
        double availableHeight = screenBounds.getHeight() - 150;

        // Thiết lập chiều cao tối thiểu
        tableView.setMinHeight(availableHeight * 0.7);
        tableView.setPrefHeight(availableHeight);

        // Đảm bảo VBox cha có thể mở rộng để lấp đầy không gian
        parentVBox.setMinHeight(availableHeight);
        parentVBox.setPrefHeight(availableHeight);

        // Đặt độ rộng tối đa cho TableView
        tableView.setMaxWidth(Double.MAX_VALUE);

        // Phân bổ lại kích thước các cột
        double totalWidth = tableView.getWidth();
        double columnCount = tableView.getColumns().size();

        if (columnCount > 0 && totalWidth > 0) {
            double columnWidth = totalWidth / columnCount;
            tableView.getColumns().forEach(col -> col.setPrefWidth(columnWidth));
        }
    }

    public static void adjustAllTableViews(Scene scene) {
        scene.getRoot().lookupAll(".table-view").forEach(node -> {
            if (node instanceof TableView) {
                TableView<?> tableView = (TableView<?>) node;
                VBox parentVBox = null;

                // Tìm VBox cha gần nhất
                javafx.scene.Parent parent = tableView.getParent();
                while (parent != null) {
                    if (parent instanceof VBox) {
                        parentVBox = (VBox) parent;
                        break;
                    }
                    if (parent.getParent() instanceof javafx.scene.Parent) {
                        parent = parent.getParent();
                    } else {
                        break;
                    }
                }

                if (parentVBox != null) {
                    adjustTableViewSize(tableView, parentVBox);
                }
            }
        });
    }
}