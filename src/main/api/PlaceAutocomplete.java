package api;

import api.exceptions.HttpErrorException;
import api.exceptions.ParseException;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

public class PlaceAutocomplete extends APIcaller {
    private HashMap<String, String> autocompleteResults = new HashMap<>();

    public PlaceAutocomplete(String input) {
        super();
        try {
            setURL(new URL("https://maps.googleapis.com/maps/api/place/autocomplete/json?input=" + input + "&key=" + googleApiKey));
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
            JSONArray predictions = obj.getJSONArray("predictions");

            for (int i = 0; i < predictions.length(); i++) {
                JSONObject prediction = predictions.getJSONObject(i);
                autocompleteResults.put(prediction.getString("description"), prediction.getString("place_id"));
            }
        }
    }

    public HashMap<String, String> getAutocompleteResults() {
        return this.autocompleteResults;
    }
}
