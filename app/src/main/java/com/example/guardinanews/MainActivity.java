package com.example.guardinanews;

import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<News>>{

	/** URL for news posts data from the Guardian dataset */
	public static final String GUARDIAN_REQUEST_URL =
		"https://content.guardianapis.com/search?api-key=107c38dd-b2fd-4473-9675-c5516e14fe9e";

	/**
	 * Constant value for the guardian loader ID. We can choose any integer.
	 * This really only comes into play if you're using multiple loaders.
	 */
	public static final int NEWS_LOADER_ID = 1;

	ListView mListView;
	TextView mTextView;
	NewsAdapter mAdapter;
	ProgressBar mProgressBar;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// Get a reference to the ConnectivityManager to check state of network connectivity
		ConnectivityManager manager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
		// Get details on the currently active default data network
		NetworkInfo networkInfo = manager.getActiveNetworkInfo();

		mListView = (ListView) findViewById(R.id.list_view);
		mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);
		mTextView = (TextView) findViewById(R.id.empty_view);

		// Create a new adapter that takes an empty list of earthquakes as input
		mAdapter = new NewsAdapter(this, new ArrayList<News>());

		// Set the adapter on the {@link ListView}
		// so the list can be populated in the user interface
		mListView.setAdapter(mAdapter);

		// Set an item click listener on the ListView, which sends an intent to a web browser
		// to open a website with more information about the selected earthquake.
		mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
				// Find the current earthquake that was clicked on
				News currentNew = mAdapter.getItem(position);

				// Convert the String URL into a URI object (to pass into the Intent constructor)
				Uri newUri = Uri.parse(currentNew.getUrl());

				// Create a new intent to view the earthquake URI
				Intent websiteIntent = new Intent(Intent.ACTION_VIEW, newUri);

				// Send the intent to launch a new activity
				startActivity(websiteIntent);
			}
		});


		// If there is a network connection, fetch data
		if (networkInfo != null && networkInfo.isConnected()) {
			// Get a reference to the LoaderManager, in order to interact with loaders.
			LoaderManager loaderManager = getLoaderManager();

			// Initialize the loader. Pass in the int ID constant defined above and pass in null for
			// the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
			// because this activity implements the LoaderCallbacks interface).
			loaderManager.initLoader(NEWS_LOADER_ID, null, this);
		} else {
			// Otherwise, display error
			// First, hide loading indicator so error message will be visible
			mProgressBar.setVisibility(View.GONE);
			mTextView.setVisibility(View.VISIBLE);

			// Update empty state with no connection error message
			mTextView.setText(R.string.no_internet_text);
		}
	}


	@Override
	public Loader<List<News>> onCreateLoader(int id, Bundle args) {
		// Create a new loader for the given URL
		return new NewsLoader(this, GUARDIAN_REQUEST_URL);
	}

	@Override
	public void onLoadFinished(Loader<List<News>> loader, List<News> data) {
		// Hide loading indicator because the data has been loaded
		mProgressBar.setVisibility(View.GONE);

		mAdapter = new NewsAdapter(this, data);


		// If there is a valid list of {@link Earthquake}s, then add them to the adapter's
		// data set. This will trigger the ListView to update.
		if (data != null && !data.isEmpty()) {
			mListView.setAdapter(mAdapter);
		}

	}

	@Override
	public void onLoaderReset(Loader<List<News>> loader) {
		// Loader reset, so we can clear out our existing data.
		mAdapter.clear();
	}
}
