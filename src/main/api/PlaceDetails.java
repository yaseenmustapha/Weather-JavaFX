package api;

import api.exceptions.HttpErrorException;
import api.exceptions.ParseException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;

public class PlaceDetails extends APIcaller {
    private String latitude;
    private String longitude;

    public PlaceDetails(String id) {
        super();
        try {
            setURL(new URL("https://maps.googleapis.com/maps/api/place/details/json?placeid=" + id + "&fields=geometry&key=" + googleApiKey));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        try {
            makeCall();
            System.out.println("API call successful");
        } catch (HttpErrorException e) {
            errorMessage = "HTTP error! Check the Google API status.";
            System.out.println(errorMessage);
        } catch (ParseException e) {
            errorMessage = "JSON parse error! Check API key.";
            System.out.println(errorMessage);
        }
    }

    @Override
    public void parse(String responseBody) throws ParseException {
        if (responseBody.charAt(0) != '{') {
            throw new ParseException();
        } else {
            JSONObject obj = new JSONObject(responseBody);
            JSONObject location = obj.getJSONObject("result").getJSONObject("geometry").getJSONObject("location");
            this.latitude = Float.toString(location.getFloat("lat"));
            this.longitude = Float.toString(location.getFloat("lng"));
        }
    }

    public String getLatitude() {
        return this.latitude;
    }

    public String getLongitude() {
        return this.longitude;
    }
}
