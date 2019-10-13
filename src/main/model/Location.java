package model;

import javafx.scene.control.ToggleButton;

import java.util.HashSet;
import java.util.Objects;
import java.util.Observable;
import java.util.Set;

public class Location extends Observable {
    private String name;
    private String latitude;
    private String longitude;
    private Boolean active;
    private ToggleButton activeButton;
    private Alert alert;

    Set<User> users = new HashSet<>();

    public Location(String name, String latitude, String longitude) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.active = true;
        this.activeButton = new ToggleButton("Yes");
        activeButton.setSelected(this.active);
        activeButton.setOnAction(event -> switchActive());
    }

    public void addUser(User u) {
        if (!users.contains(u)) {
            addObserver(u);
            System.out.println("Observer " + u.getName() + " added");
            users.add(u);
            u.addLocation(this);
        }
    }

    public void removeUser(User u) {
        if (users.contains(u)) {
            users.remove(u);
            u.removeLocation(this);
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public Alert getAlert() {
        return alert;
    }

    public void setAlert(Alert alert) {
        this.alert = alert;
        setChanged();
        notifyObservers();
        System.out.println("observers notified");
    }

    public Boolean getActive() {
        return active;
    }

    public void switchActive() {
        if (active) {
            activeButton.setText("No");
        } else {
            activeButton.setText("Yes");
        }
        this.active = !active;

    }

    public ToggleButton getActiveButton() {
        return activeButton;
    }

    public void setActiveButton(ToggleButton activeButton) {
        this.activeButton = activeButton;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Location location = (Location) o;
        return name.equals(location.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }


}
