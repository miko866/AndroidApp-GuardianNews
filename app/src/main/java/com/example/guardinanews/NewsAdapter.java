package com.example.guardinanews;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

/**
 * An {@link NewsAdapter} knows how to create a list item layout for each post
 * in the data source (a list of {@link News} objects).
 *
 * These list item layouts will be provided to an adapter view like ListView
 * to be displayed to the user.
 */
public class NewsAdapter extends ArrayAdapter<News> {

	List<News> mNewsList;

	/**
	 * Constructs a new {@link NewsAdapter}.
	 *
	 * @param context of the app
	 * @param newsList is the list of posts, which is the data source of the adapter
	 */
	public NewsAdapter(@NonNull Context context, List<News> newsList) {
		super(context, 0, newsList);
		mNewsList = newsList;
	}

	/**
	 * Returns a list item view that displays information about the post
	 */
	@NonNull
	@Override
	public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

		// Check if there is an existing list item view (called convertView) that we can reuse,
		// otherwise, if convertView is null, then inflate a new list item layout.
		View view = convertView;
		News currentNews = mNewsList.get(position);
		if (view == null) {
			view = LayoutInflater.from(getContext()).inflate(R.layout.guardian_list_item, parent, false);
		}

		// Take only date
		String trimedDate = currentNews.getDate().substring(0, 10);
		// Date to array
		String[] arrDate = trimedDate.split("-");
		// Create EU date format
		String eudate = arrDate[1] + "." + arrDate[2] + "." + arrDate[0] ;

		TextView title = (TextView) view.findViewById(R.id.title_view);
		title.setText(currentNews.getTitle());

		TextView date = (TextView) view.findViewById(R.id.date_text_view);
		date.setText(eudate);

		TextView type = (TextView) view.findViewById(R.id.type_view);
		type.setText(currentNews.getSection());

		return view;
	}
}
