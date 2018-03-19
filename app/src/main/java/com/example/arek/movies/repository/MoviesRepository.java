package com.example.arek.movies.repository;

import android.app.Application;
import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

import com.example.arek.movies.api.MovieDbApi;
import com.example.arek.movies.model.Genre;
import com.example.arek.movies.model.Movie;
import com.example.arek.movies.repository.db.MovieDbContract;
import com.example.arek.movies.utils.DbUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Arkadiusz Wilczek on 03.03.18.
 * MoviesRepository
 */

public class MoviesRepository implements MoviesDataSource {
    private static final int MAX_PAGE_COUNT = 100;
    public MovieDbApi mMovieDbApi;
    int mSortType;
    int mPage;
    public Application mApp ;
    public static final String LOG_TAG = MoviesDataSource.class.getSimpleName();

    private final List<Genre> mGenres = new ArrayList<>();
    private final List<Movie> mMoviesCache = new ArrayList<>();
    private List<Long> mFavoriteIds;

    public MoviesRepository(MovieDbApi movieDbApi, Application app){
        mApp = app;
        mMovieDbApi = movieDbApi;
    }


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
        String language = Locale.getDefault().getLanguage();
        mSortType = sortType;
        Log.d(LOG_TAG,"Sort type = " + mSortType);
        Log.d(LOG_TAG,"Page = " + mPage);

        if ( mSortType == Movie.SORT_MODE_POPULAR ){
            return getPopularMovies(language);
        }

        if ( mSortType == Movie.SORT_MODE_TOP_RATED ){
            return getTopRatedMovies(language);
        }

        if ( mSortType == Movie.SORT_MODE_FAVORITES ){
            return getFavoriteMovies();
        }

        return null;
    }

    private Observable<List<Movie>> getCachedMovies() {
        return  Observable.just(mMoviesCache)
                .map(movies -> setFavoritesMovies(movies,getFavoriteId()));
    }

    private Observable<List<Movie>> getPopularMovies(String language) {
        return mMovieDbApi.getMoviesPopular(mPage, language)
                .subscribeOn(Schedulers.io())
                .map(result -> result.getMovies())
                .map(movies -> setFavoritesMovies(movies,getFavoriteId()))
                .map(movies -> {
                    mMoviesCache.addAll(movies);
                    return movies;
                });
    }

    private Observable<List<Movie>> getTopRatedMovies(String language) {
        return mMovieDbApi.getMoviesTopRated(mPage, language)
                .subscribeOn(Schedulers.io())
                .map(result -> result.getMovies())
                .map(movies -> setFavoritesMovies(movies,getFavoriteId()))
                .map(movies -> {
                    mMoviesCache.addAll(movies);
                    return movies;
                });
    }

    private Observable<List<Movie>> getFavoriteMovies() {
        ContentResolver contentResolver = mApp.getContentResolver();
        getMoviesFromDb(contentResolver);

        return Observable.just(contentResolver)
                .subscribeOn(Schedulers.io())
                .map(cr -> getMoviesFromDb(cr));
    }

    private List<Movie> getMoviesFromDb(ContentResolver contentResolver){
        List<Movie> movies = new ArrayList<>();
        Cursor cursor = contentResolver.query(MovieDbContract.MovieEntry.CONTENT_URI,
                null,
                null,
                null,
                null);

        while (cursor.moveToNext()){
            movies.add(DbUtils.CursorToMovie(cursor));
        }
        cursor.close();
        return movies;
    }

    @Override
    public void saveFavoriteMovie(Movie movie) {
        addFavoriteToCache(movie.getId());
        ContentValues values = DbUtils.movieToContentValues(movie);
        AsyncQueryHandler handler = new MovieAsyncQuery(mApp.getContentResolver());
        handler.startInsert(1,null, MovieDbContract.MovieEntry.CONTENT_URI, values);
    }

    @Override
    public void deleteFavoriteMovie(Movie movie) {
        removeFavoriteFromCache(movie.getId());
        long id = movie.getId();
        AsyncQueryHandler handler = new MovieAsyncQuery(mApp.getContentResolver());
        handler.startDelete(1,null,MovieDbContract.MovieEntry.buildMovieUri(id),
                null,
                null);
    }

    private void removeFavoriteFromCache(long id) {
        if ( mFavoriteIds != null ){
            while (mFavoriteIds.contains(id))
                mFavoriteIds.remove(id);
        }
    }

    private void addFavoriteToCache(Long id){
        mFavoriteIds.add(id);
    }

    @Override
    public boolean isMoreMovies() {
        if ( hasSortModePages() && mPage < MAX_PAGE_COUNT  ){
            return true;
        }
        return false;
    }

    private boolean hasSortModePages() { return mSortType != Movie.SORT_MODE_FAVORITES; }


    private List<Long> getFavoriteId() {
        if ( mFavoriteIds != null ) {
            return mFavoriteIds;
        }
        mFavoriteIds = new ArrayList<>();
        Cursor cursor = mApp.getContentResolver().query(MovieDbContract.MovieEntry.CONTENT_URI,
                new String[]{MovieDbContract.MovieEntry._ID},
                null,
                null,
                null);

        while (cursor.moveToNext()){
            mFavoriteIds.add(cursor.getLong(0));
        }
        cursor.close();
        return mFavoriteIds;
    }

    private List<Movie> setFavoritesMovies(List<Movie> movies, List<Long> favoriteIds){
        for (Movie movie: movies){
            if ( favoriteIds.contains(movie.getId()) ){
                movie.setFavorite(true);
            }else{
                movie.setFavorite(false);
            }
        }
        return movies;
    }

    private static class MovieAsyncQuery extends AsyncQueryHandler{
        public MovieAsyncQuery(ContentResolver cr) {
            super(cr);
        }
    }

    private Observable<List<Genre>> getGenresFromApi(){
        String language = Locale.getDefault().getLanguage();
        return mMovieDbApi.getGenres(language)
                .subscribeOn(Schedulers.io())
                .map(result -> result.getGenres())
                .map(genres -> {
                    mGenres.addAll(genres);
                    return genres;
                });
    }

    private Observable<List<Genre>> getGenresFromCache(){
        return Observable.just(mGenres);
    }

    public Observable<List<Genre>> getGenres(){
        if ( mGenres.isEmpty() ){
            return getGenresFromApi();
        } else {
            return getGenresFromCache();
        }
    }

}
