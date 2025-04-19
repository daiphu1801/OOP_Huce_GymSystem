package com.gym.oop_huce_gymsystem.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import java.time.LocalDate;
import com.gym.oop_huce_gymsystem.model.*;
import com.gym.oop_huce_gymsystem.dao.*;
import com.gym.oop_huce_gymsystem.controller.*;
import com.gym.oop_huce_gymsystem.service.*;

public class MembersController {
    private final MembersService memberService;
    private final ObservableList<Members> memberData = FXCollections.observableArrayList();
    private final TableView<Members> tableView;
    private final TextField nameField;
    private final TextField phoneField;
    private final ComboBox<String> membershipCombo;
    private final DatePicker registrationDatePicker;
    private final DatePicker expiryDatePicker;

    public MembersController(TableView<Members> tableView, TextField nameField, TextField phoneField,
                            ComboBox<String> membershipCombo, DatePicker registrationDatePicker,
                            DatePicker expiryDatePicker) {
        this.memberService = new MembersService();
        this.tableView = tableView;
        this.nameField = nameField;
        this.phoneField = phoneField;
        this.membershipCombo = membershipCombo;
        this.registrationDatePicker = registrationDatePicker;
        this.expiryDatePicker = expiryDatePicker;
        initializeTable();
        loadMembers();
    }

    public MembersController(MembersService memberService, TableView<Members> tableView, TextField nameField, TextField phoneField, ComboBox<String> membershipCombo, DatePicker registrationDatePicker, DatePicker expiryDatePicker) {
        this.memberService = memberService;
        this.tableView = tableView;
        this.nameField = nameField;
        this.phoneField = phoneField;
        this.membershipCombo = membershipCombo;
        this.registrationDatePicker = registrationDatePicker;
        this.expiryDatePicker = expiryDatePicker;
    }

    private void initializeTable() {
        TableColumn<Members, Integer> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("memberId"));

        TableColumn<Members, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Members, String> phoneColumn = new TableColumn<>("Phone");
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));

        TableColumn<Members, String> membershipColumn = new TableColumn<>("Membership Type");
        membershipColumn.setCellValueFactory(new PropertyValueFactory<>("membershipType"));

        TableColumn<Members, LocalDate> regDateColumn = new TableColumn<>("Registration Date");
        regDateColumn.setCellValueFactory(new PropertyValueFactory<>("registrationDate"));

        TableColumn<Members, LocalDate> expDateColumn = new TableColumn<>("Expiry Date");
        expDateColumn.setCellValueFactory(new PropertyValueFactory<>("expiryDate"));

        tableView.getColumns().setAll(idColumn, nameColumn, phoneColumn, membershipColumn,
                regDateColumn, expDateColumn);
        tableView.setItems(memberData);

        // Khi chọn một hội viên, điền thông tin vào các trường nhập
        tableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                nameField.setText(newSelection.getName());
                phoneField.setText(newSelection.getPhone());
                membershipCombo.setValue(newSelection.getMembershipType());
                registrationDatePicker.setValue(newSelection.getRegistrationDate());
            }
        });
    }

    public void addMember() {
        try {
            Members member = new Members(
                    nameField.getText(),
                    phoneField.getText(),
                    membershipCombo.getValue(),
                    registrationDatePicker.getValue()
            );
            memberService.addMember(member);
            loadMembers();
            clearFields();
            showAlert(Alert.AlertType.INFORMATION, "Thành công", "Hội viên đã được thêm.");
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Lỗi", e.getMessage());
        }
    }

    public void updateMember() {
        Members selected = tableView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "Cảnh báo", "Vui lòng chọn một hội viên để sửa.");
            return;
        }
        try {
            selected.setName(nameField.getText());
            selected.setPhone(phoneField.getText());
            selected.setMembershipType(membershipCombo.getValue());
            selected.setRegistrationDate(registrationDatePicker.getValue());
            memberService.updateMember(selected);
            loadMembers();
            clearFields();
            showAlert(Alert.AlertType.INFORMATION, "Thành công", "Hội viên đã được cập nhật.");
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Lỗi", e.getMessage());
        }
    }

    public void deleteMember() {
        Members selected = tableView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "Cảnh báo", "Vui lòng chọn một hội viên để xóa.");
            return;
        }
        try {
            memberService.deleteMember(selected.getMemberId());
            loadMembers();
            clearFields();
            showAlert(Alert.AlertType.INFORMATION, "Thành công", "Hội viên đã được xóa.");
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Lỗi", e.getMessage());
        }
    }

    private void loadMembers() {
        try {
            memberData.clear();
            memberData.addAll(memberService.getAllMembers());
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể tải danh sách hội viên: " + e.getMessage());
        }
    }

    private void clearFields() {
        nameField.clear();
        phoneField.clear();
        membershipCombo.setValue("BASIC");
        registrationDatePicker.setValue(null);
        expiryDatePicker.setValue(null);
        tableView.getSelectionModel().clearSelection();
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
