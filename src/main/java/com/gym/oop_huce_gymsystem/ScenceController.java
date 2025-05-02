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
import com.gym.oop_huce_gymsystem.controller.HoiVienController.*;

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

    public HoiVienFullInfoController switchToHoiVienFullInfo(ActionEvent event, int memberId) throws IOException {
        System.out.println("[ScenceController] Bắt đầu chuyển sang HoiVienFullInfoController với memberId: " + memberId);
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/gym/oop_huce_gymsystem/hoivien_full_info.fxml"));
            if (loader.getLocation() == null) {
                System.out.println("[ScenceController] Lỗi: Không tìm thấy tệp HoiVienFullInfo.fxml");
                throw new IOException("Không tìm thấy tệp HoiVienFullInfo.fxml");
            }
            Parent root = loader.load();
            HoiVienFullInfoController controller = loader.getController();
            if (controller == null) {
                System.out.println("[ScenceController] Lỗi: Không thể lấy HoiVienFullInfoController từ loader.");
                throw new IOException("Không thể lấy controller từ loader.");
            }
            controller.setMemberId(memberId);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
            System.out.println("[ScenceController] Chuyển sang HoiVienFullInfoController thành công.");
            return controller;
        } catch (Exception e) {
            System.out.println("[ScenceController] Lỗi khi tải HoiVienFullInfo.fxml: " + e.getMessage());
            throw e;
        }
    }

    public HoiVienEditController switchToHoiVienEdit(ActionEvent event, int memberId) throws IOException {
        System.out.println("[ScenceController] Bắt đầu chuyển sang HoiVienEditController với memberId: " + memberId);
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/gym/oop_huce_gymsystem/HoiVienEdit.fxml"));
            if (loader.getLocation() == null) {
                System.out.println("[ScenceController] Lỗi: Không tìm thấy tệp HoiVienEdit.fxml");
                throw new IOException("Không tìm thấy tệp HoiVienEdit.fxml");
            }
            Parent root = loader.load();
            HoiVienEditController controller = loader.getController();
            if (controller == null) {
                System.out.println("[ScenceController] Lỗi: Không thể lấy HoiVienEditController từ loader.");
                throw new IOException("Không thể lấy controller từ loader.");
            }
            controller.setMemberId(memberId);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
            System.out.println("[ScenceController] Chuyển sang HoiVienEditController thành công.");
            return controller;
        } catch (Exception e) {
            System.out.println("[ScenceController] Lỗi khi tải HoiVienEdit.fxml: " + e.getMessage());
            throw e;
        }
    }

    @FXML
    public void switchHome (javafx.scene.input.MouseEvent event) throws IOException {
        ActionEvent actionEvent = new ActionEvent(event.getSource(), event.getTarget());
        switchToHelloView(actionEvent);
    }

    @FXML
    public void switchToHelloView(ActionEvent event) throws IOException {
        setupScene("hello-view.fxml", event);
    }

    @FXML
    public void switchToMemberCardList(ActionEvent event) throws IOException {
        setupScene("memberCardList.fxml", event);
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
    private void switchtoProduct(ActionEvent event) throws IOException {
        setupScene("product.fxml", event);
    }

    @FXML
    private void switchtoProductRegis(ActionEvent event) throws IOException {
        setupScene("productRegis.fxml", event);
    }
    @FXML
    private void switchtoCardAdd(ActionEvent event) throws IOException {
        setupScene("cardAdd.fxml", event);
    }
    @FXML
    public void SwitchTohelloview(ActionEvent event) throws IOException {
        switchToHelloView(event);
    }

//    @FXML
//    public void SwitchTohoivienfullinfo(ActionEvent event) throws IOException {
//        switchToHoiVienFullInfo(event);
//    }

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

    @FXML
    public void switchToMemberCard(ActionEvent event) throws IOException {
        switchToMemberCardList(event);
    }
    @FXML
    public void SwitchToCardAdd(ActionEvent event) throws IOException {
        switchtoCardAdd(event);
    }

    @FXML
    public void SwitchtoProduct(ActionEvent event) throws IOException {
        switchtoProduct(event);
    }
    @FXML
    public void SwitchtoProductRegis(ActionEvent event) throws IOException {
        switchtoProductRegis(event);
    }
    @FXML
    public void SwitchToPT_Regis(ActionEvent event) throws IOException {
        switchToPTdangky(event);
    }
}

