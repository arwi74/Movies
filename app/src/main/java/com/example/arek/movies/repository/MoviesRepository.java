package com.example.arek.movies.repository;

import android.app.Application;
import android.util.Log;

import com.example.arek.movies.MoviesApp;
import com.example.arek.movies.api.MovieDbApi;
import com.example.arek.movies.model.Movie;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Arkadiusz Wilczek on 03.03.18.
 */

public class MoviesRepository implements MoviesDataSource {
    public MovieDbApi mMovieDbApi;
    int mSortType;
    int mPage;

    public MoviesRepository(MovieDbApi movieDbApi){
        mMovieDbApi = movieDbApi;
    }

    public MoviesRepository(){
    }

    @Override
    public Observable<List<Movie>> getMovies(int sortType) {
        return null;
    }

    private Observable<List<Movie>> getPopularMovies() {
        return mMovieDbApi.getMoviesPopular(mPage,"pl")
                .subscribeOn(Schedulers.io())
                .map(result -> result.getMovies());
    }

    private Observable<List<Movie>> getTopRatedMovies() {
        return mMovieDbApi.getMoviesPopular(mPage,"pl")
                .subscribeOn(Schedulers.io())
                .map(result -> result.getMovies());
    }


    @Override
    public void saveFavoriteMovie(Movie movie) {

    }

    @Override
    public void deleteFavoriteMovie(Movie movie) {

    }

    public void test(){
        Log.d("***","test");
    }
}
