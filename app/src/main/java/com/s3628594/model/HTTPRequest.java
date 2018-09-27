package com.s3628594.model;

import android.content.Context;
import android.util.Log;

import com.s3628594.geotracking.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.concurrent.CountDownLatch;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

// This class makes HTTP requests to retrieve JSON formatted data using the Google Distance Matrix API
public class HTTPRequest extends Thread {

    private final String LOG_TAG = HTTPRequest.class.getName();
    private String url;
    private int statusCode;
    private int walkingTime;
    private final CountDownLatch latch = new CountDownLatch(1);

    public HTTPRequest(Context context, String startLat, String startLng, String endLat, String endLng) {
        String googleUrl = "https://maps.googleapis.com/maps/api/distancematrix/";
        String outputFormat = "json?";
        String origin = "origins=";
        String destination = "destinations=";
        String mode = "mode=walking";
        String key = "key=" + context.getString(R.string.google_maps_key);

        // Generate our google distance matrix URL
        this.url = String.format("%s%s%s%s,%s&%s%s,%s&%s&%s",
                googleUrl, outputFormat, origin, startLat, startLng,
                destination, endLat, endLng, mode, key);
    }

    public void run() {
        disableSSLCertificateChecking();
        HttpURLConnection connection = null;
        try {
            URL urlRequest = new URL(url);
            connection = (HttpURLConnection) urlRequest.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");
            connection.setReadTimeout(5000);
            connection.setConnectTimeout(5000);

            Log.i(LOG_TAG, "Request URL: " + url);
            statusCode = connection.getResponseCode();

            if (statusCode != HttpURLConnection.HTTP_OK)
            {
                Log.e(LOG_TAG, "Invalid Response Code: " + statusCode);
            }

            // Read and build our JSON formatted response
            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()));

            String line;
            StringBuffer reponse = new StringBuffer();
            while ((line = bufferedReader.readLine()) != null) {
                reponse.append(line);
            }
            bufferedReader.close();

            // Get walking duration from JSON
            JSONObject myJson = new JSONObject(reponse.toString());
            JSONArray rows = myJson.getJSONArray("rows");
            JSONObject rowObj = rows.getJSONObject(0);
            JSONArray elements = (JSONArray) rowObj.get("elements");
            JSONObject elementObj = elements.getJSONObject(0);
            JSONObject duration = elementObj.getJSONObject("duration");
            walkingTime = Integer.parseInt(duration.getString("value"));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
        latch.countDown();
    }

    public int getWalkingTime() {
        return walkingTime;
    }

    public CountDownLatch getLatch(){
        return latch;
    }

    /**
     * Disables the SSL certificate checking for new instances of {@link HttpsURLConnection} This has been created to
     * aid testing on a local box, not for use on production.
     * Source: https://gist.github.com/tobiasrohloff/72e32bc4e215522c4bcc
     *
     * This code was used due to java.security.cert.CertificateException: Chain validation failed,
     * requiring the device date to be set beyond July 5th 2018 making it not possible for testing
     * the requirements of this assignment
     */
    private static void disableSSLCertificateChecking() {
        TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }

            @Override
            public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
                // Not implemented
            }

            @Override
            public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
                // Not implemented
            }
        } };

        try {
            SSLContext sc = SSLContext.getInstance("TLS");

            sc.init(null, trustAllCerts, new java.security.SecureRandom());

            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (KeyManagementException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }
}
