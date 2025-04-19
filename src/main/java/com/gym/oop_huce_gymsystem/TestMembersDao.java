package com.gym.oop_huce_gymsystem;

import com.gym.oop_huce_gymsystem.dao.*;
import com.gym.oop_huce_gymsystem.model.*;
import com.gym.oop_huce_gymsystem.service.*;
import com.gym.oop_huce_gymsystem.controller.*;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;


public class TestMembersDao extends Application {
    private TableView<Members> table = new TableView<>();
    private TextField nameField = new TextField();
    private TextField phoneField = new TextField();
    private ComboBox<String> membershipCombo = new ComboBox<>();
    private DatePicker registrationDatePicker = new DatePicker();
    private DatePicker expiryDatePicker = new DatePicker();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Gym Management System");

        // Khởi tạo ComboBox
        membershipCombo.getItems().addAll("BASIC", "PREMIUM", "VIP");
        membershipCombo.setValue("BASIC");

        // Khởi tạo Controller
        MembersController controller = new MembersController(table, nameField, phoneField,
                membershipCombo, registrationDatePicker,
                expiryDatePicker);

        // Form nhập liệu
        GridPane form = new GridPane();
        form.setPadding(new Insets(10));
        form.setHgap(10);
        form.setVgap(10);
        form.add(new Label("Name:"), 0, 0);
        form.add(nameField, 1, 0);
        form.add(new Label("Phone:"), 0, 1);
        form.add(phoneField, 1, 1);
        form.add(new Label("Membership Type:"), 0, 2);
        form.add(membershipCombo, 1, 2);
        form.add(new Label("Registration Date:"), 0, 3);
        form.add(registrationDatePicker, 1, 3);
        form.add(new Label("Expiry Date:"), 0, 4);
        form.add(expiryDatePicker, 1, 4);

        // Buttons
        Button addButton = new Button("Add Member");
        addButton.setOnAction(e -> controller.addMember());

        Button updateButton = new Button("Update Member");
        updateButton.setOnAction(e -> controller.updateMember());

        Button deleteButton = new Button("Delete Member");
        deleteButton.setOnAction(e -> controller.deleteMember());

        HBox buttonBox = new HBox(10, addButton, updateButton, deleteButton);

        // Layout
        VBox layout = new VBox(10, form, buttonBox, table);
        layout.setPadding(new Insets(10));

        Scene scene = new Scene(layout, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
