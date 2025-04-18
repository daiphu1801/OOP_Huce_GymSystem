package com.gym.oop_huce_gymsystem;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ScenceController {
        private Stage st;
        private Scene sc;
        private Parent root;

        public void SwitchTohelloview(ActionEvent event) throws IOException {
            root = FXMLLoader.load(getClass().getResource("hello-view.fxml"));
            st = (Stage) ((Node) event.getSource()).getScene().getWindow();
            sc = new Scene(root);
            st.setScene(sc);
            st.show();
        }
        public void SwitchTohoivienfullinfo(ActionEvent event) throws IOException {
            root = FXMLLoader.load(getClass().getResource("hoivien_full_info.fxml"));
            st = (Stage) ((Node) event.getSource()).getScene().getWindow();
            sc = new Scene(root);
            st.setScene(sc);
            st.show();
        }
        public void SwitchTolisthoivien(ActionEvent event) throws IOException {
            root = FXMLLoader.load(getClass().getResource("list_hoivien.fxml"));
            st = (Stage) ((Node) event.getSource()).getScene().getWindow();
            sc = new Scene(root);
            st.setScene(sc);
            st.show();
        }
        public void SwitchToregister(ActionEvent event) throws IOException {
            root = FXMLLoader.load(getClass().getResource("register.fxml"));
            st = (Stage) ((Node) event.getSource()).getScene().getWindow();
            sc = new Scene(root);
            st.setScene(sc);
            st.show();
        }
        public void SwitchTothiet_bi(ActionEvent event) throws IOException {
            root = FXMLLoader.load(getClass().getResource("thiet_bi.fxml"));
            st = (Stage) ((Node) event.getSource()).getScene().getWindow();
            sc = new Scene(root);
            st.setScene(sc);
            st.show();
        }
        public void SwitchTothietbi_info_full(ActionEvent event) throws IOException {
            root = FXMLLoader.load(getClass().getResource("thietbi_info_full.fxml"));
            st = (Stage) ((Node) event.getSource()).getScene().getWindow();
            sc = new Scene(root);
            st.setScene(sc);
            st.show();
        }
        public void SwitchTothietbi_regis(ActionEvent event) throws IOException {
            root = FXMLLoader.load(getClass().getResource("thietbi_regis.fxml"));
            st = (Stage) ((Node) event.getSource()).getScene().getWindow();
            sc = new Scene(root);
            st.setScene(sc);
            st.show();
        }
        public void SwitchTothongke(ActionEvent event) throws IOException {
            root = FXMLLoader.load(getClass().getResource("thongke.fxml"));
            st = (Stage) ((Node) event.getSource()).getScene().getWindow();
            sc = new Scene(root);
            st.setScene(sc);
            st.show();
        }

}

