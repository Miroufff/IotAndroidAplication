package fr.imie.sensair.lib;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpUtils {

    public static HttpURLConnection httpConnection(URL url, String method) throws IOException {
        HttpURLConnection connection= (HttpURLConnection) url.openConnection();
        connection.setDoOutput(true);
        connection.setRequestMethod(method);
        connection.setRequestProperty("Content-Type", "application/json");
        connection.connect();

        return connection;
    }

    public static String serializeHttpResponse(HttpURLConnection connection) throws IOException {
        BufferedReader bufferedReader;
        if (200 <= connection.getResponseCode() && connection.getResponseCode() <= 299) {
            bufferedReader = new BufferedReader(new InputStreamReader((connection.getInputStream())));
        } else {
            bufferedReader  = new BufferedReader(new InputStreamReader((connection.getErrorStream())));
        }
        StringBuilder stringBuilder = new StringBuilder();
        String output;
        while ((output = bufferedReader.readLine()) != null) {
            stringBuilder.append(output);
        }
        return stringBuilder.toString();
    }
}
