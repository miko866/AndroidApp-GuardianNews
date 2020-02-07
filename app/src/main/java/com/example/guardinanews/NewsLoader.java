package com.example.guardinanews;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import java.util.List;


/**
 * Loads a list of news posts by using an AsyncTask to perform the
 * network request to the given URL.
 */
public class NewsLoader extends AsyncTaskLoader<List<News>> {

	/** Query URL */
	private String mUrl;


	/**
	 * Constructs a new {@link NewsLoader}.
	 *
	 * @param context of the activity
	 * @param url to load data from
	 */
	public NewsLoader(Context context, String url) {
		super(context);

		Log.i("InfoContext", String.valueOf(context));

		Log.i("Info", url);
		mUrl = url;
	}

	@Override
	protected void onStartLoading() {
		forceLoad();
	}

	/**
	 * This is on a background thread.
	 */
	@Override
	public List<News> loadInBackground() {
		if(mUrl == null) {
			return null;
		}

		// Perform the network request, parse the response, and extract a list of news.
		List<News> news = QueryUtils.fetchNewsData(mUrl);
		return news;
	}
}