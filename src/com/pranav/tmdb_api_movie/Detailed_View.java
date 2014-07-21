package com.pranav.tmdb_api_movie;

import java.lang.ref.SoftReference;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import android.graphics.Bitmap;
//memory cache

import java.io.File;
import android.content.Context;
//file cache

import java.io.InputStream;
import java.io.OutputStream;
//utils

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
//onclick

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;
//imageloader

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

//lazy

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.pranav.tmdb_api_movie.R;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.content.ContextWrapper;

import org.json.JSONObject;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.ListView;
import android.widget.SimpleAdapter;
//utils
import com.pranav.tmdb_api_movie.ImageLoader;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class Detailed_View extends Activity {
	// Declare Variables
	private final String KEY_TITLE = "title";
	private final String KEY_YEAR = "Year";
	private final String KEY_RATING = "Rating";
	private final String KEY_THUMB_URL = "thumb_url";
	private final String KEY_ID = "id";
	private final String KEY_CAST = "cast";

	String id;
	String cast;
	String title;
	String rating;
	String synopsis;
	String credits;
	String imgUrl;
	ListView list;
	LazyDetail adapter;

	TextView txtname;
	TextView txtsynopsis;
	ImageView bigger_image;

	ImageLoader imageLoader;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.detailed_view);

		Intent i = getIntent();
		// get previous values
		title = i.getStringExtra("title");
		imageLoader = new ImageLoader(getApplicationContext());

		rating = i.getStringExtra("Rating");
		credits = i.getStringExtra("credits");

		imgUrl = i.getStringExtra("thumb_url");
		id = i.getStringExtra("id");

		TextView txtname = (TextView) findViewById(R.id.name_of_movie);
		 TextView txtrating = (TextView)findViewById(R.id.rating);
		ImageView bigger_image = (ImageView) findViewById(com.pranav.tmdb_api_movie.R.id.image_of_movie); // bigger
																											// image

		txtname.setText(title);
		 txtrating.setText(rating);


		imageLoader.DisplayImage(imgUrl, bigger_image);

		new TMDBCast().execute();
		new TMDBSynopsis().execute();

	}

	public void updateCast(ArrayList<HashMap<String, String>> result) {
		ListView listView = (ListView) findViewById(R.id.cast_details);
		Log.d("from updateCast function", " " + result.toString()); // got cast

		// Add results to listView.
		adapter = new LazyDetail(this, result);
		listView.setAdapter(adapter);
	}

	// displays cast
	public void updateListOfCast(ArrayList<String> result) {
		Log.d("this", this.toString());
		Log.d("results", result.toString());

		ListView listView = (ListView) findViewById(R.id.cast_details);
		Log.d("updateViewWithResults", result.toString());
		// Add results to listView.
		ArrayAdapter<ArrayList<String>> adapter = new ArrayAdapter<ArrayList<String>>(
				this, android.R.layout.simple_list_item_1, (List) result);
		listView.setAdapter(adapter);

	}
	
	public void updateSynopsis(ArrayList<String> result) {
		TextView txtSynopsis = (TextView) findViewById(R.id.synopsis);
        txtSynopsis.setMovementMethod(new ScrollingMovementMethod());
		txtSynopsis.setText(result.get(0));

		
	}
	
	

	public class LazyDetail extends BaseAdapter {
		HashMap<String, String> movie = new HashMap<String, String>();

		private Activity activity;
		private ArrayList<HashMap<String, String>> data;
		private LayoutInflater inflater = null;
		public ImageLoader imageLoader;

		public LazyDetail(Activity a, ArrayList<HashMap<String, String>> d) {
			activity = a;
			data = d;
			inflater = (LayoutInflater) activity
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			imageLoader = new ImageLoader(activity.getApplicationContext());
		}

		public LazyDetail(SearchActivity tmdbSearchResultActivity,
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
						com.pranav.tmdb_api_movie.R.layout.detailed_view, null);

			// Locate the TextViews
			TextView castname = (TextView) vi
					.findViewById(com.pranav.tmdb_api_movie.R.id.cast_details);
			// TextView txtsynopsis =
			// (TextView)vi.findViewById(com.daginge.tmdbsearch.R.id.movie_details);
			movie = data.get(position);
			// Log.d("movie view detailed", position + movie.toString());
			// Log.d("actor", movie.get("cast"));
			// Set results to the TextViews

			castname.setText(movie.get("cast"));
			// txtsynopsis.setText(synopsis);

			// imageLoader.DisplayImage(imgUrl, bigger_image);

			return vi;
		}
	}//

	private class TMDBCast extends AsyncTask<Object, Void, ArrayList<String>> {

		private final String TMDB_API_KEY = "c47afb8e8b27906bca710175d6e8ba68";
		private static final String DEBUG_TAG = "TMDBQueryManager";

		@Override
		protected ArrayList<String> doInBackground(Object... params) {
			try {
				return getCast();
			} catch (IOException e) {
				return null;
			}
		}

		@Override
		protected void onPostExecute(ArrayList<String> results_Cast) {
			updateListOfCast(results_Cast);

		};

		public ArrayList<String> getCast() throws IOException {

			StringBuilder stringBuilder = new StringBuilder();
			stringBuilder.append("https://api.themoviedb.org/3/movie/" + id
					+ "/credits");
			stringBuilder.append("?api_key=" + TMDB_API_KEY);
			URL url = new URL(stringBuilder.toString());
			// Log.d("urlstring",stringBuilder.toString() );

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
				return parseCast(stringify(stream));
			} finally {
				if (stream != null) {
					stream.close();
				}
			}
		}

		private ArrayList<String> parseCast(String result) {
			String streamAsString = result;

			ArrayList<String> results_Cast = new ArrayList<String>();
			try {
				JSONObject jsonObject = new JSONObject(streamAsString);
				JSONArray array = (JSONArray) jsonObject.get("cast");
				Log.d("array view", array.toString());
				for (int i = 0; i < array.length(); i++) {
					HashMap<String, String> map = new HashMap<String, String>();
					JSONObject jsonMovieObject = array.getJSONObject(i);
					results_Cast.add(jsonMovieObject.getString("name"));
				}
			} catch (JSONException e) {
				Log.d("e", e.toString());
				Log.d(DEBUG_TAG, "Error parsing JSON. String was: "
						+ streamAsString);
			}
			// Log.d("resulted", results_Cast.toString());
			return results_Cast;
		}

		public String stringify(InputStream stream) throws IOException,
				UnsupportedEncodingException {
			Reader reader = null;
			reader = new InputStreamReader(stream, "UTF-8");
			BufferedReader bufferedReader = new BufferedReader(reader);
			return bufferedReader.readLine();
		}
	}

	private class TMDBSynopsis extends
			AsyncTask<Object, Void, ArrayList<String>> {

		private final String TMDB_API_KEY = "c47afb8e8b27906bca710175d6e8ba68";
		private static final String DEBUG_TAG = "TMDBQueryManager";

		@Override
		protected ArrayList<String> doInBackground(Object... params) {
			try {
				return getSynopsis();
			} catch (IOException e) {
				return null;
			}
		}

		@Override
		protected void onPostExecute(ArrayList<String> results_Cast) {
			updateSynopsis(results_Cast);

		};

		public ArrayList<String> getSynopsis() throws IOException {

			StringBuilder stringBuilder = new StringBuilder();
			stringBuilder.append("https://api.themoviedb.org/3/movie/" + id);
			stringBuilder.append("?api_key=" + TMDB_API_KEY);
			URL url = new URL(stringBuilder.toString());
			// Log.d("urlstring",stringBuilder.toString() );

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
				return parseSynopsis(stringify(stream));
			} finally {
				if (stream != null) {
					stream.close();
				}
			}
		}

		private ArrayList<String> parseSynopsis(String result) {
			String streamAsString = result;

			ArrayList<String> results_Cast = new ArrayList<String>();
			try {
				JSONObject jsonObject = new JSONObject(streamAsString);
				 Log.d("overview","synopsis : "+jsonObject.getString("overview"));
				//
				results_Cast.add(jsonObject.getString("overview"));

				// }
			} catch (JSONException e) {
				Log.d("e", e.toString());
				Log.d(DEBUG_TAG, "Error parsing JSON. String was: "
						+ streamAsString);
			}
			// Log.d("resulted", results_Cast.toString());
			return results_Cast;
		}

		public String stringify(InputStream stream) throws IOException,
				UnsupportedEncodingException {
			Reader reader = null;
			reader = new InputStreamReader(stream, "UTF-8");
			BufferedReader bufferedReader = new BufferedReader(reader);
			return bufferedReader.readLine();
		}
	}

}
