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
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.pranav.tmdb_api_movie.R;

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
//extra



import com.pranav.tmdb_api_movie.ImageLoader;
 
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
 
public class Detailed_View extends Activity {
    // Declare Variables
	final String KEY_TITLE = "title";
    final String KEY_YEAR = "Year";
    final String KEY_RATING = "Rating";
    final String KEY_THUMB_URL = "thumb_url";
    final String KEY_ID = "id";
    final String KEY_CAST = "cast";
    
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
    TextView txtsynopsis ;
    ImageView bigger_image;
  
    ImageLoader imageLoader ;
 
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Get the view from singleitemview.xml
        setContentView(R.layout.detailed_view);
 
        Intent i = getIntent();
        // Get the result of rank
        title = i.getStringExtra("title");
        // Get the result of country
        imageLoader =  new ImageLoader(getApplicationContext());
       
        synopsis = i.getStringExtra("Rating");
        // Get the result of flag
        credits = i.getStringExtra("credits");
        //get url
        imgUrl = i.getStringExtra("thumb_url");
        id = i.getStringExtra("id");
//        String cast_Got = i.getStringExtra("cast");
//        Log.d("cast_got ", cast_Got);
        Log.d("ids", id );
      
    
        TextView txtname = (TextView)findViewById(R.id.name_of_movie);
//        TextView txtsynopsis = (TextView)findViewById(R.id.movie_details);
        ImageView bigger_image=(ImageView)findViewById(com.pranav.tmdb_api_movie.R.id.image_of_movie); // bigger image
       
        
        // *     works good, except created new page
        txtname.setText(title);
//        txtsynopsis.setText(synopsis);
//        
       
        // Capture position and set results to the ImageView
        // Passes flag images URL into ImageLoader.class
        imageLoader.DisplayImage(imgUrl, bigger_image);
        
 
        
        new TMDBDetail().execute();

    }
    
public void updateCast(ArrayList<HashMap<String, String>> result) {	
    	ListView listView = (ListView) findViewById(R.id.cast_details);
      	Log.d("from updateCast function"," "+result.toString()); // got cast

    	// Add results to listView.
         adapter = new LazyDetail(this, result);
        listView.setAdapter(adapter);
        // Update Activity to show listView
//        setContentView(listView);	
        
    	
    }


//displays only text credit
public void updateListOfCast(ArrayList<String> result) {
	Log.d("this", this.toString());
    Log.d("results", result.toString());

    ListView listView = (ListView) findViewById(R.id.cast_details);
    Log.d("updateViewWithResults", result.toString());
    // Add results to listView.
    ArrayAdapter<ArrayList<String>> adapter = 
            new ArrayAdapter<ArrayList<String>>(this, android.R.layout.simple_list_item_1, (List)result);
    listView.setAdapter(adapter);
    // Update Activity to show listView
//    setContentView(listView);?
}


public class LazyDetail extends BaseAdapter {
    HashMap<String, String> movie = new HashMap<String, String>();

    private Activity activity;
    private ArrayList<HashMap<String, String>> data;
    private  LayoutInflater inflater=null;
    public ImageLoader imageLoader; 
 
    public LazyDetail(Activity a, ArrayList<HashMap <String, String>> d) {
        activity = a;
        data=d;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        imageLoader=new ImageLoader(activity.getApplicationContext());
    }
 
