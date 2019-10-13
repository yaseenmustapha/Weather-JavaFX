package api;

import api.exceptions.HttpErrorException;
import api.exceptions.ParseException;
import model.Alert;
import model.Conditions;
import model.Location;
import model.User;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class DarkSky extends APIcaller {
    private Location location;

    private Conditions currently;
    private ArrayList<Double> hourlyTemps = new ArrayList<>();
    private ArrayList<Double> hourlyApparentTemps = new ArrayList<>();
    private ArrayList<Integer> hourlyTimes = new ArrayList<>();
    private ArrayList<Double> dailyHigh = new ArrayList<>();
    private ArrayList<Double> dailyLow = new ArrayList<>();
    private ArrayList<String> dailyTimes = new ArrayList<>();
    private ArrayList<String> dailyIcons = new ArrayList<>();

    public DarkSky(Location location) {
        this.location = location;
        String units;
        if (User.getCurrentUser().isMetric()) {
            units = "ca";
        } else {
            units = "us";
        }
        try {
            setURL(new URL("https://api.darksky.net/forecast/" + darkSkyApiKey + "/"
                    + this.location.getLatitude() + "," + this.location.getLongitude()
                    + "?exclude=minutely,flags&units=" + units));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        try {
            makeCall();
            System.out.println("API call successful");
        } catch (HttpErrorException e) {
            errorMessage = "HTTP error! Check the Dark Sky API status.";
        } catch (ParseException e) {
            errorMessage = "JSON parse error! Check API key.";
        }
    }

    @Override
    public void parse(String responseBody) throws ParseException {
        if (responseBody.charAt(0) != '{') {
            throw new ParseException();
        } else {
            JSONObject obj = new JSONObject(responseBody);
            currently = getConditions(obj.getJSONObject("currently"));

            setHourly(obj.getJSONObject("hourly"));
            setDaily(obj.getJSONObject("daily"));

            if (obj.has("alerts")) {
                System.out.println("ALERT DETECTED");
                JSONArray alerts = obj.getJSONArray("alerts");
                JSONObject alert = alerts.getJSONObject(0);
                Alert warning = new Alert(alert.getString("title"), alert.getInt("time"),
                        alert.getInt("expires"), alert.getString("description"), alert.getString("uri"));
                this.location.setAlert(warning);
            } else {
                System.out.println("NO ALERTS");
            }

        }
    }

    private void setDaily(JSONObject daily) {
        JSONArray data = daily.getJSONArray("data");
        DateFormat dateFormat = new SimpleDateFormat("E dd");

        for (int i = 1; i < data.length(); i++) {
            JSONObject day = data.getJSONObject(i);
            dailyHigh.add(day.getDouble("temperatureHigh"));
            dailyLow.add(day.getDouble("temperatureLow"));
            dailyTimes.add(dateFormat.format(new Date((long)day.getInt("time") * 1000)));
            dailyIcons.add(day.getString("icon"));
            System.out.println(dateFormat.format(new Date((long)day.getInt("time") * 1000)));
        }
    }

    public ArrayList<Double> getDailyHigh() {
        return dailyHigh;
    }

    public ArrayList<Double> getDailyLow() {
        return dailyLow;
    }

    public ArrayList<String> getDailyTimes() {
        return dailyTimes;
    }

    public ArrayList<String> getDailyIcons() {
        return dailyIcons;
    }

    private void setHourly(JSONObject hourly) {
        JSONArray data = hourly.getJSONArray("data");
        DateFormat dateFormat = new SimpleDateFormat("HH");

        for (int i = 0; i < data.length(); i++) {
            JSONObject hour = data.getJSONObject(i);
            int hourInt = Integer.parseInt(dateFormat.format(new Date((long)hour.getInt("time") * 1000)));
            if (!hourlyTimes.contains(hourInt)) {
                hourlyTemps.add(hour.getDouble("temperature"));
                hourlyApparentTemps.add(hour.getDouble("apparentTemperature"));
                hourlyTimes.add(hourInt);
            }
        }
    }

    public ArrayList<Double> getHourlyTemps() {
        return hourlyTemps;
    }

    public ArrayList<Double> getHourlyApparentTemps() {
        return hourlyApparentTemps;
    }

    public ArrayList<Integer> getHourlyTimes() {
        return hourlyTimes;
    }

    private Conditions getConditions(JSONObject obj) {
        return new Conditions(obj.getInt("time"), obj.getString("summary"),
                obj.getString("icon"), obj.getDouble("precipIntensity"),
                obj.getDouble("precipProbability"), obj.getDouble("temperature"),
                obj.getDouble("apparentTemperature"), obj.getDouble("dewPoint"),
                obj.getDouble("humidity"), obj.getDouble("pressure"),
                obj.getDouble("windSpeed"), obj.getInt("windBearing"));
    }

    public Conditions getCurrently() {
        return currently;
    }
}
