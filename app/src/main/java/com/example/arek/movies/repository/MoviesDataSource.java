package com.example.arek.movies.repository;

import com.example.arek.movies.model.Movie;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by Arkadiusz Wilczek on 03.03.18.
 * MoviesDataSource
 */

public interface MoviesDataSource{

    Observable<List<Movie>> getMovies(int sortType, boolean cached);

    void saveFavoriteMovie(Movie movie);

    void deleteFavoriteMovie(Movie movie);

    boolean isMoreMovies();

}
