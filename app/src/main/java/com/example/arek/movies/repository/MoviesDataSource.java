package com.example.arek.movies.repository;

import com.example.arek.movies.model.Movie;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by Arkadiusz Wilczek on 03.03.18.
 */

public interface MoviesDataSource{

    Observable<List<Movie>> getMovies(int sortType);

    void saveFavoriteMovie(Movie movie);

    void deleteFavoriteMovie(Movie movie);

}
