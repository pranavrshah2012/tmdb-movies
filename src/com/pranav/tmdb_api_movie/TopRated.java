package com.pranav.tmdb_api_movie;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.pranav.tmdb_api_movie.R;
import com.pranav.tmdb_api_movie.SearchActivity.LazyAdapter;

public class TopRated extends Activity {
	private final String KEY_TITLE = "title";
	private  final String KEY_YEAR = "Year";
	private  final String KEY_RATING = "Rating";
	private  final String KEY_THUMB_URL = "thumb_url";
	private  final String KEY_ID = "id";

	JSONObject jsonMovieObject;
	String id;

	StringBuilder imageStringBuilder = new StringBuilder();

	ListView list;
	LazyAdapter adapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_top_rated);
		// getActionBar().setDisplayHomeAsUpEnabled(true);

		// Get the intent to get the query.
		Intent intent = getIntent();
		String query = intent.getStringExtra(MainActivity.EXTRA_QUERY);

		// Check if the NetworkConnection is active and connected.
		ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
		if (networkInfo != null && networkInfo.isConnected()) {
			new TMDBTop().execute();
		} else {
			TextView textView = new TextView(this);
			textView.setText("No network connection.");
			setContentView(textView);
		}

	}

	/**
	 * Updates the View with the results. This is called asynchronously when the
	 * results are ready.
	 *            
	 */

	public void update2(ArrayList<HashMap<String, String>> result) {

		ListView listView = new ListView(this);

		// Add results to listView.
		adapter = new LazyAdapter(this, result);
		listView.setAdapter(adapter);
		// Update Activity to show listView
		setContentView(listView);

	}

	private class TMDBTop extends AsyncTask {

		private final String TMDB_API_KEY = "c47afb8e8b27906bca710175d6e8ba68";
		private static final String DEBUG_TAG = "TMDBQueryManager";

		@Override
		protected ArrayList<HashMap<String, String>> doInBackground(
				Object... params) {
			try {
				return displayTopMovies();
			} catch (IOException e) {
				return null;
			}
		}

		@Override
		protected void onPostExecute(Object result) {
			update2((ArrayList<HashMap<String, String>>) result);
		};

		/**
		 * Searches IMDBs API for the given query
		 */
		public ArrayList<HashMap<String, String>> displayTopMovies()
				throws IOException {
			// Build URL
			// https://api.themoviedb.org/3/movie/now_playing?api_key=c47afb8e8b27906bca710175d6e8ba68
			StringBuilder stringBuilder = new StringBuilder();
			stringBuilder
					.append("https://api.themoviedb.org/3/movie/top_rated");
			stringBuilder.append("?api_key=" + TMDB_API_KEY);
			// stringBuilder.append("&query=" + query); // mk query null future?
			URL url = new URL(stringBuilder.toString());

			InputStream stream = null;
			try {
				// Establish a connection
				HttpURLConnection conn = (HttpURLConnection) url
						.openConnection();
				conn.setReadTimeout(10000 /* milliseconds */);
				conn.setConnectTimeout(15000 /* milliseconds */);
				conn.setRequestMethod("GET");
				conn.addRequestProperty("Accept", "application/json"); // Required
																		// to
																		// get
																		// TMDB
																		// to
																		// play
																		// nicely.
				conn.setDoInput(true);
				conn.connect();

				int responseCode = conn.getResponseCode();
				Log.d(DEBUG_TAG, "The response code is: " + responseCode + " "
						+ conn.getResponseMessage());

				stream = conn.getInputStream();
				return parseTopMovies(stringify(stream));
			} finally {
				if (stream != null) {
					stream.close();
				}
			}
		}

		private ArrayList<HashMap<String, String>> parseTopMovies(String result) {
			String streamAsString = result;
			ArrayList<HashMap<String, String>> results = new ArrayList<HashMap<String, String>>();
			try {
				JSONObject jsonObject = new JSONObject(streamAsString);
				JSONArray array = (JSONArray) jsonObject.get("results");
				for (int i = 0; i < array.length(); i++) {
					HashMap<String, String> map = new HashMap<String, String>();
					JSONObject jsonMovieObject = array.getJSONObject(i);
					map.put(KEY_TITLE,
							jsonMovieObject.getString("original_title"));
					map.put(KEY_YEAR, jsonMovieObject.getString("release_date"));
					map.put(KEY_RATING,
							jsonMovieObject.getString("vote_average"));
					map.put(KEY_ID, jsonMovieObject.getString("id"));
					map.put(KEY_THUMB_URL, "http://image.tmdb.org/t/p/w500"
							+ jsonMovieObject.getString("poster_path"));

					results.add(map);
				}
			} catch (JSONException e) {
				System.err.println(e);
				Log.d(DEBUG_TAG, "Error parsing JSON. String was: "
						+ streamAsString);
			}
			return results;
		}

		public String stringify(InputStream stream) throws IOException,
				UnsupportedEncodingException {
			Reader reader = null;
			reader = new InputStreamReader(stream, "UTF-8");
			BufferedReader bufferedReader = new BufferedReader(reader);
			return bufferedReader.readLine();
		}
	}

	public class LazyAdapter extends BaseAdapter {
		HashMap<String, String> movie = new HashMap<String, String>();

		private Activity activity;
		private ArrayList<HashMap<String, String>> data;
		private LayoutInflater inflater = null;
		public ImageLoader imageLoader;

		public LazyAdapter(Activity a, ArrayList<HashMap<String, String>> d) {
			activity = a;
			data = d;
			inflater = (LayoutInflater) activity
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			imageLoader = new ImageLoader(activity.getApplicationContext());
		}

		public LazyAdapter(TopRated TopRatedActivity,
				int movie2Result, int rating,
				ArrayList<HashMap<String, String>> result) {
			// TODO Auto-generated constructor stub
		}

		public int getCount() {
			return data.size();
		}

		public long getItemId(int position) {
			return position;
		}

		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		public View getView(final int position, View convertView,
				ViewGroup parent) {
			View vi = convertView;
			if (convertView == null)
				vi = inflater.inflate(
						com.pranav.tmdb_api_movie.R.layout.top_rated, null);

			TextView title = (TextView) vi
					.findViewById(com.pranav.tmdb_api_movie.R.id.title); // title

			TextView year = (TextView) vi
					.findViewById(com.pranav.tmdb_api_movie.R.id.rating); // duration
			TextView rating = (TextView) vi
					.findViewById(com.pranav.tmdb_api_movie.R.id.year); // artist
																		// name
			ImageView thumb_image = (ImageView) vi
					.findViewById(com.pranav.tmdb_api_movie.R.id.list_image); // thumb
																				// image

			movie = data.get(position);
			// Log.d("search view detailed", position + movie.toString());

			title.setText(movie.get("title"));
			year.setText(movie.get("Year"));
			rating.setText(movie.get("Rating"));
			imageLoader.DisplayImage(movie.get("thumb_url"), thumb_image);

			vi.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// Get the position
					movie = data.get(position);
					// new TMDBDetail().execute();

					Intent intent = new Intent(TopRated.this,
							Detailed_View.class);

					// pass required data to detail_view
					intent.putExtra("title", movie.get("title"));
					intent.putExtra("Year", movie.get("Year"));
					intent.putExtra("Rating", movie.get("Rating"));
					intent.putExtra("thumb_url", movie.get("thumb_url"));
					intent.putExtra("id", movie.get("id"));
					id = intent.getStringExtra("id");
					startActivity(intent);

				}
			});


			return vi;
		}

	}

}
