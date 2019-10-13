package ui.controllers;

import api.PlaceAutocomplete;
import api.PlaceDetails;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import model.Location;
import model.User;
import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.TextFields;
import ui.AlertBox;
import ui.WeatherApp;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class LocationManagerController extends MasterController {

    @FXML private TableView<Location> locationTable;
    @FXML private TableColumn<Location, ToggleButton> activeColumn;
    @FXML private TableColumn<Location, String> nameColumn;
    @FXML private TableColumn<Location, String> latColumn;
    @FXML private TableColumn<Location, String> longColumn;
    @FXML private TextField locationName;
    @FXML private TextField locationLat;
    @FXML private TextField locationLong;

    private ObservableList<Location> items = FXCollections.observableArrayList();
    private AutoCompletionBinding auto;

    @FXML
    public void initialize() {
        System.out.println("Opening location manager...");
//        locationList.setItems(items);
        locationTable.setPlaceholder(new Label("No locations in collection"));
        locationTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        for (Location l : WeatherApp.globalLocations) {
            addToList(l);
        }

        activeColumn.setCellValueFactory(new PropertyValueFactory<>("activeButton"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        latColumn.setCellValueFactory(new PropertyValueFactory<>("latitude"));
        longColumn.setCellValueFactory(new PropertyValueFactory<>("longitude"));

        locationTable.setItems(items);

        locationTable.setEditable(true);
        nameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        latColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        longColumn.setCellFactory(TextFieldTableCell.forTableColumn());
    }

    public void addToList(Location locationListItem) {
        items.add(locationListItem);
        //locationTable.getItems().add(locationListItem);
        locationName.clear();
        locationLat.clear();
        locationLong.clear();
    }

    @FXML
    public void addLocation() {
        String name = locationName.getText();
        String latitude = locationLat.getText();
        String longitude = locationLong.getText();
        Location l = new Location(name, latitude, longitude);
        if (WeatherApp.globalLocations.contains(l)) {
            AlertBox.display("Add Location Error", "Location has already been added");
        } else if (name.isEmpty()) {
            AlertBox.display("Location Name Error", "Location name field cannot be empty");
        } else if (latitude.isEmpty() || longitude.isEmpty()) {
            AlertBox.display("Coordinates Error", "Coordinates fields cannot be empty");
        } else if (!isLatitude(latitude) || !isLongitude(longitude)) {
            AlertBox.display("Coordinates Error", "Please enter valid coordinates");
        } else {
            l.addUser(User.currentUser);
            WeatherApp.globalLocations.add(l);
            addToList(l);
        }
    }

    // Reference: Modified numeric checker from CraigTP on Stack Overflow (https://stackoverflow.com/a/1102916)
    private boolean isLatitude(String latStr) {
        try {
            Double latNum = Double.parseDouble(latStr);
            return latNum >= -90.0 && latNum <= 90.0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    // Reference: Modified numeric checker from CraigTP on Stack Overflow (https://stackoverflow.com/a/1102916)
    private boolean isLongitude(String longStr) {
        try {
            Double longNum = Double.parseDouble(longStr);
            return longNum >= -180.0 && longNum <= 180.0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    @FXML void deleteLocation() {
        //ObservableList<Location> selectedLocations;
        List selectedLocations = locationTable.getSelectionModel().getSelectedItems();
        selectedLocations.forEach(WeatherApp.globalLocations::remove);
        for (User u : WeatherApp.users) {
            selectedLocations.forEach(u.getLocations()::remove);
        }
        //selectedLocations.forEach(items::remove);
        items.removeAll(selectedLocations);
        locationTable.getSelectionModel().clearSelection();
    }

    @FXML
    public void addLocationCollectionOnly() {
        Location l = new Location(locationName.getText(), locationLat.getText(), locationLong.getText());
        if (WeatherApp.globalLocations.contains(l)) {
            System.out.println("Error: location has already been added!");
        } else {
            WeatherApp.globalLocations.add(l);
            addToList(l);
        }
    }

    @FXML
    public void populateAutocompleteResults() {
        String input = locationName.getText();

        if (input.length() > 4) {
            PlaceAutocomplete pa = new PlaceAutocomplete(input);
            HashMap<String, String> results = pa.getAutocompleteResults();
            Set<String> resultNames = results.keySet();
            auto = TextFields.bindAutoCompletion(locationName, resultNames);

            auto.setOnAutoCompleted(new EventHandler<AutoCompletionBinding.AutoCompletionEvent>() {
                @Override
                public void handle(AutoCompletionBinding.AutoCompletionEvent event) {
                    fillCoordinates(results.get(locationName.getText()));
                    auto.dispose();
                }
            });
        }
    }

    private void fillCoordinates(String id) {
        PlaceDetails pd = new PlaceDetails(id);
        locationLat.setText(pd.getLatitude());
        locationLong.setText(pd.getLongitude());
    }

    @FXML
    public void editLocationName(TableColumn.CellEditEvent editedCell) {
        Location selectedLocation = locationTable.getSelectionModel().getSelectedItem();
        selectedLocation.setName(editedCell.getNewValue().toString());
    }

    @FXML
    public void editLocationLat(TableColumn.CellEditEvent editedCell) {
        Location selectedLocation = locationTable.getSelectionModel().getSelectedItem();
        selectedLocation.setLatitude(editedCell.getNewValue().toString());
    }

    @FXML
    public void editLocationLong(TableColumn.CellEditEvent editedCell) {
        Location selectedLocation = locationTable.getSelectionModel().getSelectedItem();
        selectedLocation.setLongitude(editedCell.getNewValue().toString());
    }
}
