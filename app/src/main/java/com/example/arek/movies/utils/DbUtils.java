package com.example.arek.movies.utils;

import android.content.ContentValues;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.arek.movies.model.Genre;
import com.example.arek.movies.model.Movie;
import com.example.arek.movies.repository.db.MovieDbContract;
import com.example.arek.movies.repository.db.MovieDbContract.MovieEntry;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Arkadiusz Wilczek on 05.03.18.
 */

public class DbUtils {

    public static ContentValues movieToContentValues(Movie movie) {
        ContentValues values = new ContentValues();
        values.put(MovieEntry._ID, movie.getId());
        values.put(MovieEntry.COLUMN_ADULT, boolToInt(movie.getAdult()));
        values.put(MovieEntry.COLUMN_BACKDROP_PATH, movie.getBackdropPath());
        values.put(MovieEntry.COLUMN_GENRE_IDS, genreIdsToString(movie.getGenreIds()));
        values.put(MovieEntry.COLUMN_ORIGINAL_LANGUAGE, movie.getOriginalLanguage());
        values.put(MovieEntry.COLUMN_ORIGINAL_TITLE, movie.getOriginalTitle());
        values.put(MovieEntry.COLUMN_OVERVIEW, movie.getOverview());
        values.put(MovieEntry.COLUMN_POPULARITY, movie.getPopularity());
        values.put(MovieEntry.COLUMN_POSTER_PATH, movie.getPosterPath());
        values.put(MovieEntry.COLUMN_RELEASE_DATE, movie.getReleaseDate());
        values.put(MovieEntry.COLUMN_TITLE, movie.getTitle());
        values.put(MovieEntry.COLUMN_VIDEO, boolToInt(movie.getVideo()));
        values.put(MovieEntry.COLUMN_VOTE_AVERAGE, movie.getVoteAverage());
        values.put(MovieEntry.COLUMN_VOTE_COUNT, movie.getVoteCount());
        return values;
    }

    public static Movie CursorToMovie(Cursor cursor) {
        if ( cursor == null ) return null;
        Movie movie = new Movie();
        movie.setTitle(getString(cursor, MovieEntry.COLUMN_TITLE));
        movie.setAdult(getBoolean(cursor, MovieEntry.COLUMN_ADULT));
        movie.setBackdropPath(getString(cursor, MovieEntry.COLUMN_BACKDROP_PATH));
        movie.setGenreIds(getGenreIds(cursor, MovieEntry.COLUMN_GENRE_IDS));
        movie.setId(getLong(cursor, MovieEntry._ID));
        movie.setOriginalLanguage(getString(cursor, MovieEntry.COLUMN_ORIGINAL_LANGUAGE));
        movie.setOriginalTitle(getString(cursor, MovieEntry.COLUMN_ORIGINAL_TITLE));
        movie.setOverview(getString(cursor, MovieEntry.COLUMN_OVERVIEW));
        movie.setPopularity(getDouble(cursor, MovieEntry.COLUMN_POPULARITY));
        movie.setPosterPath(getString(cursor, MovieEntry.COLUMN_POSTER_PATH));
        movie.setReleaseDate(getString(cursor, MovieEntry.COLUMN_RELEASE_DATE));
        movie.setVideo(getBoolean(cursor, MovieEntry.COLUMN_VIDEO));
        movie.setVoteAverage(getDouble(cursor, MovieEntry.COLUMN_VOTE_AVERAGE));
        movie.setVoteCount(getLong(cursor, MovieEntry.COLUMN_VOTE_COUNT));
        movie.setFavorite(true);
        return movie;
    }

    private static String getString(Cursor cursor, String columnName){
        return cursor.getString(cursor.getColumnIndex(columnName));
    }

    private static boolean getBoolean(Cursor cursor, String columnName){
        return intToBool(getInt(cursor, columnName));
    }

    private static int getInt(Cursor cursor, String columnName){
        return cursor.getInt(cursor.getColumnIndex(columnName));
    }

    private static long getLong(Cursor cursor, String columnName){
        return cursor.getLong(cursor.getColumnIndex(columnName));
    }

    private static double getDouble(Cursor cursor, String columnName){
        return cursor.getDouble(cursor.getColumnIndex(columnName));
    }

    private static List<Integer> getGenreIds(Cursor cursor, String columnName){
        return genreStringToIds(getString(cursor, columnName));

    }

    private static String genreIdsToString(List<Integer> genre) {
        if ( genre==null || genre.size()==0 ){
            return "";
        }
        StringBuilder sb = new StringBuilder();

        for ( int i = 0; i < genre.size(); i++){
            sb.append(genre.get(i));
            if ( i < genre.size()-1 ) sb.append(",");
        }

        return sb.toString();
    }

    private static List<Integer> genreStringToIds(String genre){
        List<Integer> list = new ArrayList<>();
        String[] genreArray = genre.split(",");
        try {
            for (int i = 0; i < genreArray.length; i++) {
                list.add(Integer.parseInt(genreArray[i]));
            }
        } catch(NumberFormatException e){
            e.printStackTrace();
        }

        return list;
    }

    private static int boolToInt(boolean b){
        return b ? 1:0;
    }

    private static boolean intToBool(int i){
        return i > 0;
    }

    private static String getGenre(@NonNull List<Genre> genres, int id){
        for(Genre genre: genres){
            if ( genre.getId() == id ){
                return genre.getName();
            }
        }
        return "";
    }

    public static String getGenresFromIds(@NonNull List<Integer> ids, @NonNull List<Genre> genres){
        if ( ids.isEmpty() ) return "";
        StringBuilder sb = new StringBuilder();
        sb.append(getGenre(genres,ids.get(0)));
        for (int i = 1; i<ids.size(); i++){
            sb.append(", ");
            sb.append(getGenre(genres,ids.get(i)));
        }
        return sb.toString();
    }

}
