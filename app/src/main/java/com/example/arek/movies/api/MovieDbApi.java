package com.example.arek.movies.api;

import com.example.arek.movies.model.MovieDbResult;
import com.example.arek.movies.model.ReviewsResult;
import com.example.arek.movies.model.VideoResult;


import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Arkadiusz Wilczek on 20.02.18.
 * Interface for retrofit
 */

public interface MovieDbApi {
    String BASE_URL = "https://api.themoviedb.org/";

    @GET("/3/movie/popular")
    Observable<MovieDbResult> getMoviesPopular(
            @Query("page")int page,
            @Query("language")String language);

    @GET("/3/movie/top_rated")
    Observable<MovieDbResult> getMoviesTopRated(
            @Query("page")int page,
            @Query("language")String language);

    @GET("/3/movie/{id}/reviews")
    Observable<ReviewsResult> getReviews(
            @Path("id")long id,
            @Query("page")int page,
            @Query("language")String language);

    @GET("/3/movie/{id}/videos")
    Observable<VideoResult> getVideos(
            @Path("id")long id,
            @Query("language")String language);

}