    public LazyDetail(TMDBSearchResultActivity tmdbSearchResultActivity,
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
            vi = inflater.inflate(com.pranav.tmdb_api_movie.R.layout.detailed_view, null);
        
//      Locate the TextViews in singleitemview.xml
        TextView castname = (TextView)vi.findViewById(com.pranav.tmdb_api_movie.R.id.cast_details);
//        TextView txtsynopsis = (TextView)vi.findViewById(com.daginge.tmdbsearch.R.id.movie_details);
        movie = data.get(position);
      Log.d("movie view detailed", position + movie.toString());
      
      /*
       * 
//	  Log.d("txtname", "is null");
//	  Log.d("title", title.toString() );
//	  
//      txtname.setText(title);
//      
//      txtsynopsis.setText(synopsis);

       */


       
// works on new page
      Log.d("actor", movie.get("cast"));
//        // Set results to the TextViews
      
      castname.setText(movie.get("cast"));
//      txtsynopsis.setText(synopsis);
//      
    
      // Capture position and set results to the ImageView
      // Passes flag images URL into ImageLoader.class
//      imageLoader.DisplayImage(imgUrl, bigger_image);
//  
      
 
        
 /*
        
//        Log.d("data view", data.toString());
//        Log.d("movie view detailed", position + movie.toString());
        
        
  */       
        return vi;
    }
}//

	

//added in sleep!remove it!

private class TMDBDetail extends AsyncTask<Object, Void, ArrayList<String>> {
  
  private final String TMDB_API_KEY = "c47afb8e8b27906bca710175d6e8ba68";
  private static final String DEBUG_TAG = "TMDBQueryManager";
  
  @Override
  protected ArrayList<String> doInBackground(Object... params) {
      try {
      	return searchMoreDetail ();
      } catch (IOException e) {
          return null;
      }
  }
  
  @Override
  protected void onPostExecute(ArrayList<String> results_Cast) {

//  	Log.d("cast - detailed view"," "+results_Cast.toString()); // got cast
//View vi = inflater.inflate(com.daginge.tmdbsearch.R.layout.detailed_view, null);
//
//	  Log.d("txtname", "is null");
//	  Log.d("title", title.toString() );
//	  
//      txtname.setText(title);
//      
//      txtsynopsis.setText(synopsis);
//      
     
      // Capture position and set results to the ImageView
      // Passes flag images URL into ImageLoader.class
//      imageLoader.DisplayImage(imgUrl, bigger_image);
//  	cast_array_list = (ArrayList) results_Cast;
  			updateListOfCast(results_Cast);
//  	updateCast((ArrayList<HashMap<String, String>>) results_Cast);
  };



  public ArrayList<String> searchMoreDetail() throws IOException {
      // Build URL
  	//addding support for images
//  	imageStringBuilder.append("http://image.tmdb.org/t/p/w500");
//  			"http://image.tmdb.org/t/p/w500/g5ZHGeWNY5zUcojk0Xxk1KNxqAl.jpg
  	
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("https://api.themoviedb.org/3/movie/"+id+"/credits");
      stringBuilder.append("?api_key=" + TMDB_API_KEY);
//      stringBuilder.append("&query=" + query);
      URL url = new URL(stringBuilder.toString());
//      Log.d("urlstring",stringBuilder.toString() );
      
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
//          return parseResult(stringify(stream));
          return parseDetail(stringify(stream));
      } finally {
          if (stream != null) {
              stream.close();
          }
      }
  }
  



  private ArrayList<String> parseDetail(String result) {
      String streamAsString = result;        
       
      
      ArrayList<String> results_Cast = new ArrayList<String>();
      try {
          JSONObject jsonObject = new JSONObject(streamAsString);
          JSONArray array = (JSONArray) jsonObject.get("cast");
          Log.d("array view", array.toString());
          for (int i = 0; i < array.length(); i++) {
             HashMap <String, String> map = new HashMap <String, String>();
          	JSONObject jsonMovieObject = array.getJSONObject(i);
              results_Cast.add(jsonMovieObject.getString("name"));
          }
      } catch (JSONException e) {
          Log.d("e" , e.toString());
          Log.d(DEBUG_TAG, "Error parsing JSON. String was: " + streamAsString);
      }
Log.d("resulted", results_Cast.toString());
      return results_Cast;
  }
  
  public String stringify(InputStream stream) throws IOException, UnsupportedEncodingException {
      Reader reader = null;
      reader = new InputStreamReader(stream, "UTF-8");        
      BufferedReader bufferedReader = new BufferedReader(reader);
      return bufferedReader.readLine();
  }
}





    
}


