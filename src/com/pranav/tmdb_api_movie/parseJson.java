package com.pranav.tmdb_api_movie;

 
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
 
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
 
/** A class to parse json data */
public class parseJson {
 
    // Receives a JSONObject and returns a list
    public List<HashMap<String,Object>> parse(JSONObject results){
 
        JSONArray movies = null;
        try {
            // Retrieves all the elements in the 'countries' array
            movies = results.getJSONArray("results");
        } catch (JSONException e) {
            e.printStackTrace();
        }
 
        // Invoking getCountries with the array of json object
        // where each json object represent a country
        return getMovieList(movies);
    }
 
    private List<HashMap<String, Object>> getMovieList(JSONArray movies){
        int numberOfMovies = movies.length();
        List<HashMap<String, Object>> movieList = new ArrayList<HashMap<String,Object>>();
        HashMap<String, Object> movie = null;
 
        // Taking each country, parses and adds to list object
        for(int i=0; i<numberOfMovies;i++){
            try {
                // Call getCountry with country JSON object to parse the country
                movie = getMovie((JSONObject)movies.get(i));
                movieList.add(movie);
 
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
 
        return movieList;
    }
 
    // Parsing the Country JSON object
    private HashMap<String, Object> getMovie(JSONObject jCountry){
 
        HashMap<String, Object> movieMap = new HashMap<String, Object>();
        String title = "";
        String poster="";
        String rating = "";
        String year = "";
        
        try {
            title = jCountry.getString("original_title");
            poster = jCountry.getString("poster_path");
            rating = jCountry.getString("vote_average");
            year = jCountry.getString("release_date").substring(0,5);
            
            String details =        "Title : " + title + "\n" +
                                "Poster : " + poster + "\n" +
                                "Rating : " + rating + 
                                "Year :" +year;
 
            movieMap.put("title", title);
           // country.put("poster", R.drawable.blank);
            movieMap.put("poster", poster);
            movieMap.put("details", details);
 
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return movieMap;
    }
}