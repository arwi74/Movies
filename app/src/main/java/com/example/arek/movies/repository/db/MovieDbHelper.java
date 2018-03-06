package com.example.arek.movies.repository.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Arkadiusz Wilczek on 05.03.18.
 */

public class MovieDbHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "movies";

    public MovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_MOVIE_TABLE =
                "CREATE TABLE " + MovieDbContract.MovieEntry.TABLE_NAME + " (" +
                        MovieDbContract.MovieEntry._ID + " INTEGER PRIMARY KEY ON CONFLICT REPLACE, " +
                        MovieDbContract.MovieEntry.COLUMN_ADULT + " INTEGER, " +
                        MovieDbContract.MovieEntry.COLUMN_BACKDROP_PATH + " TEXT, " +
                        MovieDbContract.MovieEntry.COLUMN_GENRE_IDS + " TEXT, " +
                        MovieDbContract.MovieEntry.COLUMN_ORIGINAL_LANGUAGE + " TEXT, " +
                        MovieDbContract.MovieEntry.COLUMN_ORIGINAL_TITLE + " TEXT, " +
                        MovieDbContract.MovieEntry.COLUMN_OVERVIEW + " TEXT, " +
                        MovieDbContract.MovieEntry.COLUMN_POPULARITY + " REAL, " +
                        MovieDbContract.MovieEntry.COLUMN_POSTER_PATH + " TEXT, " +
                        MovieDbContract.MovieEntry.COLUMN_RELEASE_DATE + " TEXT, " +
                        MovieDbContract.MovieEntry.COLUMN_TITLE + " TEXT, " +
                        MovieDbContract.MovieEntry.COLUMN_VIDEO + " INTEGER, " +
                        MovieDbContract.MovieEntry.COLUMN_VOTE_AVERAGE + " REAL, " +
                        MovieDbContract.MovieEntry.COLUMN_VOTE_COUNT + " INTEGER );";

        db.execSQL(SQL_CREATE_MOVIE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
