package api;

import api.exceptions.HttpErrorException;
import api.exceptions.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public abstract class APIcaller {

    public static final String googleApiKey = "AIzaSyDQQdJfzFRriSqWDsik4B3gOfC09RUmIEY";

    public static final String darkSkyApiKey = "a44d0775e4a62974499471b90c274895";

    public String errorMessage;

    private URL url;
    private HttpURLConnection connection;
    private int status;
    private BufferedReader reader;
    private String line;
    private StringBuffer responseContent;

    public APIcaller() {
        this.responseContent = new StringBuffer();
    }

    public void setURL(URL url) {
        this.url = url;
    }

    public void makeCall() throws HttpErrorException, ParseException {
        try {
            makeRequest();

            if (status > 299) {
                reader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
                return;
//                System.out.println(status);
//                throw new HttpErrorException();
            } else {
                reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            }

            while ((line = reader.readLine()) != null) {
                responseContent.append(line);
            }
            reader.close();

            parse(responseContent.toString());


        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void makeRequest() throws IOException {
        connection = (HttpURLConnection) url.openConnection();

        // request setup
        connection.setRequestMethod("GET");
        connection.setConnectTimeout(5000);
        connection.setReadTimeout(5000);

        this.status = connection.getResponseCode();
    }

    public abstract void parse(String responseBody) throws ParseException;

}
