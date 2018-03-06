package com.example.arek.movies.repository.db;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by Arkadiusz Wilczek on 06.03.18.
 */

public class MoviesProvider extends ContentProvider {
    MovieDbHelper mMovieDbHelper;
    private static UriMatcher sUriMatcher = buildUriMatcher();

    private static final int MOVIES = 1;
    private static final int MOVIE_WITH_ID = 2;


    private static UriMatcher buildUriMatcher(){
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(MovieDbContract.CONTENT_AUTHORITY,MovieDbContract.MOVIE_PATH,MOVIES);
        uriMatcher.addURI(MovieDbContract.CONTENT_AUTHORITY,MovieDbContract.MOVIE_PATH + "/#",MOVIE_WITH_ID);
        return uriMatcher;
    }

    @Override
    public boolean onCreate() {
        mMovieDbHelper = new MovieDbHelper(getContext());
        return false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        final SQLiteDatabase db = mMovieDbHelper.getReadableDatabase();
        Cursor retCursor;
        switch ( sUriMatcher.match(uri) ) {
            case MOVIES:
                retCursor = db.query(MovieDbContract.MovieEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        null);
                break;
            case MOVIE_WITH_ID:
                long id = MovieDbContract.MovieEntry.getIdFromUri(uri);
                retCursor = db.query(MovieDbContract.MovieEntry.TABLE_NAME,
                        projection,
                        MovieDbContract.MovieEntry._ID + "=?",
                        new String[]{String.valueOf(id)},
                        null,
                        null,
                        null);
                break;
            default:
                throw new UnsupportedOperationException("Unknown Uri:" + uri);
        }
         retCursor.setNotificationUri(getContext().getContentResolver(),uri);
        return retCursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch ( sUriMatcher.match(uri) ){
            case MOVIES:
                return MovieDbContract.MovieEntry.CONTENT_TYPE;
            case MOVIE_WITH_ID:
                return MovieDbContract.MovieEntry.CONTENT_ITEM_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown Uri:" + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        final SQLiteDatabase db = mMovieDbHelper.getWritableDatabase();
        Uri returnUri;
        switch ( sUriMatcher.match(uri) ){
            case MOVIES:{
                long id = db.insert(MovieDbContract.MovieEntry.TABLE_NAME,
                        null,
                        values);
                if ( id > 0 ){
                    returnUri = MovieDbContract.MovieEntry.buildMovieUri(id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown Uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri,null);
        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        final SQLiteDatabase db = mMovieDbHelper.getWritableDatabase();
        int rowDeleted = 0;
        switch ( sUriMatcher.match(uri) ){
            case MOVIES:{
                 rowDeleted = db.delete(MovieDbContract.MovieEntry.TABLE_NAME,
                        selection,
                        selectionArgs);
                break;
            }
            case MOVIE_WITH_ID:{
                long id = MovieDbContract.MovieEntry.getIdFromUri(uri);
                if ( id > 0 ) {
                    rowDeleted = db.delete(MovieDbContract.MovieEntry.TABLE_NAME,
                            MovieDbContract.MovieEntry._ID + "=?",
                            new String[]{String.valueOf(id)});
                }
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown Uri: " + uri);
        }
        if ( rowDeleted > 0 )
            getContext().getContentResolver().notifyChange(uri,null);
        return rowDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
