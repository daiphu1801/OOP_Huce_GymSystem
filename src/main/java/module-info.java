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
    requires java.desktop;

    opens com.gym.oop_huce_gymsystem to javafx.fxml;
    exports com.gym.oop_huce_gymsystem;
    exports com.gym.oop_huce_gymsystem.model;

    opens com.gym.oop_huce_gymsystem.model;

    exports com.gym.oop_huce_gymsystem.controller.ProductsController;
    opens com.gym.oop_huce_gymsystem.controller.ProductsController to javafx.fxml;

    exports com.gym.oop_huce_gymsystem.controller.MemberShipCardsController;
    opens com.gym.oop_huce_gymsystem.controller.MemberShipCardsController to javafx.fxml;

    exports com.gym.oop_huce_gymsystem.controller.TrainersController;
    opens com.gym.oop_huce_gymsystem.controller.TrainersController to javafx.fxml;

    exports com.gym.oop_huce_gymsystem.controller.EquipmentsController;
    opens com.gym.oop_huce_gymsystem.controller.EquipmentsController to javafx.fxml;

    exports com.gym.oop_huce_gymsystem.view;
    opens com.gym.oop_huce_gymsystem.view to javafx.fxml;
}