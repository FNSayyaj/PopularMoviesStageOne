package com.example.knightrider.popularmoviesstage1;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.android.volley.Request.Method.GET;

public class MainMovieActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MoviesAdapter adapter;
    private List<Movie> movieList;
    private final String URL_TOP_RATED = "https://api.themoviedb.org/3/movie/top_rated?api_key=PUT YOUR API KY HERE";
    private final String URL_POPULAR = "https://api.themoviedb.org/3/movie/popular?api_key=PUT YOUR API KY HERE";
    private String CHOSEN_URL = URL_TOP_RATED;
    private TextView NO_INTERNET;
    private Button REFRESH;
    private final Context context = this;
    private boolean POPULATED_UI_SUCCESSFULLY = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_movie);

        LoadPreferences();

        recyclerView = findViewById(R.id.recyclerview);
        GridLayoutManager layoutManager = new GridLayoutManager(context, 2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        movieList = new ArrayList<>();

        if(isNetworkAvailable()){
            hideConnectionErrors();
            loadMovieData(CHOSEN_URL);
            POPULATED_UI_SUCCESSFULLY = true;}
        else {

            showConnectionErrors();
        }
        REFRESH.setOnClickListener(v -> {
            if(isNetworkAvailable()) {
                recyclerView = findViewById(R.id.recyclerview);
                GridLayoutManager layoutManager1 = new GridLayoutManager(context, 2);
                recyclerView.setLayoutManager(layoutManager1);
                recyclerView.setHasFixedSize(true);
                movieList = new ArrayList<>();
                hideConnectionErrors();
                loadMovieData(CHOSEN_URL);
                POPULATED_UI_SUCCESSFULLY = true;
            }
            else{
                Toast.makeText(MainMovieActivity.this,
                        getResources().getText(R.string.no_connectivity_error),
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    private void loadMovieData(String url){

        final String KEY_POSTER_PATH = "poster_path";
        final String KEY_TITLE = "original_title";
        final String KEY_RELEASE_DATE = "release_date";
        final String KEY_RATE = "vote_average";
        final String KEY_PLOT = "overview";
        final String KEY_RESULTS = "results";

        StringRequest stringRequest = new StringRequest(GET, url, response -> {

            try {
                JSONObject jsonObject = new JSONObject(response);
                JSONArray results = jsonObject.optJSONArray(KEY_RESULTS);
                for (int i = 0; i < results.length(); i++){
                    JSONObject jo = results.optJSONObject(i);
                    Movie movies = new Movie(jo.optString(KEY_TITLE),
                            jo.optString(KEY_RELEASE_DATE),
                            jo.optDouble(KEY_RATE),
                            jo.optString(KEY_POSTER_PATH),
                            jo.optString(KEY_PLOT));
                    movieList.add(movies);
                }
                adapter = new MoviesAdapter(movieList, getApplicationContext());
                recyclerView.setAdapter(adapter);

            }catch (JSONException e){
                e.printStackTrace();
            }
        }, error -> Toast.makeText(MainMovieActivity.this,
                getResources().getText(R.string.no_connectivity_error),
                Toast.LENGTH_LONG).show()
        );
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.movie_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id) {

            case R.id.popular:
                if(isNetworkAvailable()){

                    CHOSEN_URL = URL_POPULAR;
                    setActivityName();
                    SavePreferences();
                    loadMovieData(CHOSEN_URL);
                    if (POPULATED_UI_SUCCESSFULLY){
                        adapter.refreshEvents(movieList);}
                        hideConnectionErrors();
                }else {
                    Toast.makeText(MainMovieActivity.this,
                            getResources().getText(R.string.no_connectivity_error),
                            Toast.LENGTH_LONG).show();
                }
                break;

            case R.id.top_rate:
                if(isNetworkAvailable()) {

                    CHOSEN_URL = URL_TOP_RATED;
                    setActivityName();
                    SavePreferences();
                    loadMovieData(CHOSEN_URL);
                    if (POPULATED_UI_SUCCESSFULLY){
                        adapter.refreshEvents(movieList);}
                        hideConnectionErrors();
                }else {
                    Toast.makeText(MainMovieActivity.this,
                            getResources().getText(R.string.no_connectivity_error),
                            Toast.LENGTH_LONG).show();
                }
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setActivityName(){

        if (CHOSEN_URL.equals(URL_POPULAR)){ setTitle(getResources().getText(R.string.popular_movie_title));}
        if (CHOSEN_URL.equals(URL_TOP_RATED)) {setTitle(getResources().getText(R.string.top_rated_movies_title));}
    }

    private void SavePreferences(){

        SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("sorting", CHOSEN_URL);
        editor.apply();
    }
    private void LoadPreferences(){
        SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
        CHOSEN_URL = sharedPreferences.getString("sorting", CHOSEN_URL);
    }

    private boolean isNetworkAvailable() {

        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = null;
        if (connectivityManager != null) {
            activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        }
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void defineErrorViews(){
        NO_INTERNET = findViewById(R.id.nointernettextview);
        REFRESH = findViewById(R.id.refreshbutton);
    }

    private void showConnectionErrors(){
        defineErrorViews();
        NO_INTERNET.setVisibility(View.VISIBLE);
        REFRESH.setVisibility(View.VISIBLE);
    }
    private void hideConnectionErrors(){
        defineErrorViews();
        NO_INTERNET.setVisibility(View.INVISIBLE);
        REFRESH.setVisibility(View.INVISIBLE);
    }

}
