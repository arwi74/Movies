package com.example.arek.movies.utils;

import android.net.Uri;


/**
 * Created by Arkadiusz Wilczek on 27.02.18.
 */

public class ApiImageUtils {
    private final static String IMAGE_BASE_PATH="http://image.tmdb.org/t/p";
    public final static String SIZE_W185="w185";
    public final static String SIZE_W500="w500";

    public static final Uri getImagePath(String size,String image){
        return Uri.parse(IMAGE_BASE_PATH).buildUpon()
                .appendPath(size)
                .appendPath(image)
                .build();
    }

}
