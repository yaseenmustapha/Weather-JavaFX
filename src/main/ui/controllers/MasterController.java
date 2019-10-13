package ui.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ui.WeatherApp;

import java.io.IOException;

public class MasterController {

    @FXML
    public void switchDashboard() throws IOException {
        setStage("../fxml/Dashboard.fxml", "Dashboard");
    }

    @FXML
    public void switchLocationManager() throws IOException {
        setStage("../fxml/LocationManager.fxml", "Location Manager");
    }

    @FXML
    public void switchUserManager() throws IOException {
        setStage("../fxml/UserManager.fxml", "User Manager");
    }

    private void setStage(String fxml, String title) throws IOException {
        Stage stage = WeatherApp.stage;
        Parent root = FXMLLoader.load(getClass().getResource(fxml));
        Scene scene = new Scene(root);
        stage.setTitle(title);
        stage.setScene(scene);
    }
}
