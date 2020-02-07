package com.example.guardinanews;

/**
 * An {@link News} object contains information related to a single new post from Guardian.
 */
public class News {
	/** Title of the new post */
	private String mTitle;
	/** Data of the new post */
	private String mDate;
	/** URL to complete article of the post */
	private String mUrl;
	/** Author of the post */
	private String mAuthor;
	/** Section for post (economics, sport ,ect...) */
	private String mSection;

	/**
	 * Constructs a new {@link News} object.
	 *
	 * @param title
	 * @param date
	 * @param url
	 * @param author
	 * @param section
	 */
	public News(String title, String date, String url, String author, String section) {
		mTitle = title;
		mDate = date;
		mUrl = url;
		mAuthor = author;
		mSection = section;
	}

	public String getTitle() {
		return mTitle;
	}

	public String getDate() {
		return mDate;
	}


	public String getUrl() {
		return mUrl;
	}


	public String getAuthor() {
		return mAuthor;
	}

	public String getSection() {
		return mSection;
	}
}
