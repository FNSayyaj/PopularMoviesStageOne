package com.example.knightrider.popularmoviesstage1;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import java.util.List;

/**
 * Created by KNIGHT RIDER on 3/10/2018.
 */

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.ViewHolder> {

    private static final String KEY_POSTER_PATH = "posterurl";
    private static final String KEY_TITLE = "title";
    private static final String KEY_RELEASE_DATE = "releasedate";
    private static final String KEY_RATE = "rate";
    private static final String KEY_PLOT = "plot";

    private final  List<Movie> movies;
    private final Context context;

    public MoviesAdapter(List<Movie> movies, Context context){

        this.movies = movies;
        this.context = context;

    }

    @NonNull
    @Override
    public MoviesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycled_movies_items, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MoviesAdapter.ViewHolder holder, final int position) {

        final Movie movie = movies.get(position);
        Picasso.with(this.context).load(context.getText(R.string.primary_poster_path) + movie.getPosterPath()).into(holder.poster_path,
                new Callback.EmptyCallback() {

                    @Override
                    public void onError() {

                        holder.poster_path.setImageResource(R.drawable.ic_image_failed);
                    }
                });
        holder.linearLayout.setOnClickListener(v -> {
            Movie movie1 = movies.get(position);
            Intent intent = new Intent(v.getContext(), DetailedMovieActivity.class);
            intent.putExtra(KEY_POSTER_PATH, movie1.getPosterPath());
            intent.putExtra(KEY_TITLE, movie1.getTitle());
            intent.putExtra(KEY_RELEASE_DATE, movie1.getReleaseDate());
            intent.putExtra(KEY_RATE, movie1.getVote());
            intent.putExtra(KEY_PLOT, movie1.getOverview());
            v.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        final ImageView poster_path;
        final LinearLayout linearLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            poster_path = itemView.findViewById(R.id.poster_image);
            linearLayout = itemView.findViewById(R.id.linearLayout);
        }
    }
    public void refreshEvents(List<Movie> movies) {
        this.movies.clear();
        this.movies.addAll(movies);
        notifyDataSetChanged();
    }
}