package com.example.knightrider.popularmoviesstage1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class DetailedMovieActivity extends AppCompatActivity {

    private static final String KEY_POSTER_PATH = "posterurl";
    private static final String KEY_TITLE = "title";
    private static final String KEY_RELEASE_DATE = "releasedate";
    private static final String KEY_RATE = "rate";
    private static final String KEY_PLOT = "plot";
    private final Double rate = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_movie);



        TextView title_tv = findViewById(R.id.movieTitle_tv);
        TextView releaseDate_tv = findViewById(R.id.releaseDate_tv);
        TextView plot_tv = findViewById(R.id.plot_tv);
        TextView vote_iv = findViewById(R.id.vote_iv);
        final ProgressBar progressBar = findViewById(R.id.progressBar_iv);
        final ImageView poster_iv = findViewById(R.id.moviePoster_iv);
        progressBar.setVisibility(View.VISIBLE);




        Intent intent = getIntent();
        if (intent != null){

            String movieTitle = intent.getStringExtra(KEY_TITLE);
            String movieReleaseDate = intent.getStringExtra(KEY_RELEASE_DATE);
            String moviePosterPath = intent.getStringExtra(KEY_POSTER_PATH);
            String moviePlotSynopsis = intent.getStringExtra(KEY_PLOT);
            String voteAverage = String.valueOf(intent.getDoubleExtra(KEY_RATE, rate));
            voteAverage = voteAverage + " " + getResources().getText(R.string.vote_of_ten);
            String imagePosterLink = getResources().getText(R.string.primary_poster_path) + moviePosterPath;

            setTitle(null);

            title_tv.setText(movieTitle);
            plot_tv.setText(moviePlotSynopsis);
            vote_iv.setText(voteAverage);
            releaseDate_tv.setText(movieReleaseDate);

            Picasso.with(this).load(imagePosterLink).into(poster_iv, new Callback.EmptyCallback() {
                //make the progressbar disappear when the image is done loading
                @Override public void onSuccess() {
                    progressBar.setVisibility(View.INVISIBLE);

                }

                @Override
                public void onError() {
                    progressBar.setVisibility(View.INVISIBLE);
                    poster_iv.setImageResource(R.drawable.ic_image_failed);
                }
            });
        }
    }
}
