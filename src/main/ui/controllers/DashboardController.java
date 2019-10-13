package ui.controllers;

import api.DarkSky;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import model.Conditions;
import model.Location;
import model.User;

import java.awt.*;
import java.net.URI;
import java.util.ArrayList;
import java.util.Set;

public class DashboardController extends MasterController {

    @FXML private TabPane tabPane;
    @FXML private Tab homeTab;

    private Set<Location> locationSet;

    private VBox detailsLeft;
    private VBox detailsRight;
    private HBox details;
    private VBox temps;
    private HBox currentConditions;
    private VBox hourlyGraph;
    private HBox dailyConditions;

    private String tempUnits;
    private String precipUnits;
    private String windUnits;

    private LineChart<Number, Number> lineChart;
    private XYChart.Series seriesApparentTemp;
    private XYChart.Series seriesTemp;

    private Conditions current;
    private DarkSky darkSky;

    ArrayList<Double> dailyHigh;
    ArrayList<Double> dailyLow;
    ArrayList<String> dailyTimes;
    ArrayList<String> dailyIcons;

    @FXML
    public void initialize() {
        locationSet = User.currentUser.getLocations();
        for (Location l : locationSet) {
            if (l.getActive()) {
                Tab tab = new Tab(l.getName());
                tabPane.getTabs().add(tab);
                tab.setOnSelectionChanged(event -> fillTab(l.getName()));
            }
        }

        if (User.getCurrentUser().isMetric()) {
            tempUnits = "°C";
            precipUnits = "mm/hr";
            windUnits = "km/hr";
        } else {
            tempUnits = "°F";
            precipUnits = "in/hr";
            windUnits = "mph";
        }
    }

    @FXML
    private void fillTab(String locationName) {
        System.out.println("fill tab");
        Location selectedLocation = getSelectedLocation(locationName);
        darkSky = new DarkSky(selectedLocation);
        current = darkSky.getCurrently();

        System.out.println("Selected tab: " + selectedLocation.getName());

        instantiateLayouts();
        setCurrentConditions();
        setDetailsLeft();
        setDetailsRight();
        setDetails();
        setGraph();
        setDailyConditions();

        SingleSelectionModel<Tab> selectionModel = tabPane.getSelectionModel();
        Tab selectedTab = selectionModel.getSelectedItem();

        BorderPane borderPane = new BorderPane(hourlyGraph, currentConditions, null, dailyConditions, null);
        selectedTab.setContent(borderPane);
    }

