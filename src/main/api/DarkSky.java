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
            units = "metric";
        } else {
            units = "imperial";
        }
        try {
            setURL(new URL("https://api.openweathermap.org/data/3.0/onecall?lat="
                    + this.location.getLatitude() + "&lon=" + this.location.getLongitude()
                    + "&exclude=minutely&units=" + units + "&appid=" + darkSkyApiKey));
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
            currently = getConditions(obj.getJSONObject("current"));

            setHourly(obj.getJSONArray("hourly"));
            setDaily(obj.getJSONArray("daily"));

            if (obj.has("alerts")) {
                System.out.println("ALERT DETECTED");
                JSONArray alerts = obj.getJSONArray("alerts");
                JSONObject alert = alerts.getJSONObject(0);
                Alert warning = new Alert(alert.getString("event"), alert.getInt("start"),
                        alert.getInt("end"), alert.getString("description"));
                this.location.setAlert(warning);
            } else {
                System.out.println("NO ALERTS");
            }

        }
    }

    private void setDaily(JSONArray daily) {
        DateFormat dateFormat = new SimpleDateFormat("E dd");

        for (int i = 1; i < daily.length(); i++) {
            JSONObject day = daily.getJSONObject(i);
            JSONObject temp = day.getJSONObject("temp");
            dailyHigh.add(temp.getDouble("max"));
            dailyLow.add(temp.getDouble("min"));
            dailyTimes.add(dateFormat.format(new Date((long)day.getInt("dt") * 1000)));
            JSONObject weatherObj = day.getJSONArray("weather").getJSONObject(0);
            dailyIcons.add(weatherObj.getString("icon"));
            System.out.println(dateFormat.format(new Date((long)day.getInt("dt") * 1000)));
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

    private void setHourly(JSONArray hourly) {
        DateFormat dateFormat = new SimpleDateFormat("HH");

        for (int i = 0; i < hourly.length(); i++) {
            JSONObject hour = hourly.getJSONObject(i);
            int hourInt = Integer.parseInt(dateFormat.format(new Date((long)hour.getInt("dt") * 1000)));
            if (!hourlyTimes.contains(hourInt)) {
                hourlyTemps.add(hour.getDouble("temp"));
                hourlyApparentTemps.add(hour.getDouble("feels_like"));
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
        JSONObject weatherObj = obj.getJSONArray("weather").getJSONObject(0);
        JSONObject rainObj = obj.optJSONObject("rain");
        double rain1h = 0.0;

        if (rainObj != null) {
            rain1h = rainObj.optDouble("1h", 0.0);
        }

        return new Conditions(
                obj.getInt("dt"),
                weatherObj.getString("description"),
                weatherObj.getString("icon"),
                rain1h,
                obj.getDouble("uvi"),
                obj.getDouble("temp"),
                obj.getDouble("feels_like"),
                obj.getDouble("dew_point"),
                obj.getInt("humidity"),
                obj.getInt("pressure"),
                obj.getDouble("wind_speed"),
                obj.getInt("wind_deg"));
    }

    public Conditions getCurrently() {
        return currently;
    }
}
