module com.gym.oop_huce_gymsystem {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;

    opens com.gym.oop_huce_gymsystem to javafx.fxml;
    exports com.gym.oop_huce_gymsystem;
}