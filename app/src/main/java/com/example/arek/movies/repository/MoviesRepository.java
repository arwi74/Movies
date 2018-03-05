package com.example.arek.movies.repository;

import android.util.Log;

import com.example.arek.movies.api.MovieDbApi;
import com.example.arek.movies.model.Movie;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Arkadiusz Wilczek on 03.03.18.
 */

public class MoviesRepository implements MoviesDataSource {
    private static final int MAX_PAGE_COUNT = 10;
    public MovieDbApi mMovieDbApi;
    int mSortType;
    int mPage;
    public static final String LOG_TAG = MoviesDataSource.class.getSimpleName();

    private final List<Movie> mMoviesCache = new ArrayList<>();

    public MoviesRepository(MovieDbApi movieDbApi){
        mMovieDbApi = movieDbApi;
    }

    //TODO language settings

    @Override
    public Observable<List<Movie>> getMovies(int sortType, boolean cached) {
        if ( mSortType != sortType){
            mPage = 1;
            mMoviesCache.clear();
        } else {
            if ( cached && !mMoviesCache.isEmpty()){
                return getCachedMovies();
            }
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

    private Observable<List<Movie>> getCachedMovies() {
        return  Observable.just(mMoviesCache);
    }

    private Observable<List<Movie>> getPopularMovies() {
        return mMovieDbApi.getMoviesPopular(mPage,"pl")
                .subscribeOn(Schedulers.io())
                .map(result -> result.getMovies())
                .map(movies -> {
                    mMoviesCache.addAll(movies);
                    return movies;
                });
    }

    private Observable<List<Movie>> getTopRatedMovies() {
        return mMovieDbApi.getMoviesTopRated(mPage,"pl")
                .subscribeOn(Schedulers.io())
                .map(result -> result.getMovies())
                .map(movies -> {
                    mMoviesCache.addAll(movies);
                    return movies;
                });
    }


    @Override
    public void saveFavoriteMovie(Movie movie) {

    }

    @Override
    public void deleteFavoriteMovie(Movie movie) {

    }

    @Override
    public boolean isMoreMovies() {
        if ( hasSortModePages() && mPage < MAX_PAGE_COUNT  ){
            return true;
        }
        return false;
    }

    private boolean hasSortModePages() { return mSortType != Movie.SORT_MODE_FAVORITES; }

}
