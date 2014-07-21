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

import com.pranav.tmdb_api_movie.Detailed_View;
import com.pranav.tmdb_api_movie.ImageLoader;
import com.pranav.tmdb_api_movie.MovieResult;
import com.pranav.tmdb_api_movie.Now_Viewing;
import com.pranav.tmdb_api_movie.TMDBSearchResultActivity;
import com.pranav.tmdb_api_movie.Now_Viewing.TMDBNow_View;
import com.pranav.tmdb_api_movie.MainActivity;
import com.pranav.tmdb_api_movie.Now_Viewing.LazyAdapter;
import com.pranav.tmdb_api_movie.R;
import com.pranav.tmdb_api_movie.MovieResult.Builder;

public class Now_Viewing extends Activity {
	final String KEY_TITLE = "title";
    final String KEY_YEAR = "Year";
    final String KEY_RATING = "Rating";
    final String KEY_THUMB_URL = "thumb_url";
    final String KEY_ID = "id";
    
    JSONObject jsonMovieObject;
    String id;
    
	StringBuilder imageStringBuilder = new StringBuilder();

    ListView list;
    LazyAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nowviewing_result);
        //getActionBar().setDisplayHomeAsUpEnabled(true);
        
        // Get the intent to get the query.
        Intent intent = getIntent();
        String query = intent.getStringExtra(MainActivity.EXTRA_QUERY);
        
        // Check if the NetworkConnection is active and connected.
        ConnectivityManager connMgr = (ConnectivityManager) 
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            new TMDBNow_View().execute();
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
 //works
//    	Log.d("this", this.toString());
//      Log.d("results", result.toString());
//
//  	ListView listView = new ListView(this);
//      Log.d("updateViewWithResults", result.toString());
//      // Add results to listView.
//      ArrayAdapter<HashMap<String, String>> adapter = 
//              new ArrayAdapter<HashMap<String, String>>(this,
//                      R.layout.movie2_result, R.id.rating, result);
//      listView.setAdapter(adapter);
//      // Update Activity to show listView
//      setContentView(listView);
   //end 	
    	
    	
    	ListView listView = new ListView(this);
    	
    	   // Add results to listView.
         adapter = new LazyAdapter(this, result);
        listView.setAdapter(adapter);
        // Update Activity to show listView
        setContentView(listView);	
    	
    	
        
