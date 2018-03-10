package com.example.knightrider.popularmoviesstage1;

/**
 * Created by KNIGHT RIDER on 3/10/2018.
 */

public class Movie {

    private final  String title;
    private final  String release_date;
    private final double vote_average;
    private final String poster_path;
    private final String overView;

    Movie(String title, String release_date, double vote_average, String poster_path, String overview) {

        this.title = title;
        this.release_date = release_date;
        this.vote_average = vote_average;
        this.poster_path = poster_path;
        this.overView = overview;
    }

    public String getTitle() {
        return title;
    }

    public String getReleaseDate() {
        return release_date;
    }

    public String getPosterPath() {
        return poster_path;
    }

    public double getVote() {
        return vote_average;
    }

    public String getOverview() {
        return overView;
    }
}
