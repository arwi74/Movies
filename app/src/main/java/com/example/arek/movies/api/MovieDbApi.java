package com.example.arek.movies.api;

import com.example.arek.movies.model.MovieDbResult;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Arkadiusz Wilczek on 20.02.18.
 * Interface for retrofit
 */

public interface MovieDbApi {
    String BASE_URL = "https://api.themoviedb.org/";

    @GET("/3/movie/popular")
    Call<MovieDbResult> getMoviesPopular(
            @Query("api_key")String apiKey,
            @Query("page")int page,
            @Query("language")String language);

    @GET("/3/movie/top_rated")
    Call<MovieDbResult> getMoviesTopRated(
            @Query("api_key")String apiKey,
            @Query("page")int page,
            @Query("language")String language);

}
