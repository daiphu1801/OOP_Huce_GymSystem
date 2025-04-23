package com.gym.oop_huce_gymsystem;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.control.TableView;

import java.io.IOException;

public class ScenceController {
    private Stage st;
    private Scene sc;
    private Parent root;
    @FXML
    private TableView<?> memberTable;

    @FXML
    private AnchorPane contentArea;

    // Phương thức chung để thiết lập scene với cùng cài đặt
    void setupScene(String resourceName, ActionEvent event) throws IOException {
        // Lấy stage hiện tại từ event
        st = (Stage) ((Node) event.getSource()).getScene().getWindow();

        // Load FXML
        FXMLLoader loader = new FXMLLoader(getClass().getResource(resourceName));
        root = loader.load();

        // Tạo scene mới với kích thước mặc định 1280x720
        sc = new Scene(root, 1280, 720);

        // Thiết lập scene cho stage
        st.setScene(sc);

        // Áp dụng scale để fit với màn hình
        ScreenAdapter.adaptToScreen(st, sc);

        // Force layout update
        sc.getRoot().applyCss();
        sc.getRoot().layout();

        // Điều chỉnh TableView nếu có
        if (loader.getController() instanceof ScenceController) {
            ScenceController controller = (ScenceController) loader.getController();
            if (controller.memberTable != null) {
                VBox parentVBox = findParentVBox(controller.memberTable);
                if (parentVBox != null) {
                    LayoutManager.adjustTableViewSize(controller.memberTable, parentVBox);
                }
            }
        }

        // Đảm bảo stage ở chế độ maximized
        st.setMaximized(true);
        st.setResizable(false);
        st.show();
    }

    private VBox findParentVBox(Node node) {
        Parent parent = node.getParent();
        while (parent != null) {
            if (parent instanceof VBox) {
                return (VBox) parent;
            }
            if (parent.getParent() instanceof Parent) {
                parent = parent.getParent();
            } else {
                break;
            }
        }
        return null;
    }

    @FXML
    public void initialize() {
        if (memberTable != null) {
            // Thay đổi policy để cho phép resize linh hoạt hơn
            memberTable.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);

            // Tìm VBox cha của TableView
            VBox parentVBox = findParentVBox(memberTable);

            // Thiết lập VBox.vgrow cho TableView
            if (parentVBox != null) {
                VBox.setVgrow(memberTable, javafx.scene.layout.Priority.ALWAYS);

                // Thiết lập kích thước cho TableView
                memberTable.setMaxHeight(Double.MAX_VALUE);
                memberTable.setPrefHeight(600);

                System.out.println("TableView initialized with VBox parent");
            }

            // Thiết lập kích thước cột cho TableView
            double totalWidth = memberTable.getWidth();
            int columnCount = memberTable.getColumns().size();

            if (columnCount > 0 && totalWidth > 0) {
                double columnWidth = totalWidth / columnCount;
                memberTable.getColumns().forEach(col -> col.setPrefWidth(columnWidth));
            }
        }
    }

    @FXML
    public void switchToHelloView(ActionEvent event) throws IOException {
        setupScene("hello-view.fxml", event);
    }

    @FXML
    public void switchToHoiVienFullInfo(ActionEvent event) throws IOException {
        setupScene("hoivien_full_info.fxml", event);
    }

    @FXML
    public void switchToListHoiVien(ActionEvent event) throws IOException {
        setupScene("list_hoivien.fxml", event);
    }

    @FXML
    public void switchToRegister(ActionEvent event) throws IOException {
        setupScene("register.fxml", event);
    }

    @FXML
    public void switchToThietBi(ActionEvent event) throws IOException {
        setupScene("thiet_bi.fxml", event);
    }

    @FXML
    public void switchToThietBiInfoFull(ActionEvent event) throws IOException {
        setupScene("thietbi_info_full.fxml", event);
    }

    @FXML
    public void switchToThietBiRegis(ActionEvent event) throws IOException {
        setupScene("thietbi_regis.fxml", event);
    }

    @FXML
    public void switchToThongKe(ActionEvent event) throws IOException {
        setupScene("thongke.fxml", event);
    }

    @FXML
    public void switchToPTList(ActionEvent event) throws IOException {
        setupScene("ptList.fxml", event);
    }

    @FXML
    public void switchToPTdangky(ActionEvent event) throws IOException {
        setupScene("pt_register.fxml", event);
    }

    @FXML
    public void SwitchTohelloview(ActionEvent event) throws IOException {
        switchToHelloView(event);
    }

    @FXML
    public void SwitchTohoivienfullinfo(ActionEvent event) throws IOException {
        switchToHoiVienFullInfo(event);
    }

    @FXML
    public void SwitchTolisthoivien(ActionEvent event) throws IOException {
        switchToListHoiVien(event);
    }

    @FXML
    public void SwitchToregister(ActionEvent event) throws IOException {
        switchToRegister(event);
    }

    @FXML
    public void SwitchTothiet_bi(ActionEvent event) throws IOException {
        switchToThietBi(event);
    }

    @FXML
    public void SwitchTothietbi_info_full(ActionEvent event) throws IOException {
        switchToThietBiInfoFull(event);
    }

    @FXML
    public void SwitchTothietbi_regis(ActionEvent event) throws IOException {
        switchToThietBiRegis(event);
    }

    @FXML
    public void SwitchTothongke(ActionEvent event) throws IOException {
        switchToThongKe(event);
    }

    @FXML
    public void SwitchTotrainerList(ActionEvent event) throws IOException {
        switchToPTList(event);
    }

}

