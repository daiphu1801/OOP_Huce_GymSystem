package com.gym.oop_huce_gymsystem;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.Screen;
import javafx.geometry.Rectangle2D;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        // Lấy kích thước màn hình của người dùng
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));

        // Tạo scene với kích thước mặc định 1280x720
        Scene scene = new Scene(fxmlLoader.load(), 1280, 720);

        stage.setTitle("Gym Management System");

        // Thiết lập scene cho stage trước khi áp dụng các tùy chỉnh khác

        stage.setScene(scene);

        // Áp dụng scale để fit với màn hình
        ScreenAdapter.adaptToScreen(stage, scene);

        // Sử dụng maximized để tự động điều chỉnh kích thước với màn hình
        stage.setMaximized(true);

        // Không cho phép người dùng thay đổi kích thước cửa sổ
        stage.setResizable(false);

        // Hiển thị stage
        stage.show();

        // Thông báo kích thước scene để debug
        System.out.println("Scene width: " + scene.getWidth());
        System.out.println("Scene height: " + scene.getHeight());
        System.out.println("Screen width: " + screenBounds.getWidth());
        System.out.println("Screen height: " + screenBounds.getHeight());
    }

    public static void main(String[] args) {
        launch();
    }
}