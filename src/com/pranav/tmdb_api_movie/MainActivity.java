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
    
}


