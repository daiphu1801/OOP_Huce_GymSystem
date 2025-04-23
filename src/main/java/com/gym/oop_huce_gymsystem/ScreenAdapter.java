package com.gym.oop_huce_gymsystem;

import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.Region;
import javafx.scene.image.ImageView;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class ScreenAdapter {
    private static final double DEFAULT_WIDTH = 1280.0;
    private static final double DEFAULT_HEIGHT = 720.0;

    /**
     * Điều chỉnh scene để fit với kích thước màn hình của người dùng
     */
    public static void adaptToScreen(Stage stage, Scene scene) {
        // Lấy kích thước màn hình của người dùng
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();

        // Thiết lập kích thước stage bằng với kích thước màn hình
        stage.setWidth(screenBounds.getWidth());
        stage.setHeight(screenBounds.getHeight());

        // Đặt stage ở góc trên bên trái của màn hình
        stage.setX(screenBounds.getMinX());
        stage.setY(screenBounds.getMinY());

        // Thiết lập kích thước scene bằng với kích thước màn hình
        scene.getRoot().prefWidth(screenBounds.getWidth());
        scene.getRoot().prefHeight(screenBounds.getHeight());

        // Tìm và điều chỉnh hình ảnh nền và các thành phần giao diện
        adaptLayoutComponents(scene.getRoot(), screenBounds);

        // Điều chỉnh tất cả các TableView trong scene
        scene.getRoot().applyCss();
        scene.getRoot().layout();

        // Sử dụng LayoutManager để điều chỉnh các TableView
        LayoutManager.adjustAllTableViews(scene);

        // Không cho phép người dùng thay đổi kích thước cửa sổ
        stage.setResizable(false);

        System.out.println("Screen adapted: " + screenBounds.getWidth() + "x" + screenBounds.getHeight());
    }

    /**
     * Điều chỉnh các thành phần giao diện để phù hợp với kích thước màn hình
     */
    private static void adaptLayoutComponents(Parent parent, Rectangle2D screenBounds) {
        if (parent instanceof StackPane) {
            StackPane stackPane = (StackPane) parent;

            // Thiết lập lại kích thước cho StackPane gốc
            stackPane.setPrefWidth(screenBounds.getWidth());
            stackPane.setPrefHeight(screenBounds.getHeight());

            // Tìm và điều chỉnh các thành phần con
            for (int i = 0; i < stackPane.getChildren().size(); i++) {
                if (stackPane.getChildren().get(i) instanceof StackPane) {
                    StackPane bgPane = (StackPane) stackPane.getChildren().get(i);
                    bgPane.setPrefWidth(screenBounds.getWidth());
                    bgPane.setPrefHeight(screenBounds.getHeight());

                    // Tìm và điều chỉnh ImageView trong StackPane con
                    for (int j = 0; j < bgPane.getChildren().size(); j++) {
                        if (bgPane.getChildren().get(j) instanceof ImageView) {
                            ImageView imageView = (ImageView) bgPane.getChildren().get(j);

                            // Điều chỉnh kích thước của ImageView để lấp đầy màn hình
                            imageView.setFitWidth(screenBounds.getWidth());
                            imageView.setFitHeight(screenBounds.getHeight());
                            imageView.setPreserveRatio(false);

                            System.out.println("Background image adjusted to: " +
                                    imageView.getFitWidth() + "x" + imageView.getFitHeight());
                        }
                    }
                } else if (stackPane.getChildren().get(i) instanceof HBox) {
                    HBox hbox = (HBox) stackPane.getChildren().get(i);

                    // Thiết lập lại kích thước cho HBox chính
                    hbox.setPrefWidth(screenBounds.getWidth());
                    hbox.setPrefHeight(screenBounds.getHeight());

                    // Đảm bảo các thành phần con của HBox sẽ được hiển thị đúng
                    adaptHBoxChildren(hbox, screenBounds);
                }
            }
        }
    }

    private static void adaptHBoxChildren(HBox hbox, Rectangle2D screenBounds) {
        for (javafx.scene.Node node : hbox.getChildren()) {
            if (node instanceof Region) {
                Region region = (Region) node;

                // Đảm bảo tỷ lệ chiều cao được giữ nguyên
                region.setPrefHeight(screenBounds.getHeight());

                // Nếu là thành phần có thuộc tính HBox.hgrow="ALWAYS", đảm bảo nó sẽ lấp đầy không gian còn lại
                if (HBox.getHgrow(region) == javafx.scene.layout.Priority.ALWAYS) {
                    region.setPrefWidth(Region.USE_COMPUTED_SIZE);
                }

                // Đệ quy xử lý các VBox có chứa TableView
                if (region instanceof VBox) {
                    adaptVBoxWithTableView((VBox) region, screenBounds);
                }
            }
        }
    }

    /**
     * Điều chỉnh VBox chứa TableView
     */
    private static void adaptVBoxWithTableView(VBox vbox, Rectangle2D screenBounds) {
        for (javafx.scene.Node node : vbox.getChildren()) {
            if (node instanceof TableView) {
                TableView<?> tableView = (TableView<?>) node;

                // Thiết lập VBox để giãn theo chiều cao
                VBox.setVgrow(tableView, javafx.scene.layout.Priority.ALWAYS);

                // Thiết lập TableView để lấp đầy không gian khả dụng
                tableView.setPrefHeight(screenBounds.getHeight() * 0.7);
                tableView.setMinHeight(300);  // Một giá trị tối thiểu để tránh TableView quá nhỏ

                System.out.println("TableView height adjusted to: " + tableView.getPrefHeight());
            } else if (node instanceof VBox) {
                // Đệ quy nếu có VBox lồng nhau
                adaptVBoxWithTableView((VBox) node, screenBounds);
            }
        }
    }

    /**
     * Trả về kích thước màn hình của người dùng
     */
    public static Rectangle2D getScreenBounds() {
        return Screen.getPrimary().getVisualBounds();
    }

    /**
     * Trả về kích thước mặc định của ứng dụng
     */
    public static double[] getDefaultDimensions() {
        return new double[]{DEFAULT_WIDTH, DEFAULT_HEIGHT};
    }
}