    @FXML
    private void openDarkSky() {
        try {
            Desktop desktop = java.awt.Desktop.getDesktop();
            URI url = new URI("https://darksky.net/poweredby/");
            desktop.browse(url);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void instantiateLayouts() {
        detailsLeft = new VBox(10);
        detailsRight = new VBox(10);
        details = new HBox();
        temps = new VBox(1);
        currentConditions = new HBox(30);
        hourlyGraph = new VBox();
        dailyConditions = new HBox(50);
    }

    private void setDailyConditions() {
        setupDay();

        for (int i = 0; i < dailyTimes.size(); i++) {
            VBox day = new VBox(10);
            day.setStyle("-fx-padding: 10;" + "-fx-border-style: solid inside;" + "-fx-border-width: 0.5;"
                    + "-fx-border-insets: 5;" + "-fx-border-radius: 2;" + "-fx-border-color: grey;");
            ImageView dailyImage = new ImageView(new Image("ui/images/" + dailyIcons.get(i) + ".png",
                    40, 40, false, false));
            Label timeLabel = new Label(dailyTimes.get(i));
            timeLabel.setFont(Font.font("Segoe UI", 20));
            Label highLabel = new Label(Math.round(dailyHigh.get(i)) + tempUnits);
            highLabel.setFont(Font.font("Segoe UI", 16));
            Label lowLabel = new Label(Math.round(dailyLow.get(i)) + tempUnits);
            lowLabel.setFont(Font.font("Segoe UI", 16));
            day.getChildren().addAll(timeLabel, highLabel, lowLabel, dailyImage);
            dailyConditions.getChildren().add(day);
        }

        dailyConditions.setAlignment(Pos.CENTER);
    }

    private void setupDay() {
        dailyHigh = darkSky.getDailyHigh();
        dailyLow = darkSky.getDailyLow();
        dailyTimes = darkSky.getDailyTimes();
        dailyIcons = darkSky.getDailyIcons();
    }

    private void setCurrentConditions() {
        Label tempLabel = new Label(Math.round(current.getTemperature()) + tempUnits);
        tempLabel.setFont(Font.font("Segoe UI", 40));

        Label apparentTempLabel = new Label("Feels like " + Math.round(current.getApparentTemperature()) + tempUnits);
        apparentTempLabel.setFont(Font.font("Segoe UI", 20));

        temps.getChildren().addAll(tempLabel, apparentTempLabel);
        temps.setMaxHeight(10);

        Label summaryLabel = new Label(current.getSummary());
        summaryLabel.setFont(Font.font("Segoe UI", 26));

        ImageView currentImage = new ImageView(new Image("ui/images/" + current.getIcon() + ".png"));

        currentConditions.getChildren().addAll(currentImage, temps, summaryLabel, details);
        currentConditions.setPadding(new Insets(10, 10, 0, 120));
        currentConditions.setAlignment(Pos.CENTER_LEFT);
    }

    private void setDetailsLeft() {
        Label humidityLabel = new Label("Humidity: " + current.getHumidity() + "%");
        humidityLabel.setFont(Font.font("Segoe UI", 16));

        Label precipLabel = new Label("Precip. Intensity: " + current.getPrecipIntensity() + " "
                + precipUnits);
        precipLabel.setFont(Font.font("Segoe UI", 16));

        Label precipProbabilityLabel = new Label("Precip. Probability: " + current.getPrecipProbability());
        precipProbabilityLabel.setFont(Font.font("Segoe UI", 16));

        detailsLeft.getChildren().addAll(humidityLabel, precipLabel, precipProbabilityLabel);
        detailsLeft.setPadding(new Insets(0, 0, 0, 50));
        detailsLeft.setMaxHeight(10);
    }

    private void setDetailsRight() {
        Label dewPointLabel = new Label("Dew Point: " + current.getDewPoint() + tempUnits);
        dewPointLabel.setFont(Font.font("Segoe UI", 16));

        Label windLabel = new Label("Wind Speed: " + current.getWindSpeed() + " " + windUnits
                + " at " + current.getWindBearing() + "°");
        windLabel.setFont(Font.font("Segoe UI", 16));

        Label pressureLabel = new Label("Pressure: " + current.getPressure() + " Pa");
        pressureLabel.setFont(Font.font("Segoe UI", 16));



        detailsRight.getChildren().addAll(dewPointLabel, windLabel, pressureLabel);
        detailsRight.setPadding(new Insets(0, 0, 0, 40));
        detailsRight.setMaxHeight(10);
    }

    public void setDetails() {
        details.setMaxHeight(10);
        details.getChildren().addAll(detailsLeft, detailsRight);
    }

    private void setGraph() {
        NumberAxis axisX = new NumberAxis();
        NumberAxis axisY = new NumberAxis();
        axisX.setLabel("Hour");
        axisY.setLabel("Temperature (" + tempUnits + ")");
        lineChart = new LineChart<>(axisX, axisY);
        lineChart.setTitle("Next 24 Hours");
        seriesTemp = new XYChart.Series();
        seriesTemp.setName("Temperature");
        seriesApparentTemp = new XYChart.Series();
        seriesApparentTemp.setName("Feels Like (if available)");

        ArrayList<Double> hourlyTemps = darkSky.getHourlyTemps();
        ArrayList<Double> hourlyApparentTemps = darkSky.getHourlyApparentTemps();
        ArrayList<Integer> hourlyTimes = darkSky.getHourlyTimes();

        for (int i = 0; i < hourlyTemps.size(); i++) {
            seriesTemp.getData().add(new XYChart.Data(hourlyTimes.get(i), hourlyTemps.get(i)));
            seriesApparentTemp.getData().add(new XYChart.Data(hourlyTimes.get(i), hourlyApparentTemps.get(i)));
        }

        populateGraph();

    }

    private void populateGraph() {
        lineChart.getData().addAll(seriesApparentTemp, seriesTemp);
        hourlyGraph.getChildren().add(lineChart);
    }

    public Location getSelectedLocation(String locationName) {
        for (Location l : locationSet) {
            if (l.equals(new Location(locationName, null, null))) {
                System.out.println(l);
                return l;
            }
        }
        return null;
    }
}
