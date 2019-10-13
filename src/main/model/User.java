package model;

import api.Email;

import java.util.*;

public class User implements Observer {
    private String name;
    private String email;
    private boolean metric;
    private boolean subscribed;
    public static User currentUser;

    private Set<Location> locations;

    public User(String name, String email) {
        this.name = name;
        this.email = email;
        this.metric = true;
        this.subscribed = false;
        locations = new HashSet<>();
    }

    public void addLocation(Location l) {
        if (!locations.contains(l)) {
            locations.add(l);
            l.addUser(this);
        }
    }

    public void removeLocation(Location l) {
        if (locations.contains(l)) {
            locations.remove(l);
            l.removeUser(this);
        }
    }

    public Set<Location> getLocations() {
        return this.locations;
    }

    public String getName() {
        return this.name;
    }

    public void setMetric(boolean metric) {
        this.metric = metric;
    }

    public boolean isMetric() {
        return metric;
    }

    public void setSubscribed(boolean subscribed) {
        this.subscribed = subscribed;
    }

    public boolean isSubscribed() {
        return subscribed;
    }

    public String getEmail() {
        return email;
    }

    public void setCurrentUser() {
        currentUser = this;
    }

    public static User getCurrentUser() {
        return currentUser;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        User user = (User) o;
        return Objects.equals(name, user.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public void update(Observable o, Object arg) {
        if (isSubscribed()) {
            System.out.println("Subscriber getting email");
            for (Location l : locations) {
                if (!l.getAlert().getNotified()) {
                    try {
                        Email.sendMail(this, l);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    System.out.println("Email sent!");
                    l.getAlert().setNotified(true);
                }
            }
        }
    }
}
