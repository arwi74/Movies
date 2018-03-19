package com.example.arek.movies.repository;

import android.support.annotation.NonNull;

import com.example.arek.movies.api.MovieDbApi;
import com.example.arek.movies.model.Video;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Arkadiusz Wilczek on 07.03.18.
 * VideosRepository
 */

public class VideosRepository {
    MovieDbApi mMovieDbApi;


    public VideosRepository(@NonNull MovieDbApi movieDbApi){
        mMovieDbApi = movieDbApi;
    }

    public Observable<List<Video>> getVideos(long movieId){
        return mMovieDbApi.getVideos(movieId,  "")
                .subscribeOn(Schedulers.io())
                .map(result -> result.getVideos());
    }

}
