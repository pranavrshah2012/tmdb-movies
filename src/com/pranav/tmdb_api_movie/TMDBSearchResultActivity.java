package com.pranav.tmdb_api_movie;


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
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.daginge.tmdbsearch.MovieResult;
import com.daginge.tmdbsearch.TMDBSearchResultActivity.LazyAdapter;
import com.pranav.tmdb_api_movie.R;
import com.pranav.tmdb_api_movie.MovieResult.Builder;

//
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


public class TMDBSearchResultActivity extends Activity {
	
	final String KEY_TITLE = "title";
    final String KEY_YEAR = "Year";
    final String KEY_RATING = "Rating";
    final String KEY_THUMB_URL = "thumb_url";
    final String KEY_ID = "id";    
    JSONObject jsonMovieObject;    
	StringBuilder imageStringBuilder = new StringBuilder();

    
    ListView list;
    LazyAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imdbsearch_result);
        //getActionBar().setDisplayHomeAsUpEnabled(true);
        
        // Get the intent to get the query.
        Intent intent = getIntent();
        String query = intent.getStringExtra(MainActivity.EXTRA_QUERY);
        
        // Check if the NetworkConnection is active and connected.
        ConnectivityManager connMgr = (ConnectivityManager) 
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            new TMDBQueryManager().execute(query);
        } else {
            TextView textView = new TextView(this);
            textView.setText("No network connection.");
            setContentView(textView);
        }
        
    }
    
    /**
     * Updates the View with the results. This is called asynchronously
     * when the results are ready.
     * @param result The results to be presented to the user.
     */
    public void updateViewWithResults(ArrayList<MovieResult> result) {
    	Log.d("this", this.toString());
    	ListView listView = new ListView(this);
        Log.d("updateViewWithResults", result.toString());
        // Add results to listView.
        ArrayAdapter<MovieResult> adapter = 
                new ArrayAdapter<MovieResult>(this,
                        R.layout.movie_result_list_item, result);
        listView.setAdapter(adapter);
        
        // Update Activity to show listView
        setContentView(listView);
    }
    
    public void update2(ArrayList<HashMap<String, String>> result) {	
    	 
    	Log.d("this", this.toString());
      Log.d("results", result.toString());

  	ListView listView = new ListView(this);
      Log.d("updateViewWithResults", result.toString());
      // Add results to listView.
//      ArrayAdapter<HashMap<String, String>> adapter = 
//              new ArrayAdapter<HashMap<String, String>>(this,
//                      R.layout.movie2_result, R.id.rating, result);
      
      LazyAdapter adapter = 
              new LazyAdapter(this, result);
      listView.setAdapter(adapter);
      // Update Activity to show listView
      setContentView(listView);
    	
    	
//    	ListView listView = new ListView(this);
//    	
//    	   // Add results to listView.
//
//        ArrayAdapter adapter = new ArrayAdapter
//        (this,R.layout.movie2_result, R.id.rating, result);
//        listView.setAdapter(adapter);
//        // Update Activity to show listView
//        setContentView(listView);	
    	
    	
        
  
//         // Click event for single list row
//         list.setOnItemClickListener(new OnItemClickListener() {
//  
//             @Override
//             public void onItemClick(AdapterView&lt;?&gt; parent, View view,
//                     int position, long id) {
//  
//             }
//         });
    }
   
    
    private class TMDBQueryManager extends AsyncTask {
        
        private final String TMDB_API_KEY = "c47afb8e8b27906bca710175d6e8ba68";
        private static final String DEBUG_TAG = "TMDBQueryManager";
        
        @Override
        protected ArrayList<MovieResult> doInBackground(Object... params) {
            try {
            	return search2 ((String) params[0]);
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
         * @param query The query to search.
         * @return A list of all hits.
         */public ArrayList<MovieResult> searchIMDB(String query) throws IOException {
             // Build URL
         	//addding support for images
         	StringBuilder imageStringBuilder = new StringBuilder();
         	imageStringBuilder.append("http://image.tmdb.org/t/p/w500/");
//         			"http://image.tmdb.org/t/p/w500/g5ZHGeWNY5zUcojk0Xxk1KNxqAl.jpg
         	
             StringBuilder stringBuilder = new StringBuilder();
             stringBuilder.append("https://api.themoviedb.org/3/search/movie");
             stringBuilder.append("?api_key=" + TMDB_API_KEY);
             stringBuilder.append("&query=" + query);
             URL url = new URL(stringBuilder.toString());
             
             InputStream stream = null;
             try {
                 // Establish a connection
                 HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                 conn.setReadTimeout(10000 /* milliseconds */);
                 conn.setConnectTimeout(15000 /* milliseconds */);
                 conn.setRequestMethod("GET");
                 conn.addRequestProperty("Accept", "application/json"); // Required to get TMDB to play nicely.
                 conn.setDoInput(true);
                 conn.connect();
                 
                 int responseCode = conn.getResponseCode();
                 Log.d(DEBUG_TAG, "The response code is: " + responseCode + " " + conn.getResponseMessage());
                 
                 stream = conn.getInputStream();
                 return parseResult(stringify(stream));
             } finally {
                 if (stream != null) {
                     stream.close();
                 }
             }
         }
         
         public ArrayList<HashMap<String, String>> search2(String query) throws IOException {
             // Build URL
         	//addding support for images
         	imageStringBuilder.append("http://image.tmdb.org/t/p/w500");
//         			"http://image.tmdb.org/t/p/w500/g5ZHGeWNY5zUcojk0Xxk1KNxqAl.jpg
         	
             StringBuilder stringBuilder = new StringBuilder();
             stringBuilder.append("https://api.themoviedb.org/3/search/movie");
             stringBuilder.append("?api_key=" + TMDB_API_KEY);
             stringBuilder.append("&query=" + query);
             URL url = new URL(stringBuilder.toString());
             
             InputStream stream = null;
             try {
                 // Establish a connection
                 HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                 conn.setReadTimeout(10000 /* milliseconds */);
                 conn.setConnectTimeout(15000 /* milliseconds */);
                 conn.setRequestMethod("GET");
                 conn.addRequestProperty("Accept", "application/json"); // Required to get TMDB to play nicely.
                 conn.setDoInput(true);
                 conn.connect();
                 
                 int responseCode = conn.getResponseCode();
                 Log.d(DEBUG_TAG, "The response code is: " + responseCode + " " + conn.getResponseMessage());
                 
                 stream = conn.getInputStream();
//                 return parseResult(stringify(stream));
                 return parse2(stringify(stream));
             } finally {
                 if (stream != null) {
                     stream.close();
                 }
             }
         }



        private ArrayList<MovieResult> parseResult(String result) {
            String streamAsString = result;
            ArrayList<MovieResult> results = new ArrayList<MovieResult>();
            try {
                JSONObject jsonObject = new JSONObject(streamAsString);
                JSONArray array = (JSONArray) jsonObject.get("results");
                for (int i = 0; i < array.length(); i++) {
                    JSONObject jsonMovieObject = array.getJSONObject(i);
                    Builder movieBuilder = MovieResult.newBuilder(
                            Integer.parseInt(jsonMovieObject.getString("id")),
                            jsonMovieObject.getString("title"))
                            .setBackdropPath(jsonMovieObject.getString("backdrop_path"))
                  .setPosterPath(jsonMovieObject.getString("poster_path"))
                  .setReleaseDate(jsonMovieObject.getString("release_date"))         
                  .setOriginalTitle(jsonMovieObject.getString("original_title"));
                         
//                    .setPopularity(jsonMovieObject.getString("popularity"))
                            
                            
                    results.add(movieBuilder.build());
                }
            } catch (JSONException e) {
                System.err.println(e);
                Log.d(DEBUG_TAG, "Error parsing JSON. String was: " + streamAsString);
            }
            return results;
        }
        
        public String stringify(InputStream stream) throws IOException, UnsupportedEncodingException {
            Reader reader = null;
            reader = new InputStreamReader(stream, "UTF-8");        
            BufferedReader bufferedReader = new BufferedReader(reader);
            return bufferedReader.readLine();
        }
    }
    
 }