//  
//         // Click event for single list row
//         listView.setOnItemClickListener(new OnItemClickListener() {
//  
//             @Override
//             public void onItemClick(AdapterView<?> parent, View view,
//                     int position, long id) {
//  
//             }
//         });
    }
    
    public class TMDBNow_View extends AsyncTask {
        
        private final String TMDB_API_KEY = "c47afb8e8b27906bca710175d6e8ba68";
        private static final String DEBUG_TAG = "TMDBQueryManager";
        
        @Override
        protected ArrayList<HashMap<String, String>> doInBackground(Object... params) {
            try {
                return displayNowPlayingMovies();
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
         */
        public ArrayList<HashMap<String, String>> displayNowPlayingMovies() throws IOException {
            // Build URL
        	//https://api.themoviedb.org/3/movie/now_playing?api_key=c47afb8e8b27906bca710175d6e8ba68
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("https://api.themoviedb.org/3/movie/now_playing");
            stringBuilder.append("?api_key=" + TMDB_API_KEY);
//            stringBuilder.append("&query=" + query); // mk query null future?
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
                return parseNow_View(stringify(stream));
            } finally {
                if (stream != null) {
                    stream.close();
                }
            }
        }

        private ArrayList<HashMap<String, String>> parseNow_View(String result) {
            String streamAsString = result;
            ArrayList<HashMap<String, String>> results = new ArrayList<HashMap<String, String>>();
            try {
                JSONObject jsonObject = new JSONObject(streamAsString);
                JSONArray array = (JSONArray) jsonObject.get("results");
                for (int i = 0; i < array.length(); i++) {
                    HashMap <String, String> map = new HashMap <String, String>();
                    JSONObject jsonMovieObject = array.getJSONObject(i);
                	map.put(KEY_TITLE, jsonMovieObject.getString("original_title"));
                	map.put(KEY_YEAR, jsonMovieObject.getString("release_date"));
                	map.put(KEY_RATING, jsonMovieObject.getString("vote_average"));
                	map.put(KEY_ID, jsonMovieObject.getString("id"));
                	map.put(KEY_THUMB_URL, "http://image.tmdb.org/t/p/w500"+jsonMovieObject.getString("poster_path"));
                 
                    results.add(map);
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


public class LazyAdapter extends BaseAdapter {
    HashMap<String, String> movie = new HashMap<String, String>();

    private Activity activity;
    private ArrayList<HashMap<String, String>> data;
    private  LayoutInflater inflater=null;
    public ImageLoader imageLoader; 
 
    public LazyAdapter(Activity a, ArrayList<HashMap <String, String>> d) {
        activity = a;
        data=d;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        imageLoader=new ImageLoader(activity.getApplicationContext());
    }
 
    public LazyAdapter(TMDBSearchResultActivity tmdbSearchResultActivity,
			int movie2Result, int rating,
			ArrayList<HashMap<String, String>> result) {
		// TODO Auto-generated constructor stub
	}

	public int getCount() {
//		Log.d("size", data.size()+"");
        return data.size();
    }
 
    
    public long getItemId(int position) {
        return position;
    }
    
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}
    
    
 
    public View getView(final int position, View convertView, ViewGroup parent) {
        View vi=convertView;
        if(convertView==null)
            vi = inflater.inflate(com.daginge.tmdbsearch.R.layout.movie2_result, null);
 
        TextView title = (TextView)vi.findViewById(com.daginge.tmdbsearch.R.id.title); // title
        
        TextView year = (TextView)vi.findViewById(com.daginge.tmdbsearch.R.id.rating); // duration
        TextView rating = (TextView)vi.findViewById(com.daginge.tmdbsearch.R.id.year); // artist name
        ImageView thumb_image=(ImageView)vi.findViewById(com.daginge.tmdbsearch.R.id.list_image); // thumb image
 
        movie = data.get(position);
//        Log.d("search view detailed", position + movie.toString());

        
        
//        Log.d("data view", data.toString());
//        Log.d("movie view detailed", position + movie.toString());
 
//        Setting all values in listview   
        
//        try{
//        Log.d("jsonMovie", jsonMovieObject.toString());	
        title.setText(movie.get("title"));
        year.setText(movie.get("Year"));
        rating.setText(movie.get("Rating"));
//        imageStringBuilder.append(jsonMovieObject.getString("poster_path"));
        imageLoader.DisplayImage(movie.get("thumb_url"), thumb_image);
        
        
        vi.setOnClickListener(new OnClickListener() {
       	 
            @Override
            public void onClick(View arg0) {
                // Get the position
                movie = data.get(position);
//                new TMDBDetail().execute();

                Intent intent = new Intent(Now_Viewing.this, Detailed_View.class);

                // Pass all data rank
                intent.putExtra("title", movie.get("title"));
                // Pass all data country
                intent.putExtra("Year", movie.get("Year"));
                // Pass all data population
                intent.putExtra("Rating",movie.get("Rating"));
                // Pass all data flag
                intent.putExtra("thumb_url", movie.get("thumb_url"));
                // Start SingleItemView Class
                intent.putExtra("id", movie.get("id"));
                id = intent.getStringExtra("id");
                
//                for(int i =0; i < cast.size(); i++){
//                intent.putExtra("cast", cast.toString() );
//                }
                

                startActivity(intent);
 
            }
        });
        
        
        
//        Log.d("image", imageStringBuilder.toString());
//        }
//        catch(Exception e)
//        {
//        	Log.d("exception??",e.toString());
//        }
        
        
//        single click
        
        
        
/*
 * KEY_TITLE = "title";
    KEY_YEAR = "Year";
   KEY_RATING = "Rating";
     KEY_THUMB_URL
 */

        
        
        return vi;
    }
    
    

	
}


}
