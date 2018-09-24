package com.s3628594.model;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.s3628594.geotracking.R;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

//This class makes HTTP requests to retrieve json formatted data using the Google Distance Matrix API
public class HTTPRequest extends AsyncTask<Void, Void, Void> {

    private String url;
    private JSONObject myJson;

    public HTTPRequest(Context context, String startLat, String startLng, String endLat, String endLng) {
        String googleUrl = "https://maps.googleapis.com/maps/api/distancematrix/";
        String outputFormat = "json?";
        String origin = "origins=";
        String destination = "destinations=";
        String mode = "mode=walking";
        String key = "key=" + context.getString(R.string.google_maps_key);

        this.url = String.format("%s%s%s%s,%s&%s%s,%s&%s&%s",
                googleUrl, outputFormat, origin, startLat, startLng,
                destination, endLat, endLng, mode, key);
    }

    @Override
    protected Void doInBackground(Void... voids) {
        HttpURLConnection connection = null;
        try {
            URL urlRequest = new URL(url);
            connection = (HttpURLConnection) urlRequest.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-Type", "application/json;charset=utf-8");

            int statusCode = connection.getResponseCode();
            System.out.println("\nSend GET request to " + url);
            System.out.println("Response Code: " + statusCode);

            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()));

            String line;
            StringBuffer json = new StringBuffer();
            while ((line = bufferedReader.readLine()) != null) {
                json.append(line);
            }
            bufferedReader.close();

            System.out.println(json.toString());

            myJson = new JSONObject(json.toString());
            System.out.println("MY JSON FILE");
//            System.out.println("origin: " + myJson.getString("origin"));

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
        return null;
    }

    private void setWalkingTime() {

    }

    public void getWalkingTime() {

    }

    public void printStuff() {
        try {
            System.out.println("MY JSON FILE");
            System.out.println("origin: " + myJson.getString("origin"));
        } catch (Exception e) {
            Log.e("ERROR:", e.getMessage());
        }

        System.out.println(url);
    }
}
