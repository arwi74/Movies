package com.example.arek.movies.repository.db;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Arkadiusz Wilczek on 05.03.18.
 * MovieDbContract
 */

public class MovieDbContract {
    public static final String CONTENT_AUTHORITY = "com.example.arek.movies.app";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://"+CONTENT_AUTHORITY);

    public static final String MOVIE_PATH = "movie";


    public static final class MovieEntry implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(MOVIE_PATH).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + MOVIE_PATH;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + MOVIE_PATH;

        public static final Uri buildMovieUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI,id);
        }

        public static final long getIdFromUri(Uri uri){
            return ContentUris.parseId(uri);
        }

        public static final String TABLE_NAME = "movie";

        public static final String COLUMN_ADULT = "adult";
        public static final String COLUMN_BACKDROP_PATH = "backdrop_path";
        public static final String COLUMN_GENRE_IDS = "genre_ids";
        public static final String COLUMN_ORIGINAL_LANGUAGE = "original_language";
        public static final String COLUMN_ORIGINAL_TITLE = "original_title";
        public static final String COLUMN_OVERVIEW = "overview";
        public static final String COLUMN_POPULARITY = "popularity";
        public static final String COLUMN_POSTER_PATH = "poster_path";
        public static final String COLUMN_RELEASE_DATE = "release_date";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_VIDEO = "video";
        public static final String COLUMN_VOTE_AVERAGE = "vote_average";
        public static final String COLUMN_VOTE_COUNT = "vote_count";
    }

}
