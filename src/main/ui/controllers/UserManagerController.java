package ui.controllers;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import model.Location;
import model.User;
import ui.AlertBox;

import java.io.IOException;
import java.util.regex.Pattern;

import static ui.WeatherApp.globalLocations;
import static ui.WeatherApp.users;

public class UserManagerController extends MasterController {

    @FXML private Label currentUserLabel;
    @FXML private RadioButton radioMetric;
    @FXML private RadioButton radioImperial;
    @FXML private RadioButton radioYes;
    @FXML private RadioButton radioNo;
    @FXML private ListView<String> globalLocationsList;
    @FXML private ListView<String> userLocationsList;
    @FXML private TextField userName;
    @FXML private TextField userEmail;
    @FXML private ComboBox<String> userComboBox;

    private ToggleGroup unitsToggleGroup;
    private ToggleGroup alertsToggleGroup;

    private User currentUser;

    @FXML
    public void initialize() {
        currentUser = User.getCurrentUser();

        currentUserLabel.setText(currentUser.getName() + " (" + currentUser.getEmail() + ")");

        initializeRadio();

        for (User u : users) {
            if (!User.getCurrentUser().equals(u)) {
                userComboBox.getItems().add(u.getName());
            }
        }

        globalLocationsList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        userLocationsList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        for (Location l : globalLocations) {
            if (!currentUser.getLocations().contains(l)) {
                globalLocationsList.getItems().add(l.getName());
            } else {
                userLocationsList.getItems().add(l.getName());
            }
        }

    }

    private void initializeRadio() {
        unitsToggleGroup = new ToggleGroup();
        radioMetric.setToggleGroup(unitsToggleGroup);
        radioImperial.setToggleGroup(unitsToggleGroup);

        if (currentUser.isMetric()) {
            radioMetric.setSelected(true);
        } else {
            radioImperial.setSelected(true);
        }

        alertsToggleGroup = new ToggleGroup();
        radioYes.setToggleGroup(alertsToggleGroup);
        radioNo.setToggleGroup(alertsToggleGroup);

        if (!currentUser.isSubscribed()) {
            radioNo.setSelected(true);
        } else {
            radioYes.setSelected(true);
        }
    }

    @FXML
    public void metricPressed() {
        currentUser.setMetric(true);
    }

    @FXML
    public void imperialPressed() {
        currentUser.setMetric(false);
    }

    @FXML
    public void subscribePressed() {
        currentUser.setSubscribed(true);
    }

    @FXML
    public void unsubscribePressed() {
        currentUser.setSubscribed(false);
    }

    @FXML
    public void deleteCurrentUser() throws IOException {
        if (users.size() > 1) {
            users.remove(currentUser);
            users.iterator().next().setCurrentUser();
            switchUserManager();
        } else {
            AlertBox.display("Delete User Error", "Must have more than one user to delete current user");
        }
    }

    @FXML
    public void addLocation() throws IOException {
        ObservableList selectedLocations = globalLocationsList.getSelectionModel().getSelectedItems();

        for (Object locationName : selectedLocations) {
            for (Location l : globalLocations) {
                if (l.getName().equals(locationName)) {
                    l.addUser(currentUser);
                }
            }
        }
        switchUserManager();
    }

    @FXML
    public void removeLocation() throws IOException {
        ObservableList selectedLocations = userLocationsList.getSelectionModel().getSelectedItems();

        for (Object locationName : selectedLocations) {
            for (Location l : globalLocations) {
                if (l.getName().equals(locationName)) {
                    l.removeUser(currentUser);
                }
            }
        }
        switchUserManager();
    }

    @FXML
    public void addUser() {
        String name = userName.getText();
        String email = userEmail.getText();
        if (name.isEmpty()) {
            AlertBox.display("Username Error", "Username field cannot be empty");
        } else if (email.isEmpty()) {
            AlertBox.display("Email Error", "Email field cannot be empty");
        } else if (!isValidEmail(email)) {
            AlertBox.display("Email Error", "Please enter a valid email address");
        } else {
            User u = new User(name, email);
            users.add(u);
            userComboBox.getItems().add(u.getName());
            userName.clear();
            userEmail.clear();
        }
    }

    // Reference: GeeksforGeeks email validation (https://www.geeksforgeeks.org/check-email-address-valid-not-java/)
    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."
                + "[a-zA-Z0-9_+&*-]+)*@"
                + "(?:[a-zA-Z0-9-]+\\.)+[a-z"
                + "A-Z]{2,7}$";
        Pattern pat = Pattern.compile(emailRegex);
        if (email == null) {
            return false;
        }
        return pat.matcher(email).matches();
    }

    @FXML
    public void switchUser() throws IOException {
        for (User u : users) {
            if (u.getName().equals(userComboBox.getValue())) {
                u.setCurrentUser();
                switchUserManager();
            }
        }
    }

}
