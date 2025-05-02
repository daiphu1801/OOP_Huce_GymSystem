module com.gym.oop_huce_gymsystem {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires java.sql;

    opens com.gym.oop_huce_gymsystem to javafx.fxml;
    exports com.gym.oop_huce_gymsystem;
    exports com.gym.oop_huce_gymsystem.model;
    exports com.gym.oop_huce_gymsystem.controller.HoiVienController;
    opens com.gym.oop_huce_gymsystem.controller.HoiVienController to javafx.fxml;
    opens com.gym.oop_huce_gymsystem.model;
    exports com.gym.oop_huce_gymsystem.controller;
    opens com.gym.oop_huce_gymsystem.controller to javafx.fxml;
}