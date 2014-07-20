package com.pranav.tmdb_api_movie;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;

import com.pranav.tmdb_api_movie.Display_With_Image.ImageDownloading;
import com.pranav.tmdb_api_movie.R;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;

//add
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


public class MainActivity extends Activity {
	String imgUrl;
	String strFinalUrl;

	

    public static final String EXTRA_MESSAGE = "";
    public static final String EXTRA_QUERY = "";
    ListView mListView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        
      
    }
    
    //all added only view function eixsted.
    private String downloadUrl(String strUrl) throws IOException{
        String data = "";
        InputStream iStream = null;
        try{
            URL url = new URL(strUrl);
 
            // Creating an http connection to communicate with url
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
 
            // Connecting to url
            urlConnection.connect();
 
            // Reading data from url
            iStream = urlConnection.getInputStream();
 
            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));
 
            StringBuffer sb  = new StringBuffer();
 
            String line = "";
            while( ( line = br.readLine())  != null){
                sb.append(line);
            }
 
            data = sb.toString();
 
            br.close();
 
        }catch(Exception e){
            Log.d("Exception while downloading url", e.toString());
        }finally{
            iStream.close();
        }
 
        return data;
    }
 
    /** AsyncTask to download json data */
    private class DownloadTask extends AsyncTask<String, Integer, String>{
        String data = null;
        @Override
        protected String doInBackground(String... url) {
            try{
                data = downloadUrl(strFinalUrl);
            }catch(Exception e){
                Log.d("Background Task",e.toString());
            }
            return data;
        }
 
        @Override
        protected void onPostExecute(String result) {
 
            // The parsing of the xml data is done in a non-ui thread
            ListViewLoaderTask listViewLoaderTask = new ListViewLoaderTask();
 
            // Start parsing xml data
            listViewLoaderTask.execute(result);
        }
    }

   
    
    /**
     * Fires an intent to the {@link TMDBSearchResultActivity} with the query.
     * {@link TMDBSearchResultActivity} does all the downloading and rendering.
     * @param view
     */
    
    public void queryTMDB(View view) {
//        Intent intent = new Intent(this, TMDBSearchResultActivity.class);
    	Intent intent = new Intent(this, Now_Viewing.class);
        EditText editText = (EditText) findViewById(R.id.edit_message);
        String query = editText.getText().toString();
        intent.putExtra(EXTRA_QUERY, query);
        startActivity(intent);
    }
    
    
    
    /** AsyncTask to parse json data and load ListView */
    class ListViewLoaderTask extends AsyncTask<String, Void, SimpleAdapter>{

       JSONObject jObject;
       // Doing the parsing of xml data in a non-ui thread
       @Override
       protected SimpleAdapter doInBackground(String... strJson) {
           try{
               jObject = new JSONObject(strJson[0]);
               parseJson movieJsonParser = new parseJson();
               movieJsonParser.parse(jObject);
           }catch(Exception e){
               Log.d("JSON Exception1",e.toString());
           }

           // Instantiating json parser class
           parseJson movieJsonParser = new parseJson();

           // A list object to store the parsed countries list
           List<HashMap<String, Object>> movies = null;

           try{
               // Getting the parsed data as a List construct
               movies = movieJsonParser.parse(jObject);
           }catch(Exception e){
               Log.d("Exception",e.toString());
           }

           // Keys used in Hashmap
           String[] from = { "title","poster","details"};

           // Ids of views in listview_layout
           int[] to = { R.id.name_of_movie,R.id.image_of_movie,R.id.movie_details};

           // Instantiating an adapter to store each items
           // R.layout.listview_layout defines the layout of each item
           SimpleAdapter adapter = new SimpleAdapter(getBaseContext(), movies, R.layout.movielist_with_image, from, to);

           return adapter;
       }

      
   }
    
}


