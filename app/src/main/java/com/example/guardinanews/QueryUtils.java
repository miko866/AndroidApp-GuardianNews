package com.example.guardinanews;

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


/**
 * Helper methods related to requesting and receiving post data from Guardian.
 */
public class QueryUtils {

	/** Tag for the log messages */
	private static final String LOG_TAG = QueryUtils.class.getSimpleName();

	/* Only empty Constructor */
	public QueryUtils() {
	}

	/**
	 * Query the Guardian dataset and return a list of {@link News} objects.
	 */
	public static List<News> fetchNewsData(String stringUrl) {
		// Check if have something
		if(stringUrl == null) {
			Log.e(LOG_TAG, "Problem making the HTTP request.");
			return null;
		}

		// Create URL object
		URL url = createUrl(stringUrl);

		// Perform HTTP request to the URL and receive a JSON response back
		String jsonResponse = null;
		try {
			jsonResponse = makeHttpRequest(url);
		} catch (IOException e) {
			Log.e(LOG_TAG, "Problem making the HTTP request.", e);
		}

		// Extract relevant fields from the JSON response and create a list of {@link News}
		List<News> newsList = extractDataFromJson(jsonResponse);

		// Return the list of {@link News}
		return newsList;
	}

	/**
	 * Returns new URL object from the given string URL.
	 */
	public static URL createUrl(String stringUrl) {
		URL url = null;
		if (stringUrl == null) {
			return null;
		}

		try {
			url = new URL(stringUrl);
		} catch (MalformedURLException e) {
			Log.e(LOG_TAG, "Problem building the URL ", e);
		}
		return url;
	}


	/**
	 * Make an HTTP request to the given URL and return a String as the response.
	 */
	private static String makeHttpRequest(URL url) throws IOException {
		HttpURLConnection urlConnection = null;
		String jsonResponse = "";
		InputStream inputStream = null;

		// If the URL is null, then return early.
		if(url == null) {
			return null;
		}

		try {
			urlConnection = (HttpURLConnection) url.openConnection();
			urlConnection.setRequestMethod("GET");
			urlConnection.connect();

			if(urlConnection.getResponseCode() == 200) {
				inputStream = urlConnection.getInputStream();
				jsonResponse = readFromStream(inputStream);
			} else {
				Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
			}
		} catch (IOException e) {
			Log.e(LOG_TAG, "Problem retrieving the earthquake JSON results.", e);
		} finally {
			if (urlConnection != null) {
				urlConnection.disconnect();
			}
			if (inputStream != null) {
				// Closing the input stream could throw an IOException, which is why
				// the makeHttpRequest(URL url) method signature specifies than an IOException
				// could be thrown.
				inputStream.close();
			}
		}

		Log.d(LOG_TAG, "makeHttpRequest: "+jsonResponse);
		return jsonResponse;
	}

	/**
	 * Convert the {@link InputStream} into a String which contains the
	 * whole JSON response from the server.
	 */
	private static String readFromStream (InputStream inputStream) {
		InputStreamReader streamReader = null;
		StringBuilder result = new StringBuilder() ;
		BufferedReader bufferedReader = null;

		streamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
		bufferedReader = new BufferedReader(streamReader);

		try {
			String line = bufferedReader.readLine();
			while (line != null) {
				result.append(line);
				line = bufferedReader.readLine();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return result.toString();
	}


	/**
	 * Return a list of {@link News} objects that has been built up from
	 * parsing the given JSON response.
	 */
	private static List<News> extractDataFromJson (String jsonResponse) {
		// If the JSON string is empty or null, then return early.
		if (TextUtils.isEmpty(jsonResponse)) {
			return null;
		}

		// Create an empty ArrayList that we can start adding news to
		List<News> newsList = new ArrayList<>();
		// Try to parse the JSON response string. If there's a problem with the way the JSON
		// is formatted, a JSONException exception object will be thrown.
		// Catch the exception so the app doesn't crash, and print the error message to the logs.
		try {

			// Create a JSONObject from the JSON response string
			JSONObject json = new JSONObject(jsonResponse);
			JSONObject response = json.getJSONObject("response");
			// Extract the JSONArray associated with the key called "results",
			// which represents a list of results (or news).
			JSONArray results = response.getJSONArray("results");

			// For each new post in the JSONArray, create an {@link News} object
			for (int i = 0; i < results.length(); i++) {

				// Get a single new at position i within the list of news
				JSONObject currentNews = results.getJSONObject(i);
				String title = currentNews.getString("webTitle");
				String section = currentNews.getString("sectionName");
				String date = currentNews.getString("webPublicationDate");
				String url = currentNews.getString("webUrl");
				String type = currentNews.getString("type");

				// Add the new {@link News} to the list of newsObject.
				News newsObject = new News(title, date, url, title, section);
				newsList.add(newsObject);
			}


		} catch (JSONException e) {
			// If an error is thrown when executing any of the above statements in the "try" block,
			// catch the exception here, so the app doesn't crash. Print a log message
			// with the message from the exception.
			Log.e("QueryUtils", "Problem parsing the earthquake JSON results", e);
		}

		// Return the list of earthquakes
		return newsList;
	}
}
