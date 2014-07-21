package com.pranav.tmdb_api_movie;

//import com.daginge.tmdbsearch.Display_With_Image.ImageDownloading;
import com.pranav.tmdb_api_movie.R;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
//lazy 

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;

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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	/**
	 * Fires an intent to the TMDBSearchResultActivity with the query.
	 * 
	 */

	public void queryTMDB(View view) {
		Intent intent = new Intent(this, TMDBSearchResultActivity.class);
		// Intent intent = new Intent(this, Now_Viewing.class);
		EditText editText = (EditText) findViewById(R.id.edit_message);
		String query = editText.getText().toString();
		intent.putExtra(EXTRA_QUERY, query);
		startActivity(intent);
	}

}
