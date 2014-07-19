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


//import com.pranav.R;
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


public class Display_With_Image extends Activity {

	public void displayDownloadedImages (HashMap<String, Object> result){
	  	  // Getting the path to the downloaded image
			ListView listView = new ListView (this);
	   String path = (String) result.get("poster_path");

	   // Getting the position of the downloaded image
	   int position = (Integer) result.get("position");

	   // Getting adapter of the listview
	   SimpleAdapter adapter = (SimpleAdapter ) listView.getAdapter();

	   // Getting the hashmap object at the specified position of the listview
	   HashMap<String, Object> hm = (HashMap<String, Object>) adapter.getItem(position);

	   // Overwriting the existing path in the adapter
	   hm.put("position_path",path);

	   // Noticing listview about the dataset changes
	   adapter.notifyDataSetChanged();
	  
	  
	  }
	
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
//            new ImageDownloading().execute(query);
        } 
    }
    
class ImageDownloading extends AsyncTask<HashMap<String, Object>, Void, HashMap<String, Object>>{
	public String imgUrl;
     @Override
     protected HashMap<String, Object> doInBackground(HashMap<String, Object>... hm) {

         InputStream iStream=null;
         
           imgUrl = (String) hm[0].get("poster_path");


         URL url;
         try {
        	 if(imgUrl == null)
           	Log.i("imgUrl", "hello");

             url = new URL(imgUrl);

             // Creating an http connection to communicate with url
             HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

             // Connecting to url
             urlConnection.connect();

             // Reading data from url
             iStream = urlConnection.getInputStream();

             // Getting Caching directory
             File cacheDirectory = getBaseContext().getCacheDir();

             // Temporary file to store the downloaded image
             File tmpFile = new File(cacheDirectory.getPath() + ".png");

             // The FileOutputStream to the temporary file
             FileOutputStream fOutStream = new FileOutputStream(tmpFile);

             // Creating a bitmap from the downloaded inputstream
             Bitmap b = BitmapFactory.decodeStream(iStream);

             // Writing the bitmap to the temporary file as png file
             b.compress(Bitmap.CompressFormat.PNG,100, fOutStream);

             // Flush the FileOutputStream
             fOutStream.flush();

            //Close the FileOutputStream
            fOutStream.close();

             // Create a hashmap object to store image path and its position in the listview
             HashMap<String, Object> hmBitmap = new HashMap<String, Object>();

             // Storing the path to the temporary image file
             hmBitmap.put("flag",tmpFile.getPath());

             // Storing the position of the image in the listview
//             hmBitmap.put("position",position);

             // Returning the HashMap object containing the image path and position
             return hmBitmap;

         }catch (Exception e) {
             e.printStackTrace();
         }
         return null;
     }

   

	@Override
     protected void onPostExecute(HashMap<String, Object> result) {
       displayDownloadedImages(result);
     }
	
	/*migration issue
	 public boolean onCreateOptionsMenu(Menu menu) {
	        getMenuInflater().inflate(R.menu.activity_main, menu);
	        return true;
	    }
	*/

	
 }
}


