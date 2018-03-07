package com.example.arek.movies.di;

import android.app.Application;

import com.example.arek.movies.api.MovieDbApi;
import com.example.arek.movies.repository.MoviesRepository;
import com.example.arek.movies.repository.ReviewsRepository;
import com.example.arek.movies.repository.VideosRepository;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Arkadiusz Wilczek on 03.03.18.
 */

@Module
public class RepositoryModule {

    @Provides
    @Singleton
    MoviesRepository provideMoviesRepository(MovieDbApi movieDbApi,Application app){
        return new MoviesRepository(movieDbApi, app);
    }

    @Provides
    @Singleton
    VideosRepository provideVideosRepository(MovieDbApi movieDbApi){
        return new VideosRepository(movieDbApi);
    }

    @Provides
    @Singleton
    ReviewsRepository provideReviewsRepository(MovieDbApi movieDbApi){
        return new ReviewsRepository(movieDbApi);
    }

}
