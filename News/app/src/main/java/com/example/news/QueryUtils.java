package com.example.news;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

final class QueryUtils {

    /**
     * Tag for the log messages
     */
    private static final String LOG_TAG = QueryUtils.class.getSimpleName();

    /**
     * Generate the private constructor {@link QueryUtils} object
     * The class is designed to store static variables and methods, that can be accessed
     * directly from the class QueryUtils (the object instance of QueryUtils not required)
     */
    private QueryUtils() {

    }

    /**
     * Query the Guardian data and return the list from {@link News} objects.
     */
    public static List<News> fetchNewsData(String requestUrl) {
        // Generate the URL object
        URL url = createUrl(requestUrl);

        // Execute the HTTp request to the URL and collect the JSON reply black
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Issue with making HTTP request.", e);
        }

        // Remove the significant fields from the JSON response, create the link (@link News}s
        // and return the list of {@link News}s
        return extractFeatureFromJson(jsonResponse);
    }

    /**
     * Return the URL object from the URL
     */
    private static URL createUrl (String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem constructing URL ", e);
        }
        return url;
    }

    /**
     * Make the HTTp request to the given URL and also return the String response
     */
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null return back immediately
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request is successful (response code 200),
            // next read the input stream and parse the response
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem returning the articles and JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // Exiting the input stream could thrown an IOException, that is why
                // the makeHttpRequest(URL url) specifics the IOException could be thrown
                inputStream.close();
            }
        }
        return jsonResponse;

    }
    /** Change the {@link InputStream} into the string that holds the entire JSON response
     * that will be provided from the server
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    /**
     * Return the list of {@link News} objects that have been created by the parsing
     * from the JSON Response
     */
    private static List<News> extractFeatureFromJson(String newsJSON) {
        // If the JSON string blank then return immediately
        if (TextUtils.isEmpty(newsJSON)) {
            return null;
        }

        // Create the blank list that will be used to add recent articles
        ArrayList<News> newsArticles = new ArrayList<>();

        // Attempt to parse the SAMPLE_JSON_RESPONSE. If an issue occurs with the way JSON
        // is formatted, the JSONException exception object will be thrown
        // Catch that exception so the app won't crash, and just print the error to the logs.
        try {

            // Create the JSONObject that came from the JSON response string
            JSONObject jsonResponse = new JSONObject(newsJSON);

            // Extract the JSONObject affiliated with the key known as "response"
            JSONArray results = jsonResponse.getJSONObject("response").getJSONArray("results");

        } catch (JSONException e) {
            // If the error is thrown when performing any of the above statements in the "try block",
            // catch the exception immediately here, so the app won't crash. Then print the log message
            // with the message from the exception
            Log.e(LOG_TAG, "Issue parsing the News article JSON results", e);
        }

        // Return the list of new articles
        return newsArticles;
    }
}





