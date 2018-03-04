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
    public static final String LOG_TAG = MoviesDataSource.class.getSimpleName();

    public MoviesRepository(MovieDbApi movieDbApi){
        mMovieDbApi = movieDbApi;
    }

    @Override
    public Observable<List<Movie>> getMovies(int sortType) {
        if ( mSortType != sortType){
            mPage = 1;
        } else {
            mPage++;
        }
        mSortType = sortType;
        Log.d(LOG_TAG,"Sort type = " + mSortType);
        Log.d(LOG_TAG,"Page = " + mPage);

        if ( mSortType == Movie.SORT_MODE_POPULAR ){
            return getPopularMovies();
        }

        if ( mSortType == Movie.SORT_MODE_TOP_RATED ){
            return getTopRatedMovies();
        }
        return null;
    }

    private Observable<List<Movie>> getPopularMovies() {
        return mMovieDbApi.getMoviesPopular(mPage,"pl")
                .subscribeOn(Schedulers.io())
                .map(result -> result.getMovies());
    }

    private Observable<List<Movie>> getTopRatedMovies() {
        return mMovieDbApi.getMoviesTopRated(mPage,"pl")
                .subscribeOn(Schedulers.io())
                .map(result -> result.getMovies());
    }


    @Override
    public void saveFavoriteMovie(Movie movie) {

    }

    @Override
    public void deleteFavoriteMovie(Movie movie) {

    }

    @Override
    public boolean isMoreMovies() {
        return true;
    }


}
