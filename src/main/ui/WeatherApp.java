package ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.Location;
import model.User;

import java.util.HashSet;
import java.util.Set;

public class WeatherApp extends Application {
    public static Stage stage;

    public static Set<User> users = new HashSet<>();

    private User defaultUser;

    public static HashSet<Location> globalLocations = new HashSet<>();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        defaultUser = new User("Default", "ubcweatherapp@gmail.com");
        defaultUser.setCurrentUser();
        users.add(defaultUser);
        Parent root = FXMLLoader.load(getClass().getResource("fxml/Dashboard.fxml"));

        stage = primaryStage;



        System.out.println("Initializing weather app...");
        primaryStage.setTitle("Dashboard");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }
}